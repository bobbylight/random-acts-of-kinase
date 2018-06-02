package org.sgc.rak.rest;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
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
@RequestMapping("/api/compounds")
class CompoundController {

    private final CompoundService compoundService;
    private final Messages messages;

    @Autowired
    CompoundController(CompoundService compoundService, Messages messages) {
        this.compoundService = compoundService;
        this.messages = messages;
    }

    /**
     * Returns information on a specific compound.
     *
     * @param compoundName The compound name, ignoring case.
     * @return Information on the compound.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{compoundName}")
    Compound getCompound(@PathVariable String compoundName) {

        Compound compound = compoundService.getCompound(compoundName);
        if (compound == null) {
            throw new NotFoundException(messages.get("error.noSuchCompound", compoundName));
        }
        return compound;
    }

    /**
     * Returns compound information, possibly filtered.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<Compound> getCompounds(@RequestParam(required = false) String compound,
                                @RequestParam(required = false) String kinase,
                                @RequestParam(required = false) Double activity,
                                @RequestParam(required = false) Double kd,
                                @SortDefault("compoundName") Pageable pageInfo) {

        Page<Compound> page;

        if (StringUtils.isNotBlank(compound)) {
            page = compoundService.getCompoundsByCompoundName(compound, pageInfo);
        }
        else if (StringUtils.isNotBlank(kinase) && activity != null) {
            page = compoundService.getCompoundsByKinaseAndActivity(kinase, activity, pageInfo);
        }
        else if (StringUtils.isNotBlank(kinase) && kd != null) {
            page = compoundService.getCompoundsByKinaseAndKd(kinase, kd, pageInfo);
        }
        else {
            page = compoundService.getCompounds(pageInfo);
        }

        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }

    /**
     * Returns the image for a compound.
     *
     * @param compoundName A compound name.
     * @return The image for the compound, in SVG format.  If no image exists for a compound, a default
     *         image is returned.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/images/{compoundName}", produces = "image/svg+xml")
    Resource getCompoundSmiles(@PathVariable String compoundName) {
        Resource resource = new ClassPathResource("/static/img/smiles/" + compoundName + ".svg");
        if (!resource.exists()) {
            resource = new ClassPathResource("/static/img/molecule-unknown.svg");
        }
        return resource;
    }
}
