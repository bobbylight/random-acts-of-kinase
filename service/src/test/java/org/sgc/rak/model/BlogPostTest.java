package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class BlogPostTest {

    private BlogPost blogPost;

    @BeforeEach
    public void setUp() {
        blogPost = new BlogPost();
    }

    @Test
    public void testOnCreate() {
        Assertions.assertNull(blogPost.getViewCount());
        blogPost.onCreate();
        Assertions.assertEquals(0L, blogPost.getViewCount().longValue());
    }

    @Test
    public void testGetSetId() {
        Assertions.assertNull(blogPost.getId());
        blogPost.setId(5L);
        Assertions.assertEquals(Long.valueOf(5), blogPost.getId());
    }

    @Test
    public void testGetSetTitle() {
        Assertions.assertNull(blogPost.getTitle());
        blogPost.setTitle("Title");
        Assertions.assertEquals("Title", blogPost.getTitle());
    }

    @Test
    public void testGetSetBody() {
        Assertions.assertNull(blogPost.getBody());
        blogPost.setBody("Body");
        Assertions.assertEquals("Body", blogPost.getBody());
    }

    @Test
    public void testGetSetCreateDate() {
        Assertions.assertNull(blogPost.getCreateDate());
        Date now = new Date();
        blogPost.setCreateDate(now);
        Assertions.assertEquals(now, blogPost.getCreateDate());
    }

    @Test
    public void testGetSetViewCount() {
        Assertions.assertNull(blogPost.getViewCount());
        blogPost.setViewCount(42L);
        Assertions.assertEquals(42L, blogPost.getViewCount().longValue());
    }

    @Test
    public void testToString() {
        String expected = "BlogPost[id=<null>,title=<null>,body=<null>,createDate=<null>,viewCount=<null>]";
        Assertions.assertEquals(expected, blogPost.toString());
    }
}
