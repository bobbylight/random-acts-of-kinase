package org.sgc.rak.services;

import org.sgc.rak.dao.ActivityProfileDao;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.KinaseActivityProfile;
import org.sgc.rak.reps.KinaseActivityProfileCsvRecordRep;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for manipulating kinase activity profiles.
 */
@Service
public class ActivityProfileService {

    private final ActivityProfileDao activityProfileDao;
    private final CompoundService compoundService;
    private final KinaseService kinaseService;

    private final Messages messages;

    @Autowired
    public ActivityProfileService(ActivityProfileDao activityProfileDao, CompoundService compoundService,
                                  KinaseService kinaseService, Messages messages) {
        this.activityProfileDao = activityProfileDao;
        this.compoundService = compoundService;
        this.kinaseService = kinaseService;
        this.messages = messages;
    }

    private KinaseActivityProfile activityProfileCsvRecordToActivityProfile(KinaseActivityProfileCsvRecordRep csvRep) {

        if (!compoundService.getCompoundExists(csvRep.getCompoundName())) {
            throw new BadRequestException(messages.get("error.importReferencesUnknownCompound",
                csvRep.getCompoundName()));
        }

        Kinase kinase = kinaseService.getKinase(csvRep.getDiscoverxGeneSymbol());
        if (kinase == null) {
            throw new BadRequestException(messages.get("error.importReferencesUnknownKinase",
                csvRep.getDiscoverxGeneSymbol()));
        }

        KinaseActivityProfile profile = new KinaseActivityProfile();
        profile.setCompoundName(csvRep.getCompoundName());
        profile.setKinase(kinase);
        profile.setPercentControl(csvRep.getPercentControl());
        profile.setCompoundConcentration(csvRep.getCompoundConcentration());

        return profile;
    }

    private List<ObjectImportRep.FieldStatus> activityProfileCsvRecordToFieldStatusList(
            KinaseActivityProfile newProfile, KinaseActivityProfile existingProfile) {

        String existingCompoundName = null;
        String existingDiscoverx = null;
        Double existingPercentControl = null;
        Integer existingCompoundConcentration = null;

        if (existingProfile != null) {
            existingCompoundName = existingProfile.getCompoundName();
            existingDiscoverx = existingProfile.getKinase().getDiscoverxGeneSymbol();
            existingPercentControl = existingProfile.getPercentControl();
            existingCompoundConcentration = existingProfile.getCompoundConcentration();
        }

        return Arrays.asList(
            Util.createFieldStatus("compoundName", newProfile.getCompoundName(), existingCompoundName),
            Util.createFieldStatus("discoverxGeneSymbol", newProfile.getKinase().getDiscoverxGeneSymbol(),
                existingDiscoverx),
            Util.createFieldStatus("percentControl", newProfile.getPercentControl(), existingPercentControl),
            Util.createFieldStatus("compoundConcentration",
                newProfile.getCompoundConcentration(), existingCompoundConcentration)
        );
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
     * @param activityProfileCsvRecords The activity profiles to upsert.
     * @param commit Whether to actually commit the patch, or just return the possible result.
     * @return The result of the operation (or possible result, if {@code commit} is {@code false}).
     */
    public ObjectImportRep importActivityProfiles(List<KinaseActivityProfileCsvRecordRep> activityProfileCsvRecords,
                                                  boolean commit) {

        List<String> compoundNames = activityProfileCsvRecords.stream()
            .map(KinaseActivityProfileCsvRecordRep::getCompoundName).collect(Collectors.toList());
        List<String> discoverxes = activityProfileCsvRecords.stream()
            .map(KinaseActivityProfileCsvRecordRep::getDiscoverxGeneSymbol).collect(Collectors.toList());
        List<KinaseActivityProfile> existingProfiles = activityProfileDao
            .getKinaseActivityProfiles(compoundNames, discoverxes);

        ObjectImportRep importRep = new ObjectImportRep();
        List<List<ObjectImportRep.FieldStatus>> records = new ArrayList<>();
        importRep.setFieldStatuses(records);
        List<KinaseActivityProfile> toPersist = new ArrayList<>();

        for (KinaseActivityProfileCsvRecordRep activityProfileCsvRecord : activityProfileCsvRecords) {

            Util.convertEmptyStringsToNulls(activityProfileCsvRecord);
            String compoundName = activityProfileCsvRecord.getCompoundName();
            String discoverx = activityProfileCsvRecord.getDiscoverxGeneSymbol();
            KinaseActivityProfile newActivityProfile;

            KinaseActivityProfile existingProfile = possiblyGetActivityProfile(existingProfiles, compoundName,
                discoverx);
            if (existingProfile != null) {
                newActivityProfile = Util.patchActivityProfile(existingProfile, activityProfileCsvRecord);
            }
            else {
                newActivityProfile = activityProfileCsvRecordToActivityProfile(activityProfileCsvRecord);
            }

            toPersist.add(newActivityProfile);
            records.add(activityProfileCsvRecordToFieldStatusList(newActivityProfile, existingProfile));
        }

        if (commit) {
            activityProfileDao.save(toPersist);
        }

        return importRep;
    }

    private static KinaseActivityProfile possiblyGetActivityProfile(List<KinaseActivityProfile> profiles,
                                                                    String compoundName, String discoverx) {
        Optional<KinaseActivityProfile> optional = profiles.stream()
            .filter(c -> compoundName.equals(c.getCompoundName()) &&
                        discoverx.equals(c.getKinase().getDiscoverxGeneSymbol()))
            .findFirst();
        return optional.orElse(null);
    }
}
