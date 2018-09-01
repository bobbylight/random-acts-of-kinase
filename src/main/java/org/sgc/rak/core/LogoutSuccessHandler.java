package org.sgc.rak.core;

import org.sgc.rak.model.AuditAction;
import org.sgc.rak.services.AuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Captures logout events.  Spring doesn't emit a logout-based {@code ApplicationEvent} so we must unfortunately
 * have this handler specifically to audit logouts.
 *
 * @see AppEventListener
 */
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final AuditService auditService;

    LogoutSuccessHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        // If someone calls /logout but they're not logged in (e.g. a timeout but the UI didn't refresh
        // for some reason, then the user manually logs out), authentication will be null.
        if (authentication != null) {
            auditService.createAudit(authentication.getName(), AuditAction.LOGOUT);
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
