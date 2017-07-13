package org.sgc.rak.repositories;

import org.sgc.rak.model.Kinase;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for kinases.
 */
public interface KinaseRepository extends PagingAndSortingRepository<Kinase, String> {
}
