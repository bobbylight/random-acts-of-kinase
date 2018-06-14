package org.sgc.rak.repositories;

import org.sgc.rak.model.Audit;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for audit records.
 */
public interface AuditRepository extends PagingAndSortingRepository<Audit, Long> {
}
