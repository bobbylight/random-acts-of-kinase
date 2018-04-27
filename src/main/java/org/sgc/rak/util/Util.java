package org.sgc.rak.util;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.model.Compound;

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
