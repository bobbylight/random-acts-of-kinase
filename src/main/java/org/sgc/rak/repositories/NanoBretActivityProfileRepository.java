package org.sgc.rak.repositories;

import org.sgc.rak.model.NanoBretActivityProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * JPA repository for NanoBRET activity profiles.
 */
public interface NanoBretActivityProfileRepository extends PagingAndSortingRepository<NanoBretActivityProfile, String>,
        JpaSpecificationExecutor<NanoBretActivityProfile> {

    Optional<NanoBretActivityProfile> findByCompoundNameAndKinaseDiscoverxGeneSymbol(String compoundName,
                                                                             String discoverx);

    Page<NanoBretActivityProfile> getActivityProfilesByCompoundNameIgnoreCase(String compoundName,
                                                                      Pageable pageInfo);
}
