package org.sgc.rak.model;

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
}
