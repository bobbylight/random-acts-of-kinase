package org.sgc.rak.core;

import org.sgc.rak.model.AuditAction;
import org.sgc.rak.services.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom handler for 403s.  Allows us to audit them.
 */
public class RakAccessDeniedHandler extends AccessDeniedHandlerImpl {

    private final AuditService auditService;

    public static final Logger LOGGER = LoggerFactory.getLogger(RakAccessDeniedHandler.class);

    public RakAccessDeniedHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                        AccessDeniedException exception) throws IOException, ServletException {

        String user = request.getRemoteUser();
        String details = "Access denied (403) accessing URL: " + request.getMethod() + " " + request.getRequestURL();
        if (request.getQueryString() != null) {
            details += '?' + request.getQueryString();
        }

        LOGGER.warn(details);
        auditService.createAudit(user, AuditAction.ACCESS_DENIED, false, details);

        super.handle(request, response, exception);
    }
}
