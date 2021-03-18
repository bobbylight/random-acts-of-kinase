package org.sgc.rak.rest;

import org.sgc.rak.model.AuditAction;
import org.sgc.rak.services.AuditService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * A base class for controller unit tests.
 */
abstract class AbstractControllerTest {

    /**
     * Asserts that an audit record was created.
     *
     * @param mockAuditService The mocked audit service.
     * @param user The user performing the action.
     * @param action The action performed.
     * @param success Whether the action was successful.
     */
    static void assertAuditRecorded(AuditService mockAuditService, String user, AuditAction action, boolean success) {
        verify(mockAuditService, times(1)).createAudit(eq(user), eq(action), eq(success));
    }
}
