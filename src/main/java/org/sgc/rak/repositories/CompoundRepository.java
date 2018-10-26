package org.sgc.rak.repositories;

import org.sgc.rak.model.Compound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * JPA repository for compounds.
 */
public interface CompoundRepository extends PagingAndSortingRepository<Compound, String>,
        JpaSpecificationExecutor<Compound> {

    List<Compound> findByCompoundNameInIgnoreCase(List<String> compoundNames);

    /**
     * Returns compounds whose names start contain a given substring, ignoring case.
     *
     * @param compoundNamePart A part of a compound name.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    Page<Compound> getCompoundsByCompoundNameContainsIgnoreCaseAndHiddenFalse(String compoundNamePart,
                                                                              Pageable pageInfo);
}
