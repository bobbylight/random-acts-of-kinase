package org.sgc.rak.repositories;

import org.sgc.rak.model.Compound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for compounds.
 */
public interface CompoundRepository extends PagingAndSortingRepository<Compound, String> {

    /**
     * This method is only here until we no longer have to hide certain compounds
     * (those flagged with source = "hide").  At that point we can replace calls to this
     * with findAll().
     *
     * @param pageInfo How to sort the data and what page of data to return.
     * @return The list of compounds.
     */
    @Query("from Compound c where c.source is null")
    Page<Compound> findSourceIsNull(Pageable pageInfo);

    /**
     * Returns compounds without smiles strings or s10 values defined.
     *
     * @param pageInfo How to sort the data and what page of data to return.
     * @return The list of compounds.
     */
    @Query("from Compound c where c.smiles is null or s10 is null")
    Page<Compound> findSmilesIsNullOrS10IsNull(Pageable pageInfo);

    /**
     * Returns compounds whose names start with a given string, ignoring case.
     *
     * @param compoundNamePart The start of a compound name.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    Page<Compound> getCompoundsByCompoundNameStartsWithIgnoreCaseAndSourceIsNull(String compoundNamePart,
                                                                                 Pageable pageInfo);
}
