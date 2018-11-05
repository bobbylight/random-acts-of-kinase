package org.sgc.rak.services;

import org.sgc.rak.dao.ActivityProfileDao;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.KdCsvRecord;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityProfileService.class);

    @Autowired
    public ActivityProfileService(ActivityProfileDao activityProfileDao, CompoundService compoundService,
                                  KinaseService kinaseService, Messages messages) {
        this.activityProfileDao = activityProfileDao;
        this.compoundService = compoundService;
        this.kinaseService = kinaseService;
        this.messages = messages;
    }

    private ActivityProfile activityProfileCsvRecordToActivityProfile(ActivityProfileCsvRecord csvRep) {

        if (!compoundService.getCompoundExists(csvRep.getCompoundName())) {
            throw new BadRequestException(messages.get("error.importReferencesUnknownCompound",
                csvRep.getCompoundName()));
        }

        Kinase kinase = kinaseService.getKinase(csvRep.getDiscoverxGeneSymbol());
        if (kinase == null) {
            throw new BadRequestException(messages.get("error.importReferencesUnknownKinase",
                csvRep.getDiscoverxGeneSymbol()));
        }

        ActivityProfile profile = new ActivityProfile();
        profile.setCompoundName(csvRep.getCompoundName());
        profile.setKinase(kinase);
        profile.setPercentControl(csvRep.getPercentControl());
        profile.setCompoundConcentration(csvRep.getCompoundConcentration());

        return profile;
    }

    private List<ObjectImportRep.FieldStatus> activityProfileCsvRecordToFieldStatusList(
        ActivityProfile newProfile, ActivityProfile existingProfile) {

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
     * Returns kinase activity profiles for a given compound/inhibitor, kinase and activity.
     *
     * @param compoundName The compound name.  Case is ignored.  This may be {@code null} if the returned
     *        list should not be restricted to a particular compound.
     * @param kinaseId The kinase involved in the activity profile.  This may be {@code null} to not limit
     *        the search to one particular kinase.
     * @param percentControl The value that the percent control of the activity profile must be less than or
     *        equal to. This may be {@code null} to not restrict by percent control.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     */
    public Page<ActivityProfile> getActivityProfiles(String compoundName, Long kinaseId, Double percentControl,
                                                     Pageable pageInfo) {

        if (compoundName != null && !compoundService.getCompoundExists(compoundName)) {
            throw new BadRequestException(messages.get("error.noSuchCompound", compoundName));
        }

        return activityProfileDao.getActivityProfiles(compoundName, kinaseId, percentControl, pageInfo);
    }

    /**
     * Upserts a list of activity profiles.  New profiles are added, existing ones are updated.
     *
     * @param activityProfileCsvRecords The activity profiles to upsert.
     * @param commit Whether to actually commit the patch, or just return the possible result.
     * @return The result of the operation (or possible result, if {@code commit} is {@code false}).
     */
    public ObjectImportRep importActivityProfiles(List<ActivityProfileCsvRecord> activityProfileCsvRecords,
                                                  boolean commit) {

        List<String> compoundNames = activityProfileCsvRecords.stream()
            .map(ActivityProfileCsvRecord::getCompoundName).collect(Collectors.toList());
        List<String> discoverxes = activityProfileCsvRecords.stream()
            .map(ActivityProfileCsvRecord::getDiscoverxGeneSymbol).collect(Collectors.toList());
        Set<ActivityProfile> existingProfiles = activityProfileDao
            .getActivityProfiles(compoundNames, discoverxes);

        ObjectImportRep importRep = new ObjectImportRep();
        List<List<ObjectImportRep.FieldStatus>> records = new ArrayList<>();
        importRep.setFieldStatuses(records);
        List<ActivityProfile> toPersist = new ArrayList<>();

        for (ActivityProfileCsvRecord activityProfileCsvRecord : activityProfileCsvRecords) {

            Util.convertEmptyStringsToNulls(activityProfileCsvRecord);
            String compoundName = activityProfileCsvRecord.getCompoundName();
            String discoverx = activityProfileCsvRecord.getDiscoverxGeneSymbol();
            ActivityProfile newActivityProfile;

            ActivityProfile existingProfile = possiblyGetActivityProfile(existingProfiles, compoundName,
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

        possiblyLogImportOperation(commit, toPersist);
        if (commit) {
            activityProfileDao.save(toPersist);
        }

        return importRep;
    }

    /**
     * Upserts a list of Kd values into activity profiles.  New profiles are added, existing ones are updated.
     *
     * @param kdValueCsvRecords The Kd data to upsert.
     * @param commit Whether to actually commit the patch, or just return the possible result.
     * @return The result of the operation (or possible result, if {@code commit} is {@code false}).
     */
    public ObjectImportRep importKdValues(List<KdCsvRecord> kdValueCsvRecords,
                                                  boolean commit) {

        List<String> compoundNames = kdValueCsvRecords.stream()
            .map(KdCsvRecord::getCompoundName).collect(Collectors.toList());
        List<String> discoverxes = kdValueCsvRecords.stream()
            .map(KdCsvRecord::getDiscoverxGeneSymbol).collect(Collectors.toList());
        Set<ActivityProfile> existingProfiles = activityProfileDao
            .getActivityProfiles(compoundNames, discoverxes);

        ObjectImportRep importRep = new ObjectImportRep();
        List<List<ObjectImportRep.FieldStatus>> records = new ArrayList<>();
        importRep.setFieldStatuses(records);
        List<ActivityProfile> toPersist = new ArrayList<>();

        for (KdCsvRecord kdValueCsvRecord : kdValueCsvRecords) {

            Util.convertEmptyStringsToNulls(kdValueCsvRecord);
            String compoundName = kdValueCsvRecord.getCompoundName();
            String discoverx = kdValueCsvRecord.getDiscoverxGeneSymbol();
            ActivityProfile newActivityProfile;

            ActivityProfile existingProfile = possiblyGetActivityProfile(existingProfiles, compoundName,
                discoverx);
            if (existingProfile != null) {
                newActivityProfile = Util.patchActivityProfile(existingProfile, kdValueCsvRecord);
            }
            else {
                newActivityProfile = kdCsvRecordToActivityProfile(kdValueCsvRecord);
            }

            toPersist.add(newActivityProfile);
            records.add(kdValueCsvRecordToFieldStatusList(newActivityProfile, existingProfile));
        }

        possiblyLogImportOperation(commit, toPersist);
        if (commit) {
            activityProfileDao.save(toPersist);
        }

        return importRep;
    }

    private ActivityProfile kdCsvRecordToActivityProfile(KdCsvRecord csvRep) {

        if (!compoundService.getCompoundExists(csvRep.getCompoundName())) {
            throw new BadRequestException(messages.get("error.importReferencesUnknownCompound",
                csvRep.getCompoundName()));
        }

        Kinase kinase = kinaseService.getKinase(csvRep.getDiscoverxGeneSymbol());
        if (kinase == null) {
            throw new BadRequestException(messages.get("error.importReferencesUnknownKinase",
                csvRep.getDiscoverxGeneSymbol()));
        }

        ActivityProfile profile = new ActivityProfile();
        profile.setCompoundName(csvRep.getCompoundName());
        profile.setKinase(kinase);
        profile.setKd(csvRep.getKd());

        return profile;
    }

    private List<ObjectImportRep.FieldStatus> kdValueCsvRecordToFieldStatusList(
        ActivityProfile newProfile, ActivityProfile existingProfile) {

        String existingCompoundName = null;
        String existingDiscoverx = null;
        String existingEntrez = null;
        Double existingKd = null;

        if (existingProfile != null) {
            existingCompoundName = existingProfile.getCompoundName();
            existingDiscoverx = existingProfile.getKinase().getDiscoverxGeneSymbol();
            existingEntrez = existingProfile.getKinase().getEntrezGeneSymbol();
            existingKd = existingProfile.getKd();
        }

        return Arrays.asList(
            Util.createFieldStatus("compoundName", newProfile.getCompoundName(), existingCompoundName),
            Util.createFieldStatus("discoverxGeneSymbol", newProfile.getKinase().getDiscoverxGeneSymbol(),
                existingDiscoverx),
            Util.createFieldStatus("entrezGeneSymbol", newProfile.getKinase().getEntrezGeneSymbol(), existingEntrez),
            Util.createFieldStatus("kd", newProfile.getKd(), existingKd)
        );
    }

    private static ActivityProfile possiblyGetActivityProfile(Set<ActivityProfile> profiles,
                                                              String compoundName, String discoverx) {
        Optional<ActivityProfile> optional = profiles.stream()
            .filter(c -> compoundName.equals(c.getCompoundName()) &&
                        discoverx.equals(c.getKinase().getDiscoverxGeneSymbol()))
            .findFirst();
        return optional.orElse(null);
    }

    private static void possiblyLogImportOperation(boolean commit, List<ActivityProfile> toPersist) {
        if (LOGGER.isDebugEnabled()) {
            String header = commit ? "Saving the following {} new or updated activity profiles:" :
                "Returning preview of saving the following {} new or updated activity profiles:";
            LOGGER.debug(header, toPersist.size());
            toPersist.forEach(profile -> LOGGER.debug("- {}", profile));
        }
    }
}
