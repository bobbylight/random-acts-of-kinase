package org.sgc.rak.repositories;

import org.sgc.rak.model.Kinase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * JPA repository for kinases.
 */
public interface KinaseRepository extends PagingAndSortingRepository<Kinase, String> {

    List<Kinase> findByEntrezGeneSymbolIgnoreCase(String entrez);

    Kinase findOneByDiscoverxGeneSymbolIgnoreCase(String discoverx);

    Page<Kinase> getKinasesByEntrezGeneSymbolStartsWithIgnoreCase(String prefix, Pageable pageInfo);
}
