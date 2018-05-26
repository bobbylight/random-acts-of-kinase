package org.sgc.rak.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Feedback;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.FeedbackService;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FeedbackControllerTest {

    @Mock
    private FeedbackService mockService;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private FeedbackController controller;

    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
        mapper = new ObjectMapper();
    }

    @Test
    public void testCreateFeedback() throws Exception {

        Feedback post = TestUtil.createFeedback("title", "body");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/feedback")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(post))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        verify(mockService, times(1)).createFeedback(any(Feedback.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetFeedbacks() throws Exception {

        PageRequest pr = PageRequest.of(0, 20);

        List<Feedback> feedbacks = Collections.singletonList(
            TestUtil.createFeedback("title", "body")
        );
        PageImpl<Feedback> expectedPage = new PageImpl<>(feedbacks, pr, 1);
        doReturn(expectedPage).when(mockService).getFeedback(any(Pageable.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/feedback")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        PagedDataRep<Feedback> actualFeedbacks = mapper.readValue(result.getResponse().getContentAsString(),
            PagedDataRep.class);
        // jackson converts collections of objects to Collection<LinkedHashMap>, so we must manually deserialize
        // the list that's one-level deep
        actualFeedbacks.setData(mapper.convertValue(actualFeedbacks.getData(), new TypeReference<List<Feedback>>() {}));
        Assert.assertEquals(0, actualFeedbacks.getStart());
        Assert.assertEquals(1, actualFeedbacks.getTotal());
        Assert.assertEquals(1, actualFeedbacks.getCount());
        for (int i = 0; i < feedbacks.size(); i++) {
            TestUtil.assertFeedbacksEqual(feedbacks.get(i), actualFeedbacks.getData().get(i));
        }
    }
}
