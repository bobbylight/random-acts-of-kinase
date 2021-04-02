package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class AuditTest {

    private Audit audit;

    @BeforeEach
    public void setUp() {
        audit = new Audit();
    }

    @Test
    public void testOnCreate() {
        Assertions.assertNull(audit.getCreateDate());
        audit.onCreate();
        Assertions.assertNotNull(audit.getCreateDate());
    }

    @Test
    public void testGetSetId() {
        Assertions.assertNull(audit.getId());
        audit.setId(5L);
        Assertions.assertEquals(Long.valueOf(5), audit.getId());
    }

    @Test
    public void testGetSetUserName() {
        Assertions.assertNull(audit.getUserName());
        audit.setUserName("foo");
        Assertions.assertEquals("foo", audit.getUserName());
    }

    @Test
    public void testGetSetAction() {
        Assertions.assertNull(audit.getAction());
        audit.setAction(AuditAction.LOGIN);
        Assertions.assertEquals(AuditAction.LOGIN, audit.getAction());
    }

    @Test
    public void testGetSetIpAddress() {
        Assertions.assertNull(audit.getIpAddress());
        audit.setIpAddress("foo");
        Assertions.assertEquals("foo", audit.getIpAddress());
    }

    @Test
    public void testGetSetCreateDate() {
        Assertions.assertNull(audit.getCreateDate());
        Date now = new Date();
        audit.setCreateDate(now);
        Assertions.assertEquals(now, audit.getCreateDate());
    }

    @Test
    public void testGetSetSuccess() {
        Assertions.assertNull(audit.getSuccess());
        audit.setSuccess(true);
        Assertions.assertEquals(true, audit.getSuccess());
    }

    @Test
    public void testGetSetDetails() {
        Assertions.assertNull(audit.getDetails());
        audit.setDetails("foo");
        Assertions.assertEquals("foo", audit.getDetails());
    }

    @Test
    public void testToString() {
        String expected = "Audit[id=<null>,userName=<null>,action=<null>,ipAddress=<null>,createDate=<null>," +
            "success=<null>,details=<null>]";
        Assertions.assertEquals(expected, audit.toString());
    }
}
