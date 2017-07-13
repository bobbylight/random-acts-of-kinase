package org.sgc.rak.dao;

import org.sgc.rak.model.Compound;
import org.sgc.rak.repositories.CompoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * DAO for manipulating compounds.
 */
public class CompoundDao {

    @Autowired
    private CompoundRepository compoundRepository;

    /**
     * Returns a compound by name.
     *
     * @param compoundName The compound name.
     * @return The compound, or {@code null} if no such compound is known.
     */
    public Compound getCompound(String compoundName) {
        return compoundRepository.findOne(compoundName);
    }

    /**
     * Returns compound information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompoundsByCompoundNameStartsWithIgnoreCase(String, Pageable)
     */
    public Page<Compound> getCompounds(Pageable pageInfo) {
        return compoundRepository.findAll(pageInfo);
    }

    /**
     * Returns compounds whose names start with a given string, ignoring case.
     *
     * @param compoundNamePart The start of a compound name.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompounds(Pageable)
     */
    public Page<Compound> getCompoundsByCompoundNameStartsWithIgnoreCase(String compoundNamePart,
                                                               Pageable pageInfo) {
        return compoundRepository.getCompoundsByCompoundNameStartsWithIgnoreCaseAndSourceIsNull(compoundNamePart, pageInfo);
    }
}
