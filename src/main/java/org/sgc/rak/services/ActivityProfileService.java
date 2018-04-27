package org.sgc.rak.services;

import org.sgc.rak.dao.ActivityProfileDao;
import org.sgc.rak.model.KinaseActivityProfile;
import org.sgc.rak.reps.ObjectImportRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Create a field status representing a new value.
     *
     * @param name The name of the field.
     * @param value The new value.
     * @return The field status.
     */
    private static ObjectImportRep.FieldStatus createNewFieldStatus(String name, Object value) {
        ObjectImportRep.FieldStatus status = new ObjectImportRep.FieldStatus();
        status.setFieldName(name);
        status.setNewValue(value);
        return status;
    }

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

    /**
     * Upserts a list of activity profiles.  New profiles are added, existing ones are updated.
     *
     * @param activityProfiles The activity profiles to upsert.
     * @param commit Whether to actually commit the patch, or just return the possible result.
     * @return The result of the operation (or possible result, if {@code commit} is {@code false}).
     */
    public ObjectImportRep importActivityProfiles(List<KinaseActivityProfile> activityProfiles, boolean commit) {

        // TODO: Fetch existing profiles and merge.  Right now we just assume everything is new and
        // we'll let the failure to insert a row trigger an error.

        ObjectImportRep importRep = new ObjectImportRep();
        List<List<ObjectImportRep.FieldStatus>> records = new ArrayList<>();
        importRep.setFieldStatuses(records);

        for (KinaseActivityProfile activityProfile : activityProfiles) {

            List<ObjectImportRep.FieldStatus> fields = Arrays.asList(
            	createNewFieldStatus("compoundName", activityProfile.getCompoundName()),
            	createNewFieldStatus("compoundConcentration", activityProfile.getCompoundConcentration()),
            	createNewFieldStatus("kd", activityProfile.getKd()),
            	createNewFieldStatus("kianse", activityProfile.getKinase()),
            	createNewFieldStatus("percentControl", activityProfile.getPercentControl())
            );

            records.add(fields);
        }

        if (commit) {
            activityProfileDao.save(activityProfiles);
        }

        return importRep;
    }
}
