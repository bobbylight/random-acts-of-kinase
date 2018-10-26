package org.sgc.rak.dao;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.CompoundCountPair;
import org.sgc.rak.repositories.CompoundRepository;
import org.sgc.rak.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

import static org.sgc.rak.util.QuerySpecifications.hasNullFields;
import static org.sgc.rak.util.QuerySpecifications.isHidden;

/**
 * DAO for manipulating compounds.
 */
public class CompoundDao {

    @Autowired
    private CompoundRepository compoundRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * Returns a compound by name.
     *
     * @param compoundName The compound name.
     * @return The compound, or {@code null} if no such compound is known.
     * @see #getCompoundExists(String)
     */
    public Compound getCompound(String compoundName) {
        return compoundRepository.findById(compoundName).orElse(null);
    }

    /**
     * Returns whether a compound exists.
     *
     * @param compoundName The name of the compound to search for.
     * @return Whether the compound exists.
     * @see #getCompound(String)
     */
    public boolean getCompoundExists(String compoundName) {
        return compoundRepository.existsById(compoundName);
    }

    /**
     * Returns compound information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompoundsByCompoundNameContainsIgnoreCase(String, Pageable)
     */
    public Page<Compound> getCompounds(Pageable pageInfo) {
        return compoundRepository.findAll(isHidden(null, false), pageInfo);
    }

    public List<Compound> getCompounds(List<String> compoundNames) {
        return compoundRepository.findByCompoundNameInIgnoreCase(compoundNames);
    }

    /**
     * Returns compounds whose names contain a given substring, ignoring case.
     *
     * @param compoundNamePart A pert of a compound name.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompounds(Pageable)
     */
    public Page<Compound> getCompoundsByCompoundNameContainsIgnoreCase(String compoundNamePart, Pageable pageInfo) {
        return compoundRepository.getCompoundsByCompoundNameContainsIgnoreCaseAndHiddenFalse(
            compoundNamePart, pageInfo);
    }

    /**
     * Returns information about compounds that are missing activity profiles.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    @SuppressWarnings("MagicNumber")
    public Page<CompoundCountPair> getCompoundsMissingActivityProfiles(String compound, Pageable pageInfo) {

        // TODO: kinaseRepository.getAnalyzedKinaseCount();
        long kinaseCount = 468; //kinaseRepository.count();

        // TODO: This Postgres-specific query also checks the count for each compound name.
        // I don't think this can be converted to jpql, due to an inability
        // to do an inner select in the join clause (needed to fetch compounds
        // with activity profile count > 0 but < 468), as well as path-defining
        // difficulties due to the left join
        StringBuilder sb = new StringBuilder("select compound.compound_nm, COALESCE(count, 0)\n" +
            "   from compound left join(\n" +
            "      select compound_nm, count(1) as count\n" +
            "         from kinase_activity_profile group by kinase_activity_profile.compound_nm\n" +
            "   ) countTable\n" +
            "   on compound.compound_nm = countTable.compound_nm\n" +
            "      where (count is null or count < ?)\n");

        if (StringUtils.isNotBlank(compound)) {
            sb.append("      and upper(compound.compound_nm) like upper(?)\n");
        }

        if (pageInfo.getSort() != null) {
            sb.append(' ').append(sortToOrderBy(pageInfo.getSort()));
        }

        sb.append("\n      offset ? limit ?");
        String sql = sb.toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, kinaseCount);

        int index = 2;
        if (StringUtils.isNotBlank(compound)) {
            query.setParameter(index++, '%' + Util.escapeForLike(compound) + '%');
        }
        query.setParameter(index++, pageInfo.getOffset());
        query.setParameter(index, pageInfo.getPageSize());

        @SuppressWarnings("unchecked")
        List<Object[]> objResult = query.getResultList();

        List<CompoundCountPair> compoundCountPairs = objResult.stream()
            .map(obj -> new CompoundCountPair(obj[0].toString(), ((Number)obj[1]).intValue()))
            .collect(Collectors.toList());

        sql = "select count(1)\n" +
            "   from compound left join (\n" +
            "      select compound_nm, count(1) as apCount\n" +
            "         from kinase_activity_profile group by kinase_activity_profile.compound_nm\n" +
            "   ) countTable\n" +
            "   on compound.compound_nm = countTable.compound_nm\n" +
            "   where (apCount is null or apCount < ?)";

        if (StringUtils.isNotBlank(compound)) {
            sql += "\n   and upper(compound.compound_nm) like upper(?)";
        }

        query = entityManager.createNativeQuery(sql);
        query.setParameter(1, kinaseCount);

        if (StringUtils.isNotBlank(compound)) {
            query.setParameter(2, '%' + Util.escapeForLike(compound) + '%');
        }

        long total = ((Number)query.getSingleResult()).longValue();

        return new PageImpl<>(compoundCountPairs, pageInfo, total);
    }

    /**
     * Returns information about compounds that are missing publication information.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    public Page<Compound> getCompoundsMissingPublicationInfo(String compound, Pageable pageInfo) {
        return compoundRepository.findAll(hasNullFields(compound, "primaryReference", "primaryReferenceUrl"), pageInfo);
    }

    /**
     * Returns information about compounds that are hidden.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    public Page<Compound> getHiddenCompounds(String compound, Pageable pageInfo) {
        return compoundRepository.findAll(isHidden(compound, true), pageInfo);
    }

    /**
     * Returns information on compounds without SMILES strings or s(10) values.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    public Page<Compound> getIncompleteCompounds(String compound, Pageable pageInfo) {
        return compoundRepository.findAll(hasNullFields(compound, "smiles", "s10"), pageInfo);
    }

    /**
     * Saves a single compound.
     *
     * @param compound The compound to save.
     * @return The saved compound.
     * @see #save(Iterable)
     */
    public Compound save(Compound compound) {
        return compoundRepository.save(compound);
    }

    /**
     * Saves a collection of compounds.
     *
     * @param compounds The compounds to save.
     * @see #save(Compound)
     */
    public void save(Iterable<Compound> compounds) {
        compoundRepository.saveAll(compounds);
    }

    /**
     * Converts a Spring Data {@code Sort} instance to an order by clause.  Typically this isn't
     * necessary as {@code Repository} instances handle sorting and paging for you, but we have a
     * native query we need to build.
     *
     * @param sort The sort instance, which cannot be {@code null}.
     * @return The order by clause.
     */
    static String sortToOrderBy(Sort sort) {

        StringBuilder sb = new StringBuilder("order by ");
        boolean first = true;

        for (Sort.Order order: sort) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            switch (order.getProperty()) {

                // "count" is a computed property in one of our native queries
                case "count":
                    sb.append("count ");
                    break;

                case "compoundName":
                    sb.append("compound_nm ");
                    break;

                default:
                    throw new RuntimeException("Unexpected order property: " + order.getProperty());
            }
            sb.append(order.getDirection());
        }

        return sb.toString();
    }
}
