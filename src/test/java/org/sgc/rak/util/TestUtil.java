package org.sgc.rak.util;

import org.junit.Assert;
import org.sgc.rak.model.BlogPost;

/**
 * Utility methods for unit tests.
 */
public final class TestUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private TestUtil() {
    }

    public static void assertBlogPostsEqual(BlogPost expected, BlogPost actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getBody(), actual.getBody());
        Assert.assertEquals(expected.getCreateDate(), actual.getCreateDate());
    }

    public static BlogPost createBlogPost(String title, String body) {
        BlogPost post = new BlogPost();
        post.setTitle(title);
        post.setBody(body);
        return post;
    }
}
