package org.sgc.rak.repositories;

import org.sgc.rak.model.Compound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for compounds.
 */
public interface CompoundRepository extends PagingAndSortingRepository<Compound, String> {

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
