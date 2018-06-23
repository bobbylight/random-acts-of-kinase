package org.sgc.rak.rest;

import org.sgc.rak.model.Audit;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for audit records.
 */
@RestController
@RequestMapping(path = "/admin/api/audits")
public class AuditController {

    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Returns audit records.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of audit records.
     */
    @GetMapping
    PagedDataRep<Audit> getAuditRecords(@SortDefault(value = "createDate",
            direction = Sort.Direction.DESC) Pageable pageInfo) {
        Page<Audit> page = auditService.getAudits(pageInfo);
        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}
