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
     * Updates all values in {@code existing} with their corresponding values in {@code newCompound},
     * as long as the new values are not {@code null} or empty.  This is essentially a patch operation.
     * The compound name is not updated, and is assumed to be the same between the two.
     *
     * @param existing The existing compound to update.
     * @param newCompound The new compound, whose non-{@code null}/empty values should be merged into
     *        {@code existing}.
     * @return {@code existing} is returned.
     */
    public static Compound updateNotNullValues(Compound existing, Compound newCompound) {

        if (StringUtils.isNotBlank(newCompound.getChemotype())) {
            existing.setChemotype(newCompound.getChemotype());
        }

        if (StringUtils.isNotBlank(newCompound.getSmiles())) {
            existing.setSmiles(newCompound.getSmiles());
        }

        if (StringUtils.isNotBlank(newCompound.getSource())) {
            existing.setSource(newCompound.getSource());
        }

        if (newCompound.getS10() != null) {
            existing.setS10(newCompound.getS10());
        }

        return existing;
    }
}
