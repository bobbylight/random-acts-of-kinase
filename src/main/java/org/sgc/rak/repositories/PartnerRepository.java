package org.sgc.rak.repositories;

import org.sgc.rak.model.Partner;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for partners.
 */
public interface PartnerRepository extends PagingAndSortingRepository<Partner, Long> {
}
