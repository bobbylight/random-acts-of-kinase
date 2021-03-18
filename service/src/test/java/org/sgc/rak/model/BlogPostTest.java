package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class BlogPostTest {

    private BlogPost blogPost;

    @Before
    public void setUp() {
        blogPost = new BlogPost();
    }

    @Test
    public void testOnCreate() {
        Assert.assertNull(blogPost.getViewCount());
        blogPost.onCreate();
        Assert.assertEquals(0L, blogPost.getViewCount().longValue());
    }

    @Test
    public void testGetSetId() {
        Assert.assertNull(blogPost.getId());
        blogPost.setId(5L);
        Assert.assertEquals(Long.valueOf(5), blogPost.getId());
    }

    @Test
    public void testGetSetTitle() {
        Assert.assertNull(blogPost.getTitle());
        blogPost.setTitle("Title");
        Assert.assertEquals("Title", blogPost.getTitle());
    }

    @Test
    public void testGetSetBody() {
        Assert.assertNull(blogPost.getBody());
        blogPost.setBody("Body");
        Assert.assertEquals("Body", blogPost.getBody());
    }

    @Test
    public void testGetSetCreateDate() {
        Assert.assertNull(blogPost.getCreateDate());
        Date now = new Date();
        blogPost.setCreateDate(now);
        Assert.assertEquals(now, blogPost.getCreateDate());
    }

    @Test
    public void testGetSetViewCount() {
        Assert.assertNull(blogPost.getViewCount());
        blogPost.setViewCount(42L);
        Assert.assertEquals(42L, blogPost.getViewCount().longValue());
    }

    @Test
    public void testToString() {
        String expected = "BlogPost[id=<null>,title=<null>,body=<null>,createDate=<null>,viewCount=<null>]";
        Assert.assertEquals(expected, blogPost.toString());
    }
}
