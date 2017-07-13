package org.sgc.rak.rest;

import org.sgc.rak.model.Kinase;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.KinaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for kinase information.
 */
@RestController
@RequestMapping(path = "/api/kinases")
class KinaseController {

    @Autowired
    private KinaseService kinaseService;

    /**
     * Returns kinase information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<Kinase> getKinases(
        @SortDefault.SortDefaults({ @SortDefault("discoverxGeneSymbol"), @SortDefault("entrezGeneSymbol") })
        Pageable pageInfo) {

        Page<Kinase> page = kinaseService.getKinases(pageInfo);

        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}
