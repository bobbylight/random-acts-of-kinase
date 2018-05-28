package org.sgc.rak.repositories;

import org.sgc.rak.model.Compound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * JPA repository for compounds.
 */
public interface CompoundRepository extends PagingAndSortingRepository<Compound, String> {

    List<Compound> findByCompoundNameInIgnoreCase(List<String> compoundNames);

    /**
     * Returns compounds that are not hidden.
     *
     * @param pageInfo How to sort the data and what page of data to return.
     * @return The list of compounds.
     */
    Page<Compound> findByHiddenFalse(Pageable pageInfo);

    /**
     * Returns compounds without smiles strings or s10 values defined.
     *
     * @param pageInfo How to sort the data and what page of data to return.
     * @return The list of compounds.
     */
    @Query("from Compound c where c.smiles is null or s10 is null")
    Page<Compound> findSmilesIsNullOrS10IsNull(Pageable pageInfo);

    /**
     * Returns compounds whose names start contain a given substring, ignoring case.
     *
     * @param compoundNamePart A part of a compound name.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    Page<Compound> getCompoundsByCompoundNameContainsIgnoreCaseAndHiddenFalse(String compoundNamePart,
                                                                              Pageable pageInfo);

    /**
     * Returns a list of compounds missing a primary reference field.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    Page<Compound> getCompoundsByPrimaryReferenceIsNullOrPrimaryReferenceUrlIsNull(Pageable pageInfo);
}
