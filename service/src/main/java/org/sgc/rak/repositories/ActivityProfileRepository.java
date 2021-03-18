package org.sgc.rak.repositories;

import org.sgc.rak.model.ActivityProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for kinase activity profiles.
 */
public interface ActivityProfileRepository extends PagingAndSortingRepository<ActivityProfile, String>,
        JpaSpecificationExecutor<ActivityProfile> {

    Optional<ActivityProfile> findByCompoundNameAndKinaseDiscoverxGeneSymbol(String compoundName,
                                                                             String discoverx);

    Page<ActivityProfile> getActivityProfilesByCompoundNameIgnoreCase(String compoundName,
                                                                    Pageable pageInfo);

    Page<ActivityProfile>
            getActivityProfilesByCompoundNameIgnoreCaseAndKinaseIdAndPercentControlLessThanEqual(
        String compoundName, long kinase, double activity, Pageable pageInfo);

    Page<ActivityProfile> getActivityProfilesByKinaseIdInAndPercentControlLessThanEqual(List<Long> kinases,
                                                                                double activity, Pageable pageInfo);

    Page<ActivityProfile> getActivityProfilesByKinaseIdInAndKdLessThanEqual(List<Long> kinases, double kd,
                                                                            Pageable pageInfo);
}
