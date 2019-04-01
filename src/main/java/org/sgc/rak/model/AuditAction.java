package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * An enumeration of actions that are audited in the application.
 */
public enum AuditAction {

    /**
     * The user tried to access a resource but Spring Security returned a 403 before
     * our controller endpoint was called.
     */
    ACCESS_DENIED,

    LOGIN,

    LOGOUT,

    UPDATE_COMPOUND;

    @JsonCreator
    public static AuditAction fromJsonValue(String value) {

        for (AuditAction action : AuditAction.values()) {
            if (action.name().equalsIgnoreCase(value)) {
                return action;
            }
        }

        return null;
    }

    @JsonValue
    public String toJsonValue() {
        return name().toLowerCase(Locale.US);
    }
}
