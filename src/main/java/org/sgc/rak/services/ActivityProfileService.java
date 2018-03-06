package org.sgc.rak.services;

import org.sgc.rak.dao.ActivityProfileDao;
import org.sgc.rak.model.KinaseActivityProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for manipulating kinase activity profiles.
 */
@Service
public class ActivityProfileService {

    @Autowired
    private ActivityProfileDao activityProfileDao;

    @Autowired
    private CompoundService compoundService;

    /**
     * Returns kinase activity profile information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     * @see #getKinaseActivityProfilesForCompound(String, Pageable)
     */
    public Page<KinaseActivityProfile> getKinaseActivityProfiles(Pageable pageInfo) {
        return activityProfileDao.getKinaseActivityProfiles(pageInfo);
    }

    /**
     * Returns kinase activity profiles for a given compound/inhibitor.
     *
     * @param compoundName The compound name.  Case is ignored.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     * @see #getKinaseActivityProfiles(Pageable)
     */
    public Page<KinaseActivityProfile> getKinaseActivityProfilesForCompound(String compoundName,
                                                                            Pageable pageInfo) {
        compoundService.getCompound(compoundName); // Throw exception if compound not found
        return activityProfileDao.getKinaseActivityProfilesByCompoundNameIgnoreCase(compoundName, pageInfo);
    }

    /**
     * Returns kinase activity profiles for a given compound/inhibitor, kinase and activity.
     *
     * @param compoundName The compound name.  Case is ignored.
     * @param kinase The kinase.  Case is ignored.
     * @param activity The activity of the reaction.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     * @see #getKinaseActivityProfiles(Pageable)
     */
    public Page<KinaseActivityProfile> getKinaseActivityProfilesForCompoundAndKinaseAndPercentControl(
            String compoundName, long kinase, double activity, Pageable pageInfo) {
        compoundService.getCompound(compoundName); // Throw exception if compound not found
        //kinaseService.getKinase(kinase); // Throw exception if kinase not found
        return activityProfileDao.getKinaseActivityProfilesByCompoundNameIgnoreCaseAndKinaseIgnoreCaseAndPercentControl(
            compoundName, kinase, activity, pageInfo);
    }

    public Page<KinaseActivityProfile> getKinaseActivityProfilesForKinaseAndPercentControl(long kinase,
                                                 double activity, Pageable pageInfo) {
        //kinaseService.getKinase(kinase); // Throw exception if kinase not found
        return activityProfileDao.getKinaseActivityProfilesByKinaseIgnoreCaseAndPercentControl(kinase,
            activity, pageInfo);
    }
}
