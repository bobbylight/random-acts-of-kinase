package org.sgc.rak.repositories;

import org.sgc.rak.model.Feedback;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for feedback.
 */
public interface FeedbackRepository extends PagingAndSortingRepository<Feedback, Long> {
}
