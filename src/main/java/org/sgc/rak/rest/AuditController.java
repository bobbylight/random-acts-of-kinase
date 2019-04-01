package org.sgc.rak.rest;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Audit;
import org.sgc.rak.model.AuditAction;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * REST API for audit records.
 */
@RestController
@RequestMapping(path = "/admin/api/audits")
public class AuditController {

    private final AuditService auditService;
    private final Messages messages;

    @Autowired
    public AuditController(AuditService auditService, Messages messages) {
        this.auditService = auditService;
        this.messages = messages;
    }

    /**
     * Returns audit records.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of audit records.
     */
    @GetMapping
    PagedDataRep<Audit> getAuditRecords(@RequestParam(required = false) String user,
                            @RequestParam(required = false) AuditAction action,
                            @RequestParam(required = false) String ipAddress,
                            @RequestParam(required = false) Boolean success,
                            @RequestParam(required = false) String fromDate,
                            @RequestParam(required = false) String toDate,
                            @SortDefault(value = "createDate", direction = Sort.Direction.DESC) Pageable pageInfo) {

        Date from = getDate("fromDate", fromDate, false);
        Date to = getDate("toDate", toDate, true);

        Page<Audit> page = auditService.getAudits(pageInfo, user, action, ipAddress, success, from, to);
        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }

    private Date getDate(String argName, String dateStr, boolean endOfDay) {

        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        String pattern = "yyyy-MM-dd";
        if (endOfDay) {
            pattern += "'T'HH:mm:ss'Z'";
            dateStr += "T23:59:59Z";
        }

        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException pe) {
            throw new BadRequestException(messages.get("error.invalidDate", argName, dateStr));
        }
    }
}
