package org.sgc.rak.util;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.KinaseActivityProfile;
import org.sgc.rak.reps.KinaseActivityProfileCsvRecordRep;
import org.sgc.rak.reps.ObjectImportRep;

/**
 * Obligatory utility methods.
 */
public final class Util {

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
    public static void convertEmptyStringsToNulls(KinaseActivityProfileCsvRecordRep activityProfile) {

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
    public static KinaseActivityProfile patchActivityProfile(KinaseActivityProfile existing,
                                                             KinaseActivityProfileCsvRecordRep newProfile) {

        KinaseActivityProfile retVal = new KinaseActivityProfile();
        retVal.setId(existing.getId());

        // It's assumed that the two activity profiles having the same compound and kinase was previously verified
        retVal.setCompoundName(existing.getCompoundName());
        retVal.setKinase(existing.getKinase());

        if (newProfile.getPercentControl() > 0) {
            retVal.setPercentControl(newProfile.getPercentControl());
        }
        else {
            retVal.setPercentControl(existing.getPercentControl());
        }

        if (newProfile.getCompoundConcentration() > 0) {
            retVal.setCompoundConcentration(newProfile.getCompoundConcentration());
        }
        else {
            retVal.setCompoundConcentration(existing.getCompoundConcentration());
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

        if (newCompound.getS10() != null) {
            retVal.setS10(newCompound.getS10());
        }
        else {
            retVal.setS10(existing.getS10());
        }

        return retVal;
    }
}
