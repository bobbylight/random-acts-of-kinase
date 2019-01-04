package org.sgc.rak.rest;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.NanoBretActivityProfile;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.KinaseService;
import org.sgc.rak.services.NanoBretActivityProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API for NanoBRET activity profile information.
 */
@RestController
@RequestMapping(path = "/api/nanoBretActivityProfiles")
class NanoBretActivityProfileController {

    private final NanoBretActivityProfileService activityProfileService;
    private final KinaseService kinaseService;
    private final Messages messages;

    @Autowired
    NanoBretActivityProfileController(NanoBretActivityProfileService activityProfileService,
                                      KinaseService kinaseService, Messages messages) {
        this.activityProfileService = activityProfileService;
        this.kinaseService = kinaseService;
        this.messages = messages;
    }

    /**
     * Returns NanoBRET activity profile information, possibly filtered.
     *
     * @param compound A compound name.  If specified, only activity profiles about
     *        this compound/inhibitor will be returned.
     * @param kinaseEntrez The kinase involved in the activity profile.  This may be {@code null} to not limit
     *        the search to one particular kinase.
     * @param ic50 The value that the ict50 must be less than or equal to. This may be {@code null} to not restrict
     *             by ic50.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of NanoBRET activity profiles.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<NanoBretActivityProfile> getActivityProfiles(@RequestParam(required = false) String compound,
              @RequestParam(required = false) String kinaseEntrez,
              @RequestParam(required = false) Double ic50,
              @SortDefault.SortDefaults({ @SortDefault("kd"), @SortDefault("percentControl") }) Pageable pageInfo) {

        // ic50 must be between 0 and 1
        if (ic50 != null && (ic50 > 10000 || ic50 < 0)) {
            throw new BadRequestException(messages.get("error.ic50OutOfRange"));
        }

        List<Long> kinaseIds = null;
        if (StringUtils.isNotEmpty(kinaseEntrez)) {
            List<Kinase> kinases = kinaseService.getKinase(kinaseEntrez);
            if (kinases.isEmpty()) {
                throw new BadRequestException(messages.get("error.noSuchKinase", kinaseEntrez));
            }
            kinaseIds = kinases.stream()
                .map(Kinase::getId)
                .collect(Collectors.toList());
        }

//        Page<NanoBretActivityProfile> page = activityProfileService.getActivityProfiles(compound, kinaseIds, ic50, pageInfo);
        Page<NanoBretActivityProfile> page = new PageImpl<>(Collections.emptyList(), pageInfo, 0);

        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}
