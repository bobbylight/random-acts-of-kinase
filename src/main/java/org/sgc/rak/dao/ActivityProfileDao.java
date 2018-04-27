package org.sgc.rak.dao;

import org.sgc.rak.model.KinaseActivityProfile;
import org.sgc.rak.repositories.KinaseActivityProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * DAO for manipulating kinase activity profiles.
 */
public class ActivityProfileDao {

    @Autowired
    private KinaseActivityProfileRepository activityProfileRepository;

    /**
     * Returns kinase activity profile information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     * @see #getKinaseActivityProfilesByCompoundNameIgnoreCase(String, Pageable)
     */
    public Page<KinaseActivityProfile> getKinaseActivityProfiles(Pageable pageInfo) {
        return activityProfileRepository.findAll(pageInfo);
    }

    /**
     * Returns kinase activity profiles for a given compound/inhibitor.
     *
     * @param compoundName The compound name.  Case is ignored.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     * @see #getKinaseActivityProfiles(Pageable)
     */
    public Page<KinaseActivityProfile> getKinaseActivityProfilesByCompoundNameIgnoreCase(String compoundName,
                                                                                         Pageable pageInfo) {
        return activityProfileRepository.getKinaseActivityProfilesByCompoundNameIgnoreCase(compoundName, pageInfo);
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
    public Page<KinaseActivityProfile>
            getKinaseActivityProfilesByCompoundNameIgnoreCaseAndKinaseIgnoreCaseAndPercentControl(
                                    String compoundName, long kinase, double activity, Pageable pageInfo) {
        return activityProfileRepository.
            getKinaseActivityProfilesByCompoundNameIgnoreCaseAndKinaseIdAndPercentControlLessThanEqual(
                    compoundName, kinase, activity, pageInfo);
    }

    public Page<KinaseActivityProfile> getKinaseActivityProfilesByKinaseIgnoreCaseAndPercentControl(long kinase,
                                                      double activity, Pageable pageInfo) {
        return activityProfileRepository.getKinaseActivityProfilesByKinaseIdAndPercentControlLessThanEqual(kinase,
            activity,  pageInfo);
    }

    /**
     * Saves a collection of activity profiles.
     *
     * @param activityProfiles The activity profiles to save.
     */
    public void save(Iterable<KinaseActivityProfile> activityProfiles) {
        activityProfileRepository.saveAll(activityProfiles);
    }
}
