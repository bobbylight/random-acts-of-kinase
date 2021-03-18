package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class AuditTest {

    private Audit audit;

    @Before
    public void setUp() {
        audit = new Audit();
    }

    @Test
    public void testOnCreate() {
        Assert.assertNull(audit.getCreateDate());
        audit.onCreate();
        Assert.assertNotNull(audit.getCreateDate());
    }

    @Test
    public void testGetSetId() {
        Assert.assertNull(audit.getId());
        audit.setId(5L);
        Assert.assertEquals(Long.valueOf(5), audit.getId());
    }

    @Test
    public void testGetSetUserName() {
        Assert.assertNull(audit.getUserName());
        audit.setUserName("foo");
        Assert.assertEquals("foo", audit.getUserName());
    }

    @Test
    public void testGetSetAction() {
        Assert.assertNull(audit.getAction());
        audit.setAction(AuditAction.LOGIN);
        Assert.assertEquals(AuditAction.LOGIN, audit.getAction());
    }

    @Test
    public void testGetSetIpAddress() {
        Assert.assertNull(audit.getIpAddress());
        audit.setIpAddress("foo");
        Assert.assertEquals("foo", audit.getIpAddress());
    }

    @Test
    public void testGetSetCreateDate() {
        Assert.assertNull(audit.getCreateDate());
        Date now = new Date();
        audit.setCreateDate(now);
        Assert.assertEquals(now, audit.getCreateDate());
    }

    @Test
    public void testGetSetSuccess() {
        Assert.assertNull(audit.getSuccess());
        audit.setSuccess(true);
        Assert.assertEquals(true, audit.getSuccess());
    }

    @Test
    public void testGetSetDetails() {
        Assert.assertNull(audit.getDetails());
        audit.setDetails("foo");
        Assert.assertEquals("foo", audit.getDetails());
    }

    @Test
    public void testToString() {
        String expected = "Audit[id=<null>,userName=<null>,action=<null>,ipAddress=<null>,createDate=<null>," +
            "success=<null>,details=<null>]";
        Assert.assertEquals(expected, audit.toString());
    }
}
