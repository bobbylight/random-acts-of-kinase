package org.sgc.rak.services;

import org.sgc.rak.dao.NanoBretActivityProfileDao;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.NanoBretActivityProfile;
import org.sgc.rak.model.NanoBretActivityProfileModifier;
import org.sgc.rak.model.csv.NanoBretActivityProfileCsvRecord;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for manipulating NanoBRET activity profiles.
 */
@Service
public class NanoBretActivityProfileService {

    private final NanoBretActivityProfileDao nanoBretActivityProfileDao;
    private final CompoundService compoundService;
    private final KinaseService kinaseService;

    private final Messages messages;

    private static final Logger LOGGER = LoggerFactory.getLogger(NanoBretActivityProfileService.class);

    @Autowired
    public NanoBretActivityProfileService(NanoBretActivityProfileDao nanoBretActivityProfileDao,
                                          CompoundService compoundService,
                                          KinaseService kinaseService, Messages messages) {
        this.nanoBretActivityProfileDao = nanoBretActivityProfileDao;
        this.compoundService = compoundService;
        this.kinaseService = kinaseService;
        this.messages = messages;
    }

    private NanoBretActivityProfile activityProfileCsvRecordToActivityProfile(NanoBretActivityProfileCsvRecord csvRep) {

        if (!compoundService.getCompoundExists(csvRep.getCompoundName())) {
            throw new BadRequestException(messages.get("error.importReferencesUnknownCompound",
                csvRep.getCompoundName()));
        }

        Kinase kinase = kinaseService.getKinaseByDiscoverx(csvRep.getDiscoverxGeneSymbol());
        if (kinase == null) {
            throw new BadRequestException(messages.get("error.importReferencesUnknownKinase",
                csvRep.getDiscoverxGeneSymbol()));
        }

        NanoBretActivityProfile profile = new NanoBretActivityProfile();
        profile.setDate(Util.nanoBretCsvDateToRealDate(csvRep.getDate()));
        profile.setComment(csvRep.getComment());
        profile.setCompoundName(csvRep.getCompoundName());
        profile.setConcentration(csvRep.getCompoundConcentration());
        profile.setIc50(csvRep.getIc50());
        profile.setKinase(kinase);
        profile.setModifier(csvRep.getModifier());
        profile.setNlucOrientation(csvRep.getNlucOrientation());
        profile.setPercentInhibition(csvRep.getPercentInhibition());
        profile.setPoints(csvRep.getPoints());

        return profile;
    }

    private List<ObjectImportRep.FieldStatus> activityProfileCsvRecordToFieldStatusList(
        NanoBretActivityProfile newProfile, NanoBretActivityProfile existingProfile) {

        Date existingDate = null;
        String existingComment = null;
        String existingCompoundName = null;
        String existingDiscoverx = null;
        Integer existingConcentration = null;
        Double existingIc50 = null;
        NanoBretActivityProfileModifier existingModifier = null;
        String existingNluc = null;
        Double existingPercentInhibition = null;
        Integer existingPoints = null;

        if (existingProfile != null) {
            existingDate = existingProfile.getDate();
            existingComment = existingProfile.getComment();
            existingCompoundName = existingProfile.getCompoundName();
            existingDiscoverx = existingProfile.getKinase().getDiscoverxGeneSymbol();
            existingConcentration = existingProfile.getConcentration();
            existingIc50 = existingProfile.getIc50();
            existingModifier = existingProfile.getModifier();
            existingNluc = existingProfile.getNlucOrientation();
            existingPercentInhibition = existingProfile.getPercentInhibition();
            existingPoints = existingProfile.getPoints();
        }

        String newDate = Util.realDateToNanoBretCsvDate(newProfile.getDate());
        String existingDateStr = Util.realDateToNanoBretCsvDate(existingDate);

        return Arrays.asList(
            Util.createFieldStatus("date", newDate, existingDateStr),
            Util.createFieldStatus("comment", newProfile.getComment(), existingComment),
            Util.createFieldStatus("compoundName", newProfile.getCompoundName(), existingCompoundName),
            Util.createFieldStatus("concentration", newProfile.getConcentration(), existingConcentration),
            Util.createFieldStatus("ic50", newProfile.getIc50(), existingIc50),
            Util.createFieldStatus("discoverxGeneSymbol", newProfile.getKinase().getDiscoverxGeneSymbol(),
                existingDiscoverx),
            Util.createFieldStatus("modifier", newProfile.getModifier(), existingModifier),
            Util.createFieldStatus("nlucOrientation", newProfile.getNlucOrientation(), existingNluc),
            Util.createFieldStatus("percentInhibition", newProfile.getPercentInhibition(),
                existingPercentInhibition),
            Util.createFieldStatus("points", newProfile.getPoints(), existingPoints)
        );
    }

    /**
     * Upserts a list of NanoBRET activity profiles.  New profiles are added, existing ones are updated.
     * Profiles are considered unique by the combination of compound name, entrez, and date.
     *
     * @param activityProfileCsvRecords The activity profiles to upsert.
     * @param commit Whether to actually commit the patch, or just return the possible result.
     * @return The result of the operation (or possible result, if {@code commit} is {@code false}).
     */
    public ObjectImportRep importNanoBretActivityProfiles(
                                        List<NanoBretActivityProfileCsvRecord> activityProfileCsvRecords,
                                        boolean commit) {

        List<String> compoundNames = activityProfileCsvRecords.stream()
            .map(NanoBretActivityProfileCsvRecord::getCompoundName).collect(Collectors.toList());
        List<String> discoverxes = activityProfileCsvRecords.stream()
            .map(NanoBretActivityProfileCsvRecord::getDiscoverxGeneSymbol).collect(Collectors.toList());
        List<Date> dates = activityProfileCsvRecords.stream()
            .map(NanoBretActivityProfileCsvRecord::getDate)
            .map(Util::nanoBretCsvDateToRealDate)
            .collect(Collectors.toList());
        Set<NanoBretActivityProfile> existingProfiles = nanoBretActivityProfileDao
            .getNanoBretActivityProfiles(compoundNames, discoverxes, dates);

        ObjectImportRep importRep = new ObjectImportRep();
        List<List<ObjectImportRep.FieldStatus>> records = new ArrayList<>();
        importRep.setFieldStatuses(records);
        List<NanoBretActivityProfile> toPersist = new ArrayList<>();

        for (NanoBretActivityProfileCsvRecord csvRecord : activityProfileCsvRecords) {

            Util.convertEmptyStringsToNulls(csvRecord);
            String compoundName = csvRecord.getCompoundName();
            String discoverx = csvRecord.getDiscoverxGeneSymbol();
            Date date = Util.nanoBretCsvDateToRealDate(csvRecord.getDate());
            NanoBretActivityProfile newActivityProfile;

            NanoBretActivityProfile existingProfile = possiblyGetNanoBretActivityProfile(existingProfiles, compoundName,
                discoverx, date);
            if (existingProfile != null) {
                newActivityProfile = Util.patchNanoBretActivityProfile(existingProfile, csvRecord);
            }
            else {
                newActivityProfile = activityProfileCsvRecordToActivityProfile(csvRecord);
            }

            toPersist.add(newActivityProfile);
            records.add(activityProfileCsvRecordToFieldStatusList(newActivityProfile, existingProfile));
        }

        possiblyLogImportOperation(commit, toPersist);
        if (commit) {
            nanoBretActivityProfileDao.save(toPersist);
        }

        return importRep;
    }

    private static NanoBretActivityProfile possiblyGetNanoBretActivityProfile(Set<NanoBretActivityProfile> profiles,
                                                                  String compoundName, String discoverx, Date date) {
        Optional<NanoBretActivityProfile> optional = profiles.stream()
            .filter(nbap -> compoundName.equals(nbap.getCompoundName()) &&
                discoverx.equals(nbap.getKinase().getDiscoverxGeneSymbol()) &&
                date.equals(nbap.getDate()))
            .findFirst();
        return optional.orElse(null);
    }

    private static void possiblyLogImportOperation(boolean commit, List<NanoBretActivityProfile> toPersist) {
        if (LOGGER.isDebugEnabled()) {
            String header = commit ? "Saving the following {} new or updated nanoBRET activity profiles:" :
                "Returning preview of saving the following {} new or updated nanoBRET activity profiles:";
            LOGGER.debug(header, toPersist.size());
            toPersist.forEach(profile -> LOGGER.debug("- {}", profile));
        }
    }
}
