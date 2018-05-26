package org.sgc.rak.rest;

import org.sgc.rak.model.Feedback;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST API for feedback.
 */
@RestController
@RequestMapping(path = "/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * Creates a new feedback entry.
     *
     * @param feedback The feedback entry to create.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    void createFeedback(@Valid @RequestBody Feedback feedback) {
        feedbackService.createFeedback(feedback);
    }

    /**
     * Returns feedback entries.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of feedback entries.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<Feedback> getFeedback(@SortDefault(value = "createDate",
        direction = Sort.Direction.DESC) Pageable pageInfo) {
        Page<Feedback> page = feedbackService.getFeedback(pageInfo);
        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}
