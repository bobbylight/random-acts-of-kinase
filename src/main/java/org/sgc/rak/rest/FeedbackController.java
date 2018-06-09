package org.sgc.rak.rest;

import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * REST API for feedback.
 */
@RestController
@RequestMapping(path = "/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    private final Messages messages;

    @Autowired
    FeedbackController(FeedbackService feedbackService, Messages messages) {
        this.feedbackService = feedbackService;
        this.messages = messages;
    }

    /**
     * Creates a new feedback entry.
     *
     * @param feedback The feedback entry to create.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    void createFeedback(HttpServletRequest request, @Valid @RequestBody Feedback feedback) {
        feedback.setIpAddress(request.getRemoteAddr());
        feedbackService.createFeedback(feedback);
    }

    /**
     * Deletes a feedback entry.
     *
     * @param feedbackId The ID of the feedback entry to delete.
     */
    @DeleteMapping("/{feedbackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFeedback(@PathVariable Long feedbackId) {

        if (!feedbackService.getFeedbackExists(feedbackId)) {
            throw new BadRequestException(messages.get("error.noSuchFeedback", feedbackId));
        }

        feedbackService.deleteFeedback(feedbackId);
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
