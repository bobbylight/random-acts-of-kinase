package org.sgc.rak.rest;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.model.Compound;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.CompoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for compound information.
 */
@RestController
@RequestMapping(path = "/api/compounds")
class CompoundController {

    @Autowired
    private CompoundService compoundService;

    /**
     * Returns information on a specific compound.
     *
     * @param compoundName The compound name, ignoring case.
     * @return Information on the compound.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{compoundName}")
    Compound getCompound(@PathVariable String compoundName) {
        return compoundService.getCompound(compoundName);
    }

    /**
     * Returns compound information, possibly filtered.
     *
     * @param inhibitor The start of a compound name.  If specified, only compounds
     *        whose name starts with this prefix (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<Compound> getCompounds(@RequestParam(required = false) String inhibitor,
                                @SortDefault("compoundName") Pageable pageInfo) {

        Page<Compound> page;

        if (StringUtils.isBlank(inhibitor)) {
            page = compoundService.getCompounds(pageInfo);
        }
        else {
            page = compoundService.getCompoundsByCompoundName(inhibitor, pageInfo);
        }

        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/images/{compoundName}",
            produces = "image/svg+xml")
    Resource getCompoundSmiles(@PathVariable String compoundName) {
        Resource resource = new ClassPathResource("/static/img/smiles/" + compoundName + ".svg");
        if (!resource.exists()) {
            resource = new ClassPathResource("/static/img/molecule.svg");
        }
        return resource;
    }
}
