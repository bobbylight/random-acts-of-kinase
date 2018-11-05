package org.sgc.rak.rest;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.ActivityProfileService;
import org.sgc.rak.services.KinaseService;
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

    private final ActivityProfileService activityProfileService;
    private final KinaseService kinaseService;
    private final Messages messages;

    @Autowired
    ActivityProfileController(ActivityProfileService activityProfileService, KinaseService kinaseService,
                              Messages messages) {
        this.activityProfileService = activityProfileService;
        this.kinaseService = kinaseService;
        this.messages = messages;
    }

    /**
     * Returns kinase activity profile information, possibly filtered.
     *
     * @param compound A compound name.  If specified, only activity profiles about
     *        this compound/inhibitor will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<ActivityProfile> getActivityProfiles(@RequestParam(required = false) String compound,
                                    @RequestParam(required = false) String kinaseDiscoverx,
                                    @RequestParam(required = false) Double activity,
                                    @SortDefault.SortDefaults({ @SortDefault("kd"), @SortDefault("percentControl") })
                                    Pageable pageInfo) {

        // Activity must be between 0 and 1
        if (activity != null && (activity > 100 || activity < 0)) {
            throw new BadRequestException(messages.get("error.actvityOutOfRange"));
        }

        Long kinaseId = null;
        if (StringUtils.isNotEmpty(kinaseDiscoverx)) {
            Kinase kinase = kinaseService.getKinase(kinaseDiscoverx);
            if (kinase == null) {
                throw new BadRequestException(messages.get("error.noSuchKinase", kinaseDiscoverx));
            }
            kinaseId = kinase.getId();
        }

        Page<ActivityProfile> page = activityProfileService.getActivityProfiles(compound, kinaseId, activity, pageInfo);

        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}
