package org.sgc.rak.rest;

import org.sgc.rak.model.Compound;
import org.sgc.rak.model.CompoundCountPair;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.CompoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Returns random tidbits about the data.  Accessible only by admins.
 */
@RestController
@RequestMapping(path = "/admin/api")
public class StatController {

    @Autowired
    private CompoundService compoundService;

    /**
     * Returns information about compounds that are missing activity profiles.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/compoundsMissingActivityProfiles",
        produces = { MediaType.APPLICATION_JSON_VALUE, "text/csv" })
    PagedDataRep<CompoundCountPair> getCompoundsMissingActivityProfiles(
            @SortDefault("compoundName") Pageable pageInfo) {
        Page<CompoundCountPair> page = compoundService.getCompoundsMissingActivityProfiles(pageInfo);
        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }

    /**
     * Returns information about compounds without SMILES strings, or any other missing fields, possibly filtered.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/incompleteCompounds",
            produces = { MediaType.APPLICATION_JSON_VALUE, "text/csv" })
    PagedDataRep<Compound> getIncompleteCompounds(@SortDefault("compoundName") Pageable pageInfo) {
        Page<Compound> page = compoundService.getIncompleteCompounds(pageInfo);
        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}