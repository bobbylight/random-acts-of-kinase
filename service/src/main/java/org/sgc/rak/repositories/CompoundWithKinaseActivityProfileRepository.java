package org.sgc.rak.repositories;

import org.sgc.rak.model.CompoundWithKinaseActivityProfile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for the "compounds with kinase activity profiles" view.
 */
public interface CompoundWithKinaseActivityProfileRepository
    extends PagingAndSortingRepository<CompoundWithKinaseActivityProfile, String>,
        JpaSpecificationExecutor<CompoundWithKinaseActivityProfile> {
}
