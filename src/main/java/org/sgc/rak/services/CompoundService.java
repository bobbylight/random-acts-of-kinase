package org.sgc.rak.services;

import org.sgc.rak.dao.CompoundDao;
import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.CompoundCountPair;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.repositories.ActivityProfileRepository;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for manipulating compounds.
 */
@Service
public class CompoundService {

    private final CompoundDao compoundDao;
    private final KinaseService kinaseService;
    private final ActivityProfileRepository activityProfileRepository;
    private final Messages messages;

    @Autowired
    public CompoundService(CompoundDao compoundDao, KinaseService kinaseService,
                           ActivityProfileRepository activityProfileRepository, Messages messages) {
        this.compoundDao = compoundDao;
        this.kinaseService = kinaseService;
        this.activityProfileRepository = activityProfileRepository;
        this.messages = messages;
    }

    /**
     * Creates a field status list from a compound that's being modified.
     *
     * @param compound The new version of the compound.
     * @param existing The prior version of the compound.
     * @return The field status list.
     */
    private static List<ObjectImportRep.FieldStatus> compoundToFieldStatusList(Compound compound, Compound existing) {

        String existingCompoundName = null;
        String existingChemotype = null;
        String existingSmiles = null;
        Double existingS10 = null;
        String existingSource = null;
        String existingPrimaryReference = null;
        String existingPrimaryReferenceUrl = null;
        Boolean existingHidden = null; // false here makes no records "new" when previewing an import

        if (existing != null) {
            existingCompoundName = existing.getCompoundName();
            existingChemotype = existing.getChemotype();
            existingSmiles = existing.getSmiles();
            existingS10 = existing.getS10();
            existingSource = existing.getSource();
            existingPrimaryReference = existing.getPrimaryReference();
            existingPrimaryReferenceUrl = existing.getPrimaryReferenceUrl();
            existingHidden = existing.isHidden();
        }

        return Arrays.asList(
            Util.createFieldStatus("compoundName", compound.getCompoundName(), existingCompoundName),
            Util.createFieldStatus("chemotype", compound.getChemotype(), existingChemotype),
            Util.createFieldStatus("smiles", compound.getSmiles(), existingSmiles),
            Util.createFieldStatus("s10", compound.getS10(), existingS10),
            Util.createFieldStatus("source", compound.getSource(), existingSource),
            Util.createFieldStatus("primaryReference", compound.getPrimaryReference(), existingPrimaryReference),
            Util.createFieldStatus("primaryReferenceUrl", compound.getPrimaryReferenceUrl(),
                existingPrimaryReferenceUrl),
            Util.createFieldStatus("hidden", compound.isHidden(), existingHidden)
        );
    }

    /**
     * Returns information on a specific compound.
     *
     * @param compoundName The compound name, ignoring case.
     * @return Information on the compound, or {@code null} if no such compound is known.
     * @see #getCompoundExists(String)
     */
    public Compound getCompound(String compoundName) {
        return compoundDao.getCompound(compoundName);
    }

    /**
     * Returns whether a specific compound exists.
     *
     * @param compoundName The compound name, ignoring case.
     * @return Whether the compound exists.
     * @see #getCompound(String)
     */
    public boolean getCompoundExists(String compoundName) {
        return compoundDao.getCompoundExists(compoundName);
    }

    /**
     * Returns compound information.
     *
     * @param compoundNamePart A part of a compound name.  This may be {@code null} to omit filtering
     *        by compound name.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @param includeHidden Whether hidden compounds should be included in the result.
     * @return The list of compounds.
     */
    public Page<Compound> getCompounds(String compoundNamePart, Pageable pageInfo, boolean includeHidden) {
        return compoundDao.getCompounds(compoundNamePart, pageInfo, includeHidden);
    }

    public Page<Compound> getCompoundsByKinaseAndActivity(String kinase, double activity, Pageable pageInfo) {

        Kinase kinase2 = kinaseService.getKinase(kinase);
        if (kinase2 == null) {
            throw new NotFoundException(messages.get("error.noSuchKinase", kinase));
        }
        long kinaseId = kinase2.getId();

        Page<ActivityProfile> profiles = activityProfileRepository.
            getActivityProfilesByKinaseIdAndPercentControlLessThanEqual(kinaseId, activity, pageInfo);

        List<String> compoundNames = profiles.getContent().stream().map(ActivityProfile::getCompoundName)
            .collect(Collectors.toList());

        List<Compound> compounds = compoundDao.getCompounds(compoundNames);
        return new PageImpl<>(compounds, pageInfo, profiles.getTotalElements());
    }

    public Page<Compound> getCompoundsByKinaseAndKd(String kinase, double kd, Pageable pageInfo) {

        Kinase kinase2 = kinaseService.getKinase(kinase);
        if (kinase2 == null) {
            throw new NotFoundException(messages.get("error.noSuchKinase", kinase));
        }
        long kinaseId = kinase2.getId();

        Page<ActivityProfile> profiles = activityProfileRepository.
            getActivityProfilesByKinaseIdAndKdLessThanEqual(kinaseId, kd, pageInfo);

        List<String> compoundNames = profiles.getContent().stream().map(ActivityProfile::getCompoundName)
            .collect(Collectors.toList());

        List<Compound> compounds = compoundDao.getCompounds(compoundNames);
        return new PageImpl<>(compounds, pageInfo, profiles.getTotalElements());
    }

    /**
     * Returns information about compounds that are missing activity profiles.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompoundsMissingPublicationInfo(String, Pageable)
     * @see #getIncompleteCompounds(String, Pageable)
     */
    public Page<CompoundCountPair> getCompoundsMissingActivityProfiles(String compound, Pageable pageInfo) {
        return compoundDao.getCompoundsMissingActivityProfiles(compound, pageInfo);
    }

    /**
     * Returns information about compounds that are missing one or more publication information fields.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompoundsMissingActivityProfiles(String, Pageable)
     * @see #getIncompleteCompounds(String, Pageable)
     */
    public Page<Compound> getCompoundsMissingPublicationInfo(String compound, Pageable pageInfo) {
        return compoundDao.getCompoundsMissingPublicationInfo(compound, pageInfo);
    }

    /**
     * Returns information about compounds that are hidden.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    public Page<Compound> getHiddenCompounds(String compound, Pageable pageInfo) {
        return compoundDao.getHiddenCompounds(compound, pageInfo);
    }

    /**
     * Returns information about compounds without SMILES strings or s(10) values.
     *
     * @param compound A part of a compound name.  If specified, only compounds
     *        whose name contains this substring (ignoring case) will be returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompounds(String, Pageable, boolean)
     * @see #getCompoundsMissingActivityProfiles(String, Pageable)
     * @see #getCompoundsMissingPublicationInfo(String, Pageable)
     */
    public Page<Compound> getIncompleteCompounds(String compound, Pageable pageInfo) {
        return compoundDao.getIncompleteCompounds(compound, pageInfo);
    }

    /**
     * Upserts a list of compounds.  New compounds are added, existing ones are updated.
     *
     * @param compounds The compounds to upsert.
     * @param commit Whether to actually commit the patch, or just return the possible result.
     * @return The result of the operation (or possible result, if {@code commit} is {@code false}).
     */
    public ObjectImportRep importCompounds(List<Compound> compounds, boolean commit) {

        List<String> compoundNames = compounds.stream().map(Compound::getCompoundName).collect(Collectors.toList());
        List<Compound> existingCompounds = compoundDao.getCompounds(compoundNames);

        ObjectImportRep importRep = new ObjectImportRep();
        List<List<ObjectImportRep.FieldStatus>> records = new ArrayList<>();
        importRep.setFieldStatuses(records);

        List<Compound> toPersist = new ArrayList<>();

        for (Compound compound : compounds) {

            Util.convertEmptyStringsToNulls(compound);
            String compoundName = compound.getCompoundName();
            Compound newCompound;

            Compound existingCompound = possiblyGetCompound(existingCompounds, compoundName);
            if (existingCompound != null) {
                newCompound = Util.patchCompound(existingCompound, compound);
            }
            else {
                newCompound = compound;
            }

            toPersist.add(newCompound);
            records.add(compoundToFieldStatusList(newCompound, existingCompound));
        }

        if (commit) {
            compoundDao.save(toPersist);
        }

        return importRep;
    }

    private static Compound possiblyGetCompound(List<Compound> compounds, String compoundName) {
        Optional<Compound> optional = compounds.stream().filter(c -> compoundName.equals(c.getCompoundName()))
            .findFirst();
        return optional.orElse(null);
    }

    /**
     * Updates a compound.
     *
     * @param compound The compound to update.
     * @return The updated compound.
     */
    public Compound updateCompound(Compound compound) {
        return compoundDao.save(compound);
    }
}
