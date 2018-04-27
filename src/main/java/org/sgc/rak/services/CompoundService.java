package org.sgc.rak.services;

import org.sgc.rak.dao.CompoundDao;
import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.CompoundCountPair;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.KinaseActivityProfile;
import org.sgc.rak.repositories.KinaseActivityProfileRepository;
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

    @Autowired
    private CompoundDao compoundDao;

    @Autowired
    private KinaseService kinaseService;

    @Autowired
    private KinaseActivityProfileRepository kinaseActivityProfileRepository;

    @Autowired
    private Messages messages;

    /**
     * Creates a field status list from a compound.
     *
     * @param compound The compound.
     * @param existing The prior version of the compound.
     * @return The field status list.
     */
    private static List<ObjectImportRep.FieldStatus> compoundToFieldStatusList(Compound compound, Compound existing) {

        String existingCompoundName = null;
        String existingChemotype = null;
        String existingSmiles = null;
        Double existingS10 = null;
        String existingSource = null;

        if (existing != null) {
            existingCompoundName = existing.getCompoundName();
            existingChemotype = existing.getChemotype();
            existingSmiles = existing.getSmiles();
            existingS10 = existing.getS10();
            existingSource = existing.getSource();
        }

        return Arrays.asList(
            createCompoundFieldStatus("compoundName", compound.getCompoundName(), existingCompoundName),
            createCompoundFieldStatus("chemotype", compound.getChemotype(), existingChemotype),
            createCompoundFieldStatus("smiles", compound.getSmiles(), existingSmiles),
            createCompoundFieldStatus("s10", compound.getS10(), existingS10),
            createCompoundFieldStatus("source", compound.getSource(), existingSource)
        );
    }

    /**
     * Creates a field status representing a new field value.
     *
     * @param name The field name.
     * @param newValue The new value for the field.
     * @param existingValue The existing/prior value for the field.
     * @return The field status.
     */
    private static ObjectImportRep.FieldStatus createCompoundFieldStatus(String name, Object newValue,
                                                                         Object existingValue) {
        ObjectImportRep.FieldStatus status = new ObjectImportRep.FieldStatus();
        status.setFieldName(name);
        status.setNewValue(newValue);
        status.setOldValue(existingValue);
        return status;
    }

    /**
     * Returns information on a specific compound.
     *
     * @param compoundName The compound name, ignoring case.
     * @return Information on the compound.  If no such compound is known, an exception
     *         is thrown.
     */
    public Compound getCompound(String compoundName) {
        Compound compound = compoundDao.getCompound(compoundName);
        if (compound == null) {
            throw new NotFoundException(messages.get("error.noSuchCompound", compoundName));
        }
        return compound;
    }

    /**
     * Returns compound information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompoundsByCompoundName(String, Pageable)
     * @see #getIncompleteCompounds(Pageable)
     */
    public Page<Compound> getCompounds(Pageable pageInfo) {
        return compoundDao.getCompounds(pageInfo);
    }

    /**
     * Returns compounds whose names start with a given string, ignoring case.
     *
     * @param compoundNamePart The start of a compound name.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompounds(Pageable)
     */
    public Page<Compound> getCompoundsByCompoundName(String compoundNamePart,
                                                     Pageable pageInfo) {
        return compoundDao.getCompoundsByCompoundNameStartsWithIgnoreCase(compoundNamePart, pageInfo);
    }

    public Page<Compound> getCompoundsByKinaseAndActivity(String kinase, double activity, Pageable pageInfo) {

        Kinase kinase2 = kinaseService.getKinase(kinase);
        if (kinase2 == null) {
            throw new NotFoundException(messages.get("error.noSuchKinase", kinase));
        }
        long kinaseId = kinase2.getId();

        Page<KinaseActivityProfile> profiles = kinaseActivityProfileRepository.
            getKinaseActivityProfilesByKinaseIdAndPercentControlLessThanEqual(kinaseId, activity, pageInfo);

        List<String> compoundNames = profiles.getContent().stream().map(KinaseActivityProfile::getCompoundName)
            .collect(Collectors.toList());

        List<Compound> compounds = compoundDao.getCompounds(compoundNames);
        return new PageImpl<>(compounds, pageInfo, profiles.getTotalElements());
    }

    /**
     * Returns information about compounds that are missing activity profiles.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     */
    public Page<CompoundCountPair> getCompoundsMissingActivityProfiles(Pageable pageInfo) {
        return compoundDao.getCompoundsMissingActivityProfiles(pageInfo);
    }

    /**
     * Returns information about compounds without SMILES strings, or any other missing fields.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompounds(Pageable)
     */
    public Page<Compound> getIncompleteCompounds(Pageable pageInfo) {
        return compoundDao.getIncompleteCompounds(pageInfo);
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
}
