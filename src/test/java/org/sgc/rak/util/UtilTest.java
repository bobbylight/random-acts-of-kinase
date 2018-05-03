package org.sgc.rak.util;

import org.junit.Assert;
import org.junit.Test;
import org.sgc.rak.model.Compound;
import org.sgc.rak.reps.KinaseActivityProfileCsvRecordRep;

public class UtilTest {

    @Test
    public void testConvertEmptyStringsToNulls_activityProfile() {

        KinaseActivityProfileCsvRecordRep activityProfileCsvRecordRep = new KinaseActivityProfileCsvRecordRep();
        activityProfileCsvRecordRep.setDiscoverxGeneSymbol("");

        Util.convertEmptyStringsToNulls(activityProfileCsvRecordRep);
        Assert.assertNull(activityProfileCsvRecordRep.getDiscoverxGeneSymbol());
    }

    @Test
    public void testConvertEmptyStringsToNulls_compound() {

        Compound compound = new Compound();
        compound.setChemotype("");
        compound.setSmiles(" ");
        compound.setSource("  ");

        Util.convertEmptyStringsToNulls(compound);
        Assert.assertNull(compound.getChemotype());
        Assert.assertNull(compound.getSmiles());
        Assert.assertNull(compound.getSource());
    }

    @Test
    public void testPatchCompound_nonNullValuesOverwritePriorValues() {

        Compound existing = new Compound();
        existing.setCompoundName("compoundA");
        existing.setChemotype("a");
        existing.setSmiles("a");
        existing.setSource("a");
        existing.setS10(0.3);

        Compound newCompound = new Compound();
        newCompound.setCompoundName("compoundA");
        newCompound.setChemotype("b");
        newCompound.setSmiles("b");
        newCompound.setSource("b");
        newCompound.setS10(0.4);

        Compound result = Util.patchCompound(existing, newCompound);

        Assert.assertEquals("compoundA", result.getCompoundName());
        Assert.assertEquals("b", result.getChemotype());
        Assert.assertEquals("b", result.getSmiles());
        Assert.assertEquals("b", result.getSource());
        Assert.assertEquals(0.4, result.getS10(), 0.01);
    }

    @Test
    public void testPatchCompound_nullValuesDontOverwriteNonNullValues() {

        Compound existing = new Compound();
        existing.setCompoundName("compoundA");
        existing.setChemotype("a");
        existing.setSmiles("a");
        existing.setSource("a");
        existing.setS10(0.3);

        Compound newCompound = new Compound();
        newCompound.setCompoundName("compoundA");

        Compound result = Util.patchCompound(existing, newCompound);

        Assert.assertEquals("compoundA", result.getCompoundName());
        Assert.assertEquals("a", result.getChemotype());
        Assert.assertEquals("a", result.getSmiles());
        Assert.assertEquals("a", result.getSource());
        Assert.assertEquals(0.3, result.getS10(), 0.01);
    }
}
