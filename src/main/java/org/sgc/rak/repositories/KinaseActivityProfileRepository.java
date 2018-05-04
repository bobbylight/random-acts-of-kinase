package org.sgc.rak.repositories;

import org.sgc.rak.model.KinaseActivityProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * JPA repository for kinase activity profiles.
 */
public interface KinaseActivityProfileRepository extends PagingAndSortingRepository<KinaseActivityProfile, String> {

    Optional<KinaseActivityProfile> findByCompoundNameAndKinaseDiscoverxGeneSymbol(String compoundName,
                                                                                   String discoverx);

    Page<KinaseActivityProfile> getKinaseActivityProfilesByCompoundNameIgnoreCase(String compoundName,
                                                                                  Pageable pageInfo);

    Page<KinaseActivityProfile>
            getKinaseActivityProfilesByCompoundNameIgnoreCaseAndKinaseIdAndPercentControlLessThanEqual(
        String compoundName, long kinase, double activity, Pageable pageInfo);

    Page<KinaseActivityProfile> getKinaseActivityProfilesByKinaseIdAndPercentControlLessThanEqual(long kinase,
                                                                              double activity, Pageable pageInfo);
}
