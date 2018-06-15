package org.sgc.rak.core;

import org.sgc.rak.model.AuditAction;
import org.sgc.rak.services.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * Listens for login events, both successful and unsuccessful.
 */
@Component
public class AppEventListener {

    private final AuditService auditService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppEventListener.class);

    public AppEventListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void handle(AuthenticationSuccessEvent e) {

        User user = (User)e.getAuthentication().getPrincipal();
        String userName = user.getUsername();
        LOGGER.debug("Authentication success for user: {}", userName);

        auditService.createAudit(userName, AuditAction.LOGIN);
    }

    @EventListener
    public void handle(AuthenticationFailureBadCredentialsEvent e) {

        String userName = (String)e.getAuthentication().getPrincipal();
        LOGGER.debug("Authentication failure for user: {}", userName);

        auditService.createAudit(userName, AuditAction.LOGIN, false);
    }
}
