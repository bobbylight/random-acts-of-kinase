package org.sgc.rak.rest;

import org.sgc.rak.model.Partner;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for partners.
 */
@RestController
@RequestMapping(path = "/api/partners")
public class PartnerController {

    private final PartnerService partnerService;

    @Autowired
    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    /**
     * Returns partners.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of partners.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<Partner> getPartners(@SortDefault(value = "name",
            direction = Sort.Direction.ASC) Pageable pageInfo) {
        Page<Partner> page = partnerService.getPartners(pageInfo);
        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}
