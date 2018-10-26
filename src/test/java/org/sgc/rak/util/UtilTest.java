package org.sgc.rak.util;

import org.junit.Assert;
import org.junit.Test;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.KdCsvRecord;
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
    public void testConvertEmptyStringsToNulls_kdCsvRecord() {

        KdCsvRecord kd = new KdCsvRecord();
        kd.setDiscoverxGeneSymbol(" ");
        kd.setEntrezGeneSymbol("  ");
        kd.setModifier("");

        Util.convertEmptyStringsToNulls(kd);
        Assert.assertNull(kd.getDiscoverxGeneSymbol());
        Assert.assertNull(kd.getEntrezGeneSymbol());
        Assert.assertNull(kd.getModifier());
    }

    @Test
    public void testCreateFieldStatus() {
        ObjectImportRep.FieldStatus status = Util.createFieldStatus("name", "newValue", "oldValue");
        Assert.assertEquals("name", status.getFieldName());
        Assert.assertEquals("newValue", status.getNewValue());
        Assert.assertEquals("oldValue", status.getOldValue());
    }

    @Test
    public void testEscapeForLike() {
        Assert.assertEquals("foo", Util.escapeForLike("foo"));
        Assert.assertEquals("54\\% \\_", Util.escapeForLike("54% _"));
    }

    @Test
    public void testPatchActivityProfile_activityProfileCsvRecord_nonNullValuesOverwritePriorValues() {

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

        // New values for these fields overwrite prior values
        Assert.assertEquals(7, result.getCompoundConcentration().intValue());
        Assert.assertEquals(3.7, result.getPercentControl(), 0.001);

        // Kd doesn't get cleared out just because it isn't in ActivityProfileCsvRecord
        Assert.assertEquals(4.2, result.getKd(), 0.001);
    }

    @Test
    public void testPatchActivityProfile_activityProfileCsvRecord_zeroForNumericValuesStillTake() {

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
        newProfile.setCompoundConcentration(0); // Ensure 0 is inserted
        newProfile.setCompoundName(existing.getCompoundName()); // It'a assumed these were already found to match
        newProfile.setDiscoverxGeneSymbol(existing.getKinase().getDiscoverxGeneSymbol()); // Ditto
        newProfile.setPercentControl(0d); // Ensure 0 is inserted

        ActivityProfile result = Util.patchActivityProfile(existing, newProfile);

        // New values for these fields overwrite prior values
        Assert.assertEquals(0, result.getCompoundConcentration().intValue());
        Assert.assertEquals(0, result.getPercentControl(), 0.001);

        // Kd doesn't get cleared out just because it isn't in ActivityProfileCsvRecord
        Assert.assertEquals(4.2, result.getKd(), 0.001);
    }

    @Test
    public void testPatchActivityProfile_activityProfileCsvRecord_nullValuesDontOverwriteNonNullValues() {

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

        // Original values preserved because they were null in newProfile
        Assert.assertEquals(3, result.getCompoundConcentration().intValue());
        Assert.assertEquals(0.3, result.getPercentControl(), 0.001);

        // Kd doesn't get cleared out just because it isn't in ActivityProfileCsvRecord
        Assert.assertEquals(4.2, result.getKd(), 0.001);
    }

    @Test
    public void testPatchActivityProfile_kdCsvRecord_nonNullValuesOverwritePriorValues() {

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

        KdCsvRecord newKd = new KdCsvRecord();
        newKd.setCompoundName(existing.getCompoundName()); // It'a assumed these were already found to match
        newKd.setDiscoverxGeneSymbol(existing.getKinase().getDiscoverxGeneSymbol()); // Ditto
        newKd.setEntrezGeneSymbol(existing.getKinase().getEntrezGeneSymbol()); // Ditto
        newKd.setKd(12.1);
        newKd.setModifier("=");

        ActivityProfile result = Util.patchActivityProfile(existing, newKd);

        // New values for these fields overwrite prior values
        Assert.assertEquals(12.1, result.getKd(), 0.001);

        // These fields don't get cleared out just because they aren't in KdCsvRecord
        Assert.assertEquals(3, result.getCompoundConcentration().intValue());
        Assert.assertEquals(0.3, result.getPercentControl(), 0.001);
    }

    @Test
    public void testPatchActivityProfile_kdCsvRecord_nullValuesDontOverwriteNonNullValues() {

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

        KdCsvRecord newKd = new KdCsvRecord();
        newKd.setCompoundName(existing.getCompoundName()); // It'a assumed these were already found to match
        newKd.setDiscoverxGeneSymbol(existing.getKinase().getDiscoverxGeneSymbol()); // Ditto
        newKd.setEntrezGeneSymbol(existing.getKinase().getEntrezGeneSymbol()); // Ditto

        ActivityProfile result = Util.patchActivityProfile(existing, newKd);

        // Original values preserved because they were null in newKd
        Assert.assertEquals(4.2, result.getKd(), 0.001);

        // These fields don't get cleared out just because they aren't in KdCsvRecord
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
        existing.setPrimaryReference("referenceA");
        existing.setPrimaryReferenceUrl("urlA");
        existing.setHidden(true);

        Compound newCompound = new Compound();
        newCompound.setCompoundName("compoundA");
        newCompound.setChemotype("b");
        newCompound.setSmiles("b");
        newCompound.setSource("b");
        newCompound.setS10(0.4);
        newCompound.setPrimaryReference("referenceB");
        newCompound.setPrimaryReferenceUrl("urlB");
        newCompound.setHidden(false);

        Compound result = Util.patchCompound(existing, newCompound);

        Assert.assertEquals("compoundA", result.getCompoundName());
        Assert.assertEquals("b", result.getChemotype());
        Assert.assertEquals("b", result.getSmiles());
        Assert.assertEquals("b", result.getSource());
        Assert.assertEquals(0.4, result.getS10(), 0.01);
        Assert.assertEquals("referenceB", result.getPrimaryReference());
        Assert.assertEquals("urlB", result.getPrimaryReferenceUrl());
        Assert.assertFalse(result.isHidden());
    }

    @Test
    public void testPatchCompound_nullValuesDontOverwriteNonNullValues() {

        Compound existing = new Compound();
        existing.setCompoundName("compoundA");
        existing.setChemotype("a");
        existing.setSmiles("a");
        existing.setSource("a");
        existing.setS10(0.3);
        existing.setPrimaryReference("referenceA");
        existing.setPrimaryReferenceUrl("urlA");
        existing.setHidden(true);

        Compound newCompound = new Compound();
        newCompound.setCompoundName("compoundA");

        Compound result = Util.patchCompound(existing, newCompound);

        Assert.assertEquals("compoundA", result.getCompoundName());
        Assert.assertEquals("a", result.getChemotype());
        Assert.assertEquals("a", result.getSmiles());
        Assert.assertEquals("a", result.getSource());
        Assert.assertEquals(0.3, result.getS10(), 0.01);
        Assert.assertEquals("referenceA", result.getPrimaryReference());
        Assert.assertEquals("urlA", result.getPrimaryReferenceUrl());
        Assert.assertTrue(result.isHidden());
    }

    @Test
    public void testSanitizeForFileName() {
        String fileName = "abcdefghijlmnopqrstuvwxyz-_/\\ ()";
        Assert.assertEquals("abcdefghijlmnopqrstuvwxyz-___ __", Util.sanitizeForFileName(fileName));
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
