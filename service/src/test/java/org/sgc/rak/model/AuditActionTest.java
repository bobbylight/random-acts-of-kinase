package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Test;

public class AuditActionTest {

    @Test
    public void testFromJsonValue_happyPath() {

        Assert.assertEquals(AuditAction.ACCESS_DENIED, AuditAction.fromJsonValue("ACCESS_DENIED"));
        Assert.assertEquals(AuditAction.ACCESS_DENIED, AuditAction.fromJsonValue("access_denied"));
        Assert.assertEquals(AuditAction.LOGIN, AuditAction.fromJsonValue("LOGIN"));
        Assert.assertEquals(AuditAction.LOGIN, AuditAction.fromJsonValue("login"));
        Assert.assertEquals(AuditAction.LOGOUT, AuditAction.fromJsonValue("LOGOUT"));
        Assert.assertEquals(AuditAction.LOGOUT, AuditAction.fromJsonValue("logout"));
        Assert.assertEquals(AuditAction.UPDATE_COMPOUND, AuditAction.fromJsonValue("UPDATE_COMPOUND"));
        Assert.assertEquals(AuditAction.UPDATE_COMPOUND, AuditAction.fromJsonValue("update_compound"));
    }

    @Test
    public void testFromJsonValue_error_unknownValue() {

        Assert.assertNull(AuditAction.fromJsonValue("unknownValue"));
    }

    @Test
    public void testToJsonValue() {
        Assert.assertEquals(AuditAction.ACCESS_DENIED.toJsonValue(), "access_denied");
        Assert.assertEquals(AuditAction.LOGIN.toJsonValue(), "login");
        Assert.assertEquals(AuditAction.LOGOUT.toJsonValue(), "logout");
        Assert.assertEquals(AuditAction.ACCESS_DENIED.toJsonValue(), "access_denied");
    }
}
