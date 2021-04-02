package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class FeedbackTest {

    private Feedback feedback;

    @BeforeEach
    public void setUp() {
        feedback = new Feedback();
    }

    @Test
    public void testGetSetId() {
        Assertions.assertNull(feedback.getId());
        feedback.setId(5L);
        Assertions.assertEquals(Long.valueOf(5), feedback.getId());
    }

    @Test
    public void testGetSetEmail() {
        Assertions.assertNull(feedback.getEmail());
        feedback.setEmail("foo");
        Assertions.assertEquals("foo", feedback.getEmail());
    }

    @Test
    public void testGetSetIpAddress() {
        Assertions.assertNull(feedback.getIpAddress());
        feedback.setIpAddress("foo");
        Assertions.assertEquals("foo", feedback.getIpAddress());
    }

    @Test
    public void testGetSetTitle() {
        Assertions.assertNull(feedback.getTitle());
        feedback.setTitle("Title");
        Assertions.assertEquals("Title", feedback.getTitle());
    }

    @Test
    public void testGetSetBody() {
        Assertions.assertNull(feedback.getBody());
        feedback.setBody("Body");
        Assertions.assertEquals("Body", feedback.getBody());
    }

    @Test
    public void testGetSetCreateDate() {
        Assertions.assertNull(feedback.getCreateDate());
        Date now = new Date();
        feedback.setCreateDate(now);
        Assertions.assertEquals(now, feedback.getCreateDate());
    }

    @Test
    public void testToString() {
        String expected =
            "Feedback[id=<null>,email=<null>,ipAddress=<null>,title=<null>,body=<null>,createDate=<null>]";
        Assertions.assertEquals(expected, feedback.toString());
    }
}
