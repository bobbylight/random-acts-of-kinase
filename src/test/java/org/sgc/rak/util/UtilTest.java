package org.sgc.rak.util;

import org.junit.Assert;
import org.junit.Test;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.SScoreCsvRecord;
import org.sgc.rak.reps.ObjectImportRep;

public class UtilTest {

    @Test
    public void testConvertEmptyStringsToNulls_activityProfile() {

        ActivityProfileCsvRecord activityProfileCsvRecord = new ActivityProfileCsvRecord();
        activityProfileCsvRecord.setDiscoverxGeneSymbol("");

        Util.convertEmptyStringsToNulls(activityProfileCsvRecord);
        Assert.assertNull(activityProfileCsvRecord.getDiscoverxGeneSymbol());
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
    public void testCreateFieldStatus() {
        ObjectImportRep.FieldStatus status = Util.createFieldStatus("name", "newValue", "oldValue");
        Assert.assertEquals("name", status.getFieldName());
        Assert.assertEquals("newValue", status.getNewValue());
        Assert.assertEquals("oldValue", status.getOldValue());
    }

    @Test
    public void testPatchActivityProfile_nonNullValuesOverwritePriorValues() {

        Kinase existingKinase = new Kinase();
        existingKinase.setId(3);
        existingKinase.setDiscoverxGeneSymbol("existingDiscoverx");

        ActivityProfile existing = new ActivityProfile();
        existing.setId(42L);
        existing.setCompoundConcentration(3);
        existing.setCompoundName("compoundA");
        existing.setKd(4.2);
        existing.setKinase(existingKinase);
        existing.setPercentControl(0.3);

        ActivityProfileCsvRecord newProfile = new ActivityProfileCsvRecord();
        newProfile.setCompoundConcentration(7);
        newProfile.setCompoundName(existing.getCompoundName()); // It'a assumed these were already found to match
        newProfile.setDiscoverxGeneSymbol(existing.getKinase().getDiscoverxGeneSymbol()); // Ditto
        newProfile.setPercentControl(3.7);

        ActivityProfile result = Util.patchActivityProfile(existing, newProfile);
        Assert.assertEquals(7, result.getCompoundConcentration().intValue());
        Assert.assertEquals(3.7, result.getPercentControl(), 0.001);
    }

    @Test
    public void testPatchActivityProfile_nullValuesDontOverwriteNonNullValues() {

        Kinase existingKinase = new Kinase();
        existingKinase.setId(3);
        existingKinase.setDiscoverxGeneSymbol("existingDiscoverx");

        ActivityProfile existing = new ActivityProfile();
        existing.setId(42L);
        existing.setCompoundConcentration(3);
        existing.setCompoundName("compoundA");
        existing.setKd(4.2);
        existing.setKinase(existingKinase);
        existing.setPercentControl(0.3);

        ActivityProfileCsvRecord newProfile = new ActivityProfileCsvRecord();
        newProfile.setCompoundName(existing.getCompoundName()); // It'a assumed these were already found to match
        newProfile.setDiscoverxGeneSymbol(existing.getKinase().getDiscoverxGeneSymbol()); // Ditto

        ActivityProfile result = Util.patchActivityProfile(existing, newProfile);
        Assert.assertEquals(3, result.getCompoundConcentration().intValue());
        Assert.assertEquals(0.3, result.getPercentControl(), 0.001);
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

    @Test
    public void testSScoreCsvRecordToCompound() {

        SScoreCsvRecord rep = new SScoreCsvRecord();
        rep.setCompoundName("compoundA");
        rep.setSelectivityScore(0.3);

        Compound compound = Util.sScoreCsvRecordToCompound(rep);
        Assert.assertEquals(rep.getCompoundName(), compound.getCompoundName());
        Assert.assertEquals(rep.getSelectivityScore(), compound.getS10());
    }
}
