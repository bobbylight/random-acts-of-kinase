package org.sgc.rak.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.BlogPost;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.BlogPostService;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class BlogPostControllerTest {

    @Mock
    private BlogPostService mockService;

    @Mock
    private Messages mockMessages;

    private BlogPostController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new BlogPostController(mockService, mockMessages);
    }

    @Test
    public void testCreateBlogPost() {

        BlogPost post = new BlogPost();
        post.setTitle("title");
        post.setBody("body");

        controller.createBlogPost(post);
    }

    @Test
    public void testDeleteBlogPost() {

        controller.deleteBlogPost(42L);
    }

    @Test
    public void testGetBlogPosts() {

        PageRequest pr = PageRequest.of(0, 20);

        List<BlogPost> posts = Collections.singletonList(TestUtil.createBlogPost("title", "body"));
        PageImpl<BlogPost> expectedPage = new PageImpl<>(posts, pr, 1);
        doReturn(expectedPage).when(mockService).getBlogPosts(any(Pageable.class));

        PagedDataRep<BlogPost> actualPosts = controller.getBlogPosts(pr);
        Assert.assertEquals(0, actualPosts.getStart());
        Assert.assertEquals(1, actualPosts.getTotal());
        Assert.assertEquals(1, actualPosts.getCount());
        for (int i = 0; i < posts.size(); i++) {
            TestUtil.assertBlogPostsEqual(posts.get(i), actualPosts.getData().get(i));
        }
    }

    @Test
    @Ignore("Not yet implemented")
    public void testUpdateBlogPost() {
    }
}
