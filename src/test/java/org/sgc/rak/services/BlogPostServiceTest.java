package org.sgc.rak.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.BlogPost;
import org.sgc.rak.repositories.BlogPostRepository;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BlogPostServiceTest {

    @Mock
    private BlogPostRepository mockRepository;

    private BlogPostService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new BlogPostService(mockRepository);
    }

    @Test
    public void testCreateBlogPost() {

        BlogPost post = new BlogPost();
        post.setTitle("title");
        post.setBody("body");

        service.createBlogPost(post);

        verify(mockRepository, times(1)).save(any());
    }

    @Test
    public void testGetBlogPosts() {

        PageRequest pr = PageRequest.of(0, 20);

        List<BlogPost> posts = Collections.singletonList(TestUtil.createBlogPost("title", "body"));
        PageImpl<BlogPost> expectedPage = new PageImpl<>(posts, pr, 1);
        doReturn(expectedPage).when(mockRepository).findAllByOrderByCreateDateDesc(any(Pageable.class));

        Page<BlogPost> actualPosts = service.getBlogPosts(pr);
        Assert.assertEquals(1, actualPosts.getNumberOfElements());
        Assert.assertEquals(1, actualPosts.getTotalElements());
        Assert.assertEquals(1, actualPosts.getTotalPages());
        for (int i = 0; i < posts.size(); i++) {
            TestUtil.assertBlogPostsEqual(posts.get(i), actualPosts.getContent().get(i));
        }
    }
}
