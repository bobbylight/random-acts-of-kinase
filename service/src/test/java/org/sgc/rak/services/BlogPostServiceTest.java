package org.sgc.rak.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.BlogPost;
import org.sgc.rak.repositories.BlogPostRepository;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BlogPostServiceTest {

    @Mock
    private BlogPostRepository mockRepository;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private BlogPostService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBlogPost() {

        BlogPost post = TestUtil.createBlogPost("title", "body");
        service.createBlogPost(post);

        verify(mockRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteBlogPost() {

        service.deleteBlogPost(42L);

        verify(mockRepository, times(1)).deleteById(eq(42L));
    }

    @Test
    public void testGetBlogPosts() {

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<BlogPost> posts = Collections.singletonList(TestUtil.createBlogPost("title", "body"));
        PageImpl<BlogPost> expectedPage = new PageImpl<>(posts, pr, 1);
        doReturn(expectedPage).when(mockRepository).findAll(any(Pageable.class));

        Page<BlogPost> actualPosts = service.getBlogPosts(pr);
        Assertions.assertEquals(1, actualPosts.getNumberOfElements());
        Assertions.assertEquals(1, actualPosts.getTotalElements());
        Assertions.assertEquals(1, actualPosts.getTotalPages());
        for (int i = 0; i < posts.size(); i++) {
            TestUtil.assertBlogPostsEqual(posts.get(i), actualPosts.getContent().get(i));
        }
    }

    @Test
    public void testUpdateBlogPost_happyPath() {

        BlogPost post = TestUtil.createBlogPost("new title", "new body");
        post.setId(42L);

        BlogPost origPost = TestUtil.createBlogPost("old title", "old body");
        origPost.setId(42L);
        origPost.setViewCount(3L);

        doReturn(Optional.of(origPost)).when(mockRepository).findById(anyLong());
        doReturn(origPost).when(mockRepository).save(any(BlogPost.class));

        BlogPost actual = service.updateBlogPost(post);
        Assertions.assertEquals(42, actual.getId().longValue());
        Assertions.assertEquals("new title", actual.getTitle());
        Assertions.assertEquals("new body", actual.getBody());
        Assertions.assertEquals(4, actual.getViewCount().longValue());
    }

    @Test
    public void testUpdateBlogPost_error_blogPostNotFound() {

        BlogPost post = TestUtil.createBlogPost("title", "body");
        post.setId(42L);

        doReturn(Optional.empty()).when(mockRepository).findById(anyLong());

        Assertions.assertThrows(NotFoundException.class, () -> service.updateBlogPost(post));
    }
}
