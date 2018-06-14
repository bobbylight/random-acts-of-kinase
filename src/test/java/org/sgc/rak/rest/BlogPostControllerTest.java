package org.sgc.rak.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.BlogPost;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.BlogPostService;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BlogPostControllerTest {

    @Mock
    private BlogPostService mockService;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private BlogPostController controller;

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

    @After
    public void tearDown() {
        // It seems MockMvcBuilders.standaloneSetup() populates RequestContextHolder, which breaks other test classes
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testCreateBlogPost() throws Exception {

        BlogPost post = TestUtil.createBlogPost("title", "body");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/blogPosts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(post))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        verify(mockService, times(1)).createBlogPost(any(BlogPost.class));
    }

    @Test
    public void testDeleteBlogPost() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/blogPosts/42")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetBlogPosts() throws Exception {

        PageRequest pr = PageRequest.of(0, 20);

        List<BlogPost> posts = Collections.singletonList(TestUtil.createBlogPost("title", "body"));
        PageImpl<BlogPost> expectedPage = new PageImpl<>(posts, pr, 1);
        doReturn(expectedPage).when(mockService).getBlogPosts(any(Pageable.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/blogPosts")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        PagedDataRep<BlogPost> actualPosts = mapper.readValue(result.getResponse().getContentAsString(),
            PagedDataRep.class);
        // jackson converts collections of objects to Collection<LinkedHashMap>, so we must manually deserialize
        // the list that's one-level deep
        actualPosts.setData(mapper.convertValue(actualPosts.getData(), new TypeReference<List<BlogPost>>() {}));
        Assert.assertEquals(0, actualPosts.getStart());
        Assert.assertEquals(1, actualPosts.getTotal());
        Assert.assertEquals(1, actualPosts.getCount());
        for (int i = 0; i < posts.size(); i++) {
            TestUtil.assertBlogPostsEqual(posts.get(i), actualPosts.getData().get(i));
        }
    }

    @Test
    public void testUpdateBlogPost_happyPath() throws Exception {

        BlogPost post = TestUtil.createBlogPost("title", "body");
        post.setId(42L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/blogPosts/" + post.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(post))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockService, times(1)).updateBlogPost(any(BlogPost.class));
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateBlogPost_error_nonNumericBlogPostId() throws Exception {

        BlogPost post = TestUtil.createBlogPost("title", "body");

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/api/blogPosts/notANumber")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(post))
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateBlogPost_error_postIdsDontMatch() throws Exception {

        BlogPost post = TestUtil.createBlogPost("title", "body");
        post.setId(42L);

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/api/blogPosts/43")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(post))
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }
}
