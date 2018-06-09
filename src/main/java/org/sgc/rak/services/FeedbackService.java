package org.sgc.rak.services;

import org.sgc.rak.model.Feedback;
import org.sgc.rak.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service for manipulating feedback.
 */
@Service
public class FeedbackService {

    private final FeedbackRepository repository;

    @Autowired
    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new feedback entry.
     *
     * @param feedback The feedback entry to create.
     */
    public void createFeedback(Feedback feedback) {
        feedback.setCreateDate(new Date());
        repository.save(feedback);
    }

    /**
     * Deletes a feedback entry.
     *
     * @param id The ID of the feedback entry to delete.
     */
    public void deleteFeedback(Long id) {
        repository.deleteById(id);
    }

    /**
     * Returns feedback.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of feedback entries.
     */
    public Page<Feedback> getFeedback(Pageable pageInfo) {
        return repository.findAll(pageInfo);
    }

    /**
     * Returns whether a feedback entry exists.
     *
     * @param id The ID of the feedback entry to search for.
     * @return Whether the feedback entry exists.
     */
    public boolean getFeedbackExists(Long id) {
        return repository.existsById(id);
    }
}
