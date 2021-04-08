package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuditActionTest {

    @Test
    public void testFromJsonValue_happyPath() {

        Assertions.assertEquals(AuditAction.ACCESS_DENIED, AuditAction.fromJsonValue("ACCESS_DENIED"));
        Assertions.assertEquals(AuditAction.ACCESS_DENIED, AuditAction.fromJsonValue("access_denied"));
        Assertions.assertEquals(AuditAction.LOGIN, AuditAction.fromJsonValue("LOGIN"));
        Assertions.assertEquals(AuditAction.LOGIN, AuditAction.fromJsonValue("login"));
        Assertions.assertEquals(AuditAction.LOGOUT, AuditAction.fromJsonValue("LOGOUT"));
        Assertions.assertEquals(AuditAction.LOGOUT, AuditAction.fromJsonValue("logout"));
        Assertions.assertEquals(AuditAction.UPDATE_COMPOUND, AuditAction.fromJsonValue("UPDATE_COMPOUND"));
        Assertions.assertEquals(AuditAction.UPDATE_COMPOUND, AuditAction.fromJsonValue("update_compound"));
    }

    @Test
    public void testFromJsonValue_error_unknownValue() {

        Assertions.assertNull(AuditAction.fromJsonValue("unknownValue"));
    }

    @Test
    public void testToJsonValue() {
        Assertions.assertEquals(AuditAction.ACCESS_DENIED.toJsonValue(), "access_denied");
        Assertions.assertEquals(AuditAction.LOGIN.toJsonValue(), "login");
        Assertions.assertEquals(AuditAction.LOGOUT.toJsonValue(), "logout");
        Assertions.assertEquals(AuditAction.ACCESS_DENIED.toJsonValue(), "access_denied");
    }
}
