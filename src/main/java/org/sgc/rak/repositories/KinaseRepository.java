package org.sgc.rak.repositories;

import org.sgc.rak.model.Kinase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for kinases.
 */
public interface KinaseRepository extends PagingAndSortingRepository<Kinase, String> {

    Page<Kinase> getKinasesByDiscoverxGeneSymbolStartsWithIgnoreCase(String prefix, Pageable pageInfo);
}
