package org.sgc.rak.repositories;

import org.sgc.rak.model.CompoundWithNanoBretActivityProfile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for the "compounds with NanoBRET activity profiles" view.
 */
public interface CompoundWithNanoBretActivityProfileRepository
    extends PagingAndSortingRepository<CompoundWithNanoBretActivityProfile, String>,
        JpaSpecificationExecutor<CompoundWithNanoBretActivityProfile> {
}
