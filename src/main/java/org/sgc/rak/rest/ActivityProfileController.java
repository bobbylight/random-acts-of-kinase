package org.sgc.rak.rest;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.KinaseActivityProfile;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.ActivityProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for kinase activity profile information.
 */
@RestController
@RequestMapping(path = "/api/activityProfiles")
class ActivityProfileController {

    @Autowired
    private ActivityProfileService activityProfileService;

    @Autowired
    private Messages messages;

    /**
     * Returns kinase activity profile information, possibly filtered.
     *
     * @param inhibitor A compound name.  If specified, only activity profiles about
     *        this compound/inhibitor will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<KinaseActivityProfile> getKinaseActivityProfiles(@RequestParam(required = false) String inhibitor,
                  @RequestParam(required = false) String kinase,
                  @RequestParam(required = false) Double activity,
                  @SortDefault.SortDefaults({ @SortDefault("kd"), @SortDefault("percentControl") })
                  Pageable pageInfo) {

        // Kinase and activity are a pair; you can't filter with just one
        if (StringUtils.isEmpty(kinase) && activity != null) {
            throw new BadRequestException(messages.get("error.missingDependentSearchParam", "activity", "kinase"));
        }
        if (StringUtils.isNotEmpty(kinase) && activity == null) {
            throw new BadRequestException(messages.get("error.missingDependentSearchParam", "kinase", "activity"));
        }

        // Activity must be between 0 and 1
        if (activity != null && (activity > 1 || activity <= 0)) {
            throw new BadRequestException(messages.get("error.actvityOutOfRange"));
        }

        Page<KinaseActivityProfile> page;

        if (StringUtils.isEmpty(inhibitor)) {
            if (StringUtils.isEmpty(kinase)) {
                page = activityProfileService.getKinaseActivityProfiles(pageInfo);
            }
            else {
                page = activityProfileService.getKinaseActivityProfilesForKinaseAndPercentControl(kinase, activity, pageInfo);
            }
        }
        else {
            if (StringUtils.isEmpty(kinase)) {
                page = activityProfileService.getKinaseActivityProfilesForCompound(inhibitor, pageInfo);
            }
            else {
                page = activityProfileService.getKinaseActivityProfilesForCompoundAndKinaseAndPercentControl(inhibitor,
                    kinase, activity, pageInfo);
            }
        }

        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}
