package org.sgc.rak.repositories;

import org.sgc.rak.model.Compound;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * JPA repository for compounds.
 */
public interface CompoundRepository extends PagingAndSortingRepository<Compound, String>,
        JpaSpecificationExecutor<Compound> {

    List<Compound> findByCompoundNameInIgnoreCase(List<String> compoundNames);
}
