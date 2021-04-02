package org.sgc.rak.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Feedback;
import org.sgc.rak.repositories.FeedbackRepository;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository mockRepository;

    @InjectMocks
    private FeedbackService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFeedback() {

        Feedback feedback = TestUtil.createFeedback("title", "body");
        service.createFeedback(feedback);

        verify(mockRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteFeedback() {
        service.deleteFeedback(42L);
        verify(mockRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testGetFeedback() {

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Feedback> posts = Collections.singletonList(TestUtil.createFeedback("title", "body"));
        PageImpl<Feedback> expectedPage = new PageImpl<>(posts, pr, 1);
        doReturn(expectedPage).when(mockRepository).findAll(any(Pageable.class));

        Page<Feedback> actualPosts = service.getFeedback(pr);
        Assertions.assertEquals(1, actualPosts.getNumberOfElements());
        Assertions.assertEquals(1, actualPosts.getTotalElements());
        Assertions.assertEquals(1, actualPosts.getTotalPages());
        for (int i = 0; i < posts.size(); i++) {
            TestUtil.assertFeedbacksEqual(posts.get(i), actualPosts.getContent().get(i));
        }
    }

    @Test
    public void testGetFeedbackExists() {
        service.getFeedbackExists(42L);
        verify(mockRepository, times(1)).existsById(anyLong());
    }
}
