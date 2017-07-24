package org.sgc.rak.repositories;

import org.sgc.rak.model.KinaseActivityProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for kinase activity profiles.
 */
public interface KinaseActivityProfileRepository extends PagingAndSortingRepository<KinaseActivityProfile, String> {

    Page<KinaseActivityProfile> getKinaseActivityProfilesByCompoundNameIgnoreCase(String compoundName,
                                                                                  Pageable pageInfo);

    Page<KinaseActivityProfile> getKinaseActivityProfilesByCompoundNameIgnoreCaseAndKinaseIgnoreCaseAndPercentControl(
        String compoundName, String kinase, double activity, Pageable pageInfo);

    Page<KinaseActivityProfile> getKinaseActivityProfilesByKinaseIgnoreCaseAndPercentControl(String kinase,
                                                   double activity, Pageable pageInfo);
}