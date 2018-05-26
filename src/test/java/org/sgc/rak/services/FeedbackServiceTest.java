package org.sgc.rak.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateFeedback() {

        Feedback feedback = TestUtil.createFeedback("title", "body");
        service.createFeedback(feedback);

        verify(mockRepository, times(1)).save(any());
    }

    @Test
    public void testGetFeedback() {

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Feedback> posts = Collections.singletonList(TestUtil.createFeedback("title", "body"));
        PageImpl<Feedback> expectedPage = new PageImpl<>(posts, pr, 1);
        doReturn(expectedPage).when(mockRepository).findAll(any(Pageable.class));

        Page<Feedback> actualPosts = service.getFeedback(pr);
        Assert.assertEquals(1, actualPosts.getNumberOfElements());
        Assert.assertEquals(1, actualPosts.getTotalElements());
        Assert.assertEquals(1, actualPosts.getTotalPages());
        for (int i = 0; i < posts.size(); i++) {
            TestUtil.assertFeedbacksEqual(posts.get(i), actualPosts.getContent().get(i));
        }
    }
}
