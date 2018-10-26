package org.sgc.rak.util;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.KdCsvRecord;
import org.sgc.rak.model.csv.SScoreCsvRecord;
import org.sgc.rak.reps.ObjectImportRep;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Obligatory utility methods.
 */
public final class Util {

    private static final String VALID_NON_LETTER_NON_DIGIT_FILENAME_CHARS = "_- ";

    /**
     * Private constructor to prevent instantiation.
     */
    private Util() {
    }

    /**
     * Converts any fields that are empty strings into {@code null}.
     *
     * @param activityProfile The activity profile to examine.
     */
    public static void convertEmptyStringsToNulls(ActivityProfileCsvRecord activityProfile) {

        // Compound name is not checked

        if (StringUtils.isBlank(activityProfile.getDiscoverxGeneSymbol())) {
            activityProfile.setDiscoverxGeneSymbol(null);
        }
    }

    /**
     * Converts any fields that are empty strings into {@code null}.
     *
     * @param compound The compound to examine.
     */
    public static void convertEmptyStringsToNulls(Compound compound) {

        // Compound name is not checked

        if (StringUtils.isBlank(compound.getChemotype())) {
            compound.setChemotype(null);
        }

        if (StringUtils.isBlank(compound.getSmiles())) {
            compound.setSmiles(null);
        }

        if (StringUtils.isBlank(compound.getSource())) {
            compound.setSource(null);
        }
    }

    /**
     * Converts any fields that are empty strings into {@code null}.
     *
     * @param kdCsvRecord The activity profile to examine.
     */
    public static void convertEmptyStringsToNulls(KdCsvRecord kdCsvRecord) {

        // Compound name is not checked

        if (StringUtils.isBlank(kdCsvRecord.getDiscoverxGeneSymbol())) {
            kdCsvRecord.setDiscoverxGeneSymbol(null);
        }

        if (StringUtils.isBlank(kdCsvRecord.getEntrezGeneSymbol())) {
            kdCsvRecord.setEntrezGeneSymbol(null);
        }

        if (StringUtils.isBlank(kdCsvRecord.getModifier())) {
            kdCsvRecord.setModifier(null);
        }
    }

    /**
     * Creates a field status representing a field value and how it changed.
     *
     * @param name The field name.
     * @param newValue The new value for the field.
     * @param existingValue The existing/prior value for the field.
     * @return The field status.
     */
    public static ObjectImportRep.FieldStatus createFieldStatus(String name, Object newValue,
                                                                Object existingValue) {
        ObjectImportRep.FieldStatus status = new ObjectImportRep.FieldStatus();
        status.setFieldName(name);
        status.setNewValue(newValue);
        status.setOldValue(existingValue);
        return status;
    }

    /**
     * Prepares a text string to be used in a 'like' SQL statement; that is, {@code %} and {@code _} are
     * escaped.
     *
     * @param text The text to prepare.
     * @return The escaped version of the text.
     */
    public static String escapeForLike(String text) {
        return text.replace("%", "\\%").replace("_", "\\_");
    }

    /**
     * Returns a string detailing the differences between two compounds.  Useful for auditing when a user
     * updates a compound.
     *
     * @param oldC The prior compound values.
     * @param newC The new compound values.
     * @return The string.
     */
    public static String getCompoundDifference(Compound oldC, Compound newC) {

        List<String> differences = new ArrayList<>();

        possiblyAddDifference(differences, "chemotype", oldC.getChemotype(), newC.getChemotype());
        possiblyAddDifference(differences, "compoundName", oldC.getCompoundName(), newC.getCompoundName());
        possiblyAddDifference(differences, "primaryReference", oldC.getPrimaryReference(), newC.getPrimaryReference());
        possiblyAddDifference(differences, "primaryReferenceUrl", oldC.getPrimaryReferenceUrl(),
            newC.getPrimaryReferenceUrl());
        possiblyAddDifference(differences, "s10", oldC.getS10(), newC.getS10());
        possiblyAddDifference(differences, "smiles", oldC.getSmiles(), newC.getSmiles());
        possiblyAddDifference(differences, "solubility", oldC.getSolubility(), newC.getSolubility());
        possiblyAddDifference(differences, "source", oldC.getSource(), newC.getSource());
        possiblyAddDifference(differences, "hidden", oldC.isHidden(), newC.isHidden());

        return String.join(", ", differences);
    }

    /**
     * Returns whether a character is valid in a file name (e.g. for a suggested download
     * file name).
     *
     * @param ch The character to check.
     * @return Whether it is vaid.
     */
    private static boolean isValidFileNameChar(char ch) {
        return Character.isLetterOrDigit(ch) ||
            VALID_NON_LETTER_NON_DIGIT_FILENAME_CHARS.indexOf(ch) != -1;
    }

    /**
     * Creates and returns a new activity profile that is essentially a patch of {@code newProfile}'s fields into
     * {@code existing}.  That is, the returned activity profile will have any non-{@code null} property values
     * from the new profile, and for any {@code null} property values, it will have the existing profile's
     * values.
     *
     * @param existing The existing activity profile.
     * @param newProfile The new activity profile, whose non-{@code null}/empty values should be merged into
     *        the result.
     * @return The result of the patch/merge operation.
     */
    public static ActivityProfile patchActivityProfile(ActivityProfile existing,
                                                       ActivityProfileCsvRecord newProfile) {

        ActivityProfile retVal = new ActivityProfile();
        retVal.setId(existing.getId());
        retVal.setKd(existing.getKd()); // Kds aren't in activity profile files, so keep existing value

        // It's assumed that the two activity profiles having the same compound and kinase was previously verified
        retVal.setCompoundName(existing.getCompoundName());
        retVal.setKinase(existing.getKinase());

        if (newProfile.getPercentControl() != null) {
            retVal.setPercentControl(newProfile.getPercentControl());
        }
        else {
            retVal.setPercentControl(existing.getPercentControl());
        }

        if (newProfile.getCompoundConcentration() != null) {
            retVal.setCompoundConcentration(newProfile.getCompoundConcentration());
        }
        else {
            retVal.setCompoundConcentration(existing.getCompoundConcentration());
        }

        return retVal;
    }

    /**
     * Creates and returns a new activity profile that is essentially a patch of {@code newProfile}'s fields into
     * {@code existing}.  That is, the returned activity profile will have any non-{@code null} property values
     * from the new profile, and for any {@code null} property values, it will have the existing profile's
     * values.
     *
     * @param existing The existing activity profile.
     * @param kdCsvRecord The new activity profile, whose non-{@code null}/empty values should be merged into
     *        the result.
     * @return The result of the patch/merge operation.
     */
    public static ActivityProfile patchActivityProfile(ActivityProfile existing, KdCsvRecord kdCsvRecord) {

        ActivityProfile retVal = new ActivityProfile();
        retVal.setId(existing.getId());

        // These fields aren't in Kd files, so keep existing values
        retVal.setPercentControl(existing.getPercentControl());
        retVal.setCompoundConcentration(existing.getCompoundConcentration());

        // It's assumed that the two activity profiles having the same compound and kinase was previously verified
        retVal.setCompoundName(existing.getCompoundName());
        retVal.setKinase(existing.getKinase());

        if (kdCsvRecord.getKd() != null) {
            retVal.setKd(kdCsvRecord.getKd());
        }
        else {
            retVal.setKd(existing.getKd());
        }

        return retVal;
    }

    /**
     * Creates and returns a new compound that is essentially a patch of {@code newCompound}'s fields into
     * {@code existing}.  That is, the returned compound will have any non-{@code null} property values
     * from the new compound, and for any {@code null} property values, it will have the existing compound's
     * values.
     *
     * @param existing The existing compound.
     * @param newCompound The new compound, whose non-{@code null}/empty values should be merged into
     *        the result.
     * @return The result of the patch/merge operation.
     */
    public static Compound patchCompound(Compound existing, Compound newCompound) {

        Compound retVal = new Compound();

        // It's assumed that the two compounds having the same name was previously verified
        retVal.setCompoundName(existing.getCompoundName());

        if (StringUtils.isNotBlank(newCompound.getChemotype())) {
            retVal.setChemotype(newCompound.getChemotype());
        }
        else {
            retVal.setChemotype(existing.getChemotype());
        }

        if (newCompound.getS10() != null) {
            retVal.setS10(newCompound.getS10());
        }
        else {
            retVal.setS10(existing.getS10());
        }

        if (StringUtils.isNotBlank(newCompound.getSmiles())) {
            retVal.setSmiles(newCompound.getSmiles());
        }
        else {
            retVal.setSmiles(existing.getSmiles());
        }

        if (StringUtils.isNotBlank(newCompound.getSource())) {
            retVal.setSource(newCompound.getSource());
        }
        else {
            retVal.setSource(existing.getSource());
        }

        if (StringUtils.isNotBlank(newCompound.getPrimaryReference())) {
            retVal.setPrimaryReference(newCompound.getPrimaryReference());
        }
        else {
            retVal.setPrimaryReference(existing.getPrimaryReference());
        }

        if (StringUtils.isNotBlank(newCompound.getPrimaryReferenceUrl())) {
            retVal.setPrimaryReferenceUrl(newCompound.getPrimaryReferenceUrl());
        }
        else {
            retVal.setPrimaryReferenceUrl(existing.getPrimaryReferenceUrl());
        }

        retVal.setHidden(newCompound.isHidden() != null ? newCompound.isHidden() :
            existing.isHidden());

        return retVal;
    }

    private static void possiblyAddDifference(List<String> differences, String fieldName, Object oldValue,
                                                Object newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            differences.add(String.format("%s: '%s' -> '%s'", fieldName, String.valueOf(oldValue),
                String.valueOf(newValue)));
        }
    }

    /**
     * Sanitizes a string to be a file name, or part of a file name.  Useful for
     * file downloads, for example.
     *
     * @param str The string to sanitize.
     * @return The sanitized version of the string.
     */
    public static String sanitizeForFileName(String str) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            sb.append(isValidFileNameChar(ch) ? ch : '_');
        }

        return sb.toString();
    }

    /**
     * Converts a CSV record from an S-Scores CSV file from Discoverx into a {@code Compound}.  Note that since most
     * of the fields in the CSV record are not included in our data model, the returned {@code Compound} will be
     * sparse.
     *
     * @param rep The CSV record.
     * @return The {@code Compound}.
     */
    public static Compound sScoreCsvRecordToCompound(SScoreCsvRecord rep) {
        Compound compound = new Compound();
        compound.setCompoundName(rep.getCompoundName());
        compound.setS10(rep.getSelectivityScore());
        return compound;
    }
}
