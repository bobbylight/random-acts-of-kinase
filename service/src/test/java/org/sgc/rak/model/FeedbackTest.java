package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class FeedbackTest {

    private Feedback feedback;

    @Before
    public void setUp() {
        feedback = new Feedback();
    }

    @Test
    public void testGetSetId() {
        Assert.assertNull(feedback.getId());
        feedback.setId(5L);
        Assert.assertEquals(Long.valueOf(5), feedback.getId());
    }

    @Test
    public void testGetSetEmail() {
        Assert.assertNull(feedback.getEmail());
        feedback.setEmail("foo");
        Assert.assertEquals("foo", feedback.getEmail());
    }

    @Test
    public void testGetSetIpAddress() {
        Assert.assertNull(feedback.getIpAddress());
        feedback.setIpAddress("foo");
        Assert.assertEquals("foo", feedback.getIpAddress());
    }

    @Test
    public void testGetSetTitle() {
        Assert.assertNull(feedback.getTitle());
        feedback.setTitle("Title");
        Assert.assertEquals("Title", feedback.getTitle());
    }

    @Test
    public void testGetSetBody() {
        Assert.assertNull(feedback.getBody());
        feedback.setBody("Body");
        Assert.assertEquals("Body", feedback.getBody());
    }

    @Test
    public void testGetSetCreateDate() {
        Assert.assertNull(feedback.getCreateDate());
        Date now = new Date();
        feedback.setCreateDate(now);
        Assert.assertEquals(now, feedback.getCreateDate());
    }

    @Test
    public void testToString() {
        String expected =
            "Feedback[id=<null>,email=<null>,ipAddress=<null>,title=<null>,body=<null>,createDate=<null>]";
        Assert.assertEquals(expected, feedback.toString());
    }
}
