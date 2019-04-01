package org.sgc.rak.services;


import org.sgc.rak.model.Audit;
import org.sgc.rak.model.AuditAction;
import org.sgc.rak.model.ModelConstants;
import org.sgc.rak.repositories.AuditRepository;
import org.sgc.rak.util.QuerySpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Service for manipulating audit records.
 */
@Service
public class AuditService {

    /**
     * The user name stored in the database when the user name could not be determined (or a user was not logged
     * in).
     */
    static final String UNKNOWN_USER_NAME = "<unknown>";

    private final AuditRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Autowired
    public AuditService(AuditRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates an audit record for a successful action.
     *
     * @param userName The user performing the action.  If this is {@code null}, and the current thread is
     *        servicing an HTTP request, the user ID is looked up on the request.
     * @param action The action performed.
     * @return The audit record created.
     * @see #createAudit(String, AuditAction, boolean)
     * @see #createAudit(String, AuditAction, boolean, String)
     */
    public Audit createAudit(String userName, AuditAction action) {
        return createAudit(userName, action, true);
    }

    /**
     * Creates an audit record.
     *
     * @param userName The user performing the action.  If this is {@code null}, and the current thread is
     *        servicing an HTTP request, the user ID is looked up on the request.
     * @param action The action performed.
     * @param success Whether the action was successful.
     * @return The audit record created.
     * @see #createAudit(String, AuditAction)
     * @see #createAudit(String, AuditAction, boolean, String)
     */
    public Audit createAudit(String userName, AuditAction action, boolean success) {
        return createAudit(userName, action, success, null);
    }

    /**
     * Creates an audit record.
     *
     * @param userName The user performing the action.  If this is {@code null}, and the current thread is
     *        servicing an HTTP request, the user ID is looked up on the request.
     * @param action The action performed.
     * @param success Whether the action was successful.
     * @param details Optional details about the action.
     * @return The audit record created.
     * @see #createAudit(String, AuditAction)
     * @see #createAudit(String, AuditAction, boolean, String)
     */
    public Audit createAudit(String userName, AuditAction action, boolean success, String details) {

        Audit audit = new Audit();
        audit.setUserName(ensureLength(userName, ModelConstants.AUDIT_USER_NAME_MAX));
        audit.setAction(action);
        audit.setSuccess(success);
        audit.setDetails(ensureLength(details, ModelConstants.AUDIT_DETAILS_MAX));
        populateAuditFieldsFromRequest(audit);

        if (audit.getUserName() == null) {
            audit.setUserName(UNKNOWN_USER_NAME);
        }

        return repository.save(audit);
    }

    /**
     * Ensures a string is less than or equal to a given length.
     *
     * @param str The string to examine.  May be {@code null}.
     * @param length The maximum length for the string.
     * @return The possibly trimmed string, or {@code null} if the input was {@code null}.
     */
    private static String ensureLength(String str, int length) {
        if (str != null && str.length() > length) {
            str = str.substring(0, length);
        }
        return str;
    }

    /**
     * Returns audit records.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @param user If specified, filter on user names starting with this prefix, ignoring case.  May be {@code null}.
     * @param action If specified, filter on this action type.  May be {@code null}.
     * @param ipAddress If specified, filter on IP address starting with this prefix, ignoring case.  May be
     *                  {@code null}.
     * @param success Whether the action was successful.  May be {@code null} to denote to ignore this property.
     * @param fromDate The starting date from which to fetch audits.  May be {@code null}.
     * @param toDate The ending date to which to fetch audits.  May be {@code null}.
     * @return The list of audit records.
     */
    public Page<Audit> getAudits(Pageable pageInfo, String user, AuditAction action, String ipAddress,
                                 Boolean success, Date fromDate, Date toDate) {
        return repository.findAll(QuerySpecifications.auditRecordsMatching(user, action, ipAddress, success,
            fromDate, toDate), pageInfo);
    }

    /**
     * Populates the user and IP address fields of an audit record if we're on a request-bound thread (which should
     * always be this case for this application).
     *
     * @param audit The audit record to populate.
     */
    private void populateAuditFieldsFromRequest(Audit audit) {

        try {

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

            // User name is sometimes passed in, such as for login failures
            if (audit.getUserName() == null) {
                audit.setUserName(request.getRemoteUser());
            }
            audit.setIpAddress(request.getRemoteAddr());
        } catch (IllegalStateException e) {
            LOGGER.debug("Audit record created outside of request context, will not populate user info: {}", audit);
        }
    }
}
