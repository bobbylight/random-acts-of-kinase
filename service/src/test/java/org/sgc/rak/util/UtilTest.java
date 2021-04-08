package org.sgc.rak.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sgc.rak.model.*;
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.KdCsvRecord;
import org.sgc.rak.model.csv.NanoBretActivityProfileCsvRecord;
import org.sgc.rak.model.csv.SScoreCsvRecord;
import org.sgc.rak.reps.ObjectImportRep;

import java.util.Date;

public class UtilTest {

    @Test
    public void testConvertEmptyStringsToNulls_activityProfile() {

        ActivityProfileCsvRecord activityProfileCsvRecord = new ActivityProfileCsvRecord();
        activityProfileCsvRecord.setDiscoverxGeneSymbol("");

        Util.convertEmptyStringsToNulls(activityProfileCsvRecord);
        Assertions.assertNull(activityProfileCsvRecord.getDiscoverxGeneSymbol());
    }

    @Test
    public void testConvertEmptyStringsToNulls_compound() {

        Compound compound = new Compound();
        compound.setChemotype("");
        compound.setSmiles(" ");
        compound.setSource("  ");

        Util.convertEmptyStringsToNulls(compound);
        Assertions.assertNull(compound.getChemotype());
        Assertions.assertNull(compound.getSmiles());
        Assertions.assertNull(compound.getSource());
    }

    @Test
    public void testConvertEmptyStringsToNulls_kdCsvRecord() {

        KdCsvRecord kd = new KdCsvRecord();
        kd.setDiscoverxGeneSymbol(" ");
        kd.setEntrezGeneSymbol("  ");
        kd.setModifier("");

        Util.convertEmptyStringsToNulls(kd);
        Assertions.assertNull(kd.getDiscoverxGeneSymbol());
        Assertions.assertNull(kd.getEntrezGeneSymbol());
        Assertions.assertNull(kd.getModifier());
    }

    @Test
    public void testConvertEmptyStringsToNulls_nanoBretActivityProfile() {

        NanoBretActivityProfileCsvRecord csvRecord = new NanoBretActivityProfileCsvRecord();
        csvRecord.setDiscoverxGeneSymbol("");
        csvRecord.setNlucOrientation("");
        csvRecord.setComment("");
        csvRecord.setDate("");

        Util.convertEmptyStringsToNulls(csvRecord);
        Assertions.assertNull(csvRecord.getDiscoverxGeneSymbol());
        Assertions.assertNull(csvRecord.getNlucOrientation());
        Assertions.assertNull(csvRecord.getComment());
        Assertions.assertNull(csvRecord.getDate());
    }

    @Test
    public void testCreateFieldStatus() {
        ObjectImportRep.FieldStatus status = Util.createFieldStatus("name", "newValue", "oldValue");
        Assertions.assertEquals("name", status.getFieldName());
        Assertions.assertEquals("newValue", status.getNewValue());
        Assertions.assertEquals("oldValue", status.getOldValue());
    }

    @Test
    public void testEscapeForLike() {
        Assertions.assertEquals("foo", Util.escapeForLike("foo"));
        Assertions.assertEquals("54\\% \\_", Util.escapeForLike("54% _"));
    }

    @Test
    public void testNanoBretCsvDateToRealDate_happyPath() throws Exception {
        Date date = Util.createNanoBretDataDateFormat().parse("81_02_06");
        Assertions.assertEquals(date, Util.nanoBretCsvDateToRealDate("1981_02_06"));
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
        Assertions.assertEquals(7, result.getCompoundConcentration().intValue());
        Assertions.assertEquals(3.7, result.getPercentControl(), 0.001);

        // Kd doesn't get cleared out just because it isn't in ActivityProfileCsvRecord
        Assertions.assertEquals(4.2, result.getKd(), 0.001);
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
        Assertions.assertEquals(0, result.getCompoundConcentration().intValue());
        Assertions.assertEquals(0, result.getPercentControl(), 0.001);

        // Kd doesn't get cleared out just because it isn't in ActivityProfileCsvRecord
        Assertions.assertEquals(4.2, result.getKd(), 0.001);
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
        Assertions.assertEquals(3, result.getCompoundConcentration().intValue());
        Assertions.assertEquals(0.3, result.getPercentControl(), 0.001);

        // Kd doesn't get cleared out just because it isn't in ActivityProfileCsvRecord
        Assertions.assertEquals(4.2, result.getKd(), 0.001);
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
        Assertions.assertEquals(12.1, result.getKd(), 0.001);

        // These fields don't get cleared out just because they aren't in KdCsvRecord
        Assertions.assertEquals(3, result.getCompoundConcentration().intValue());
        Assertions.assertEquals(0.3, result.getPercentControl(), 0.001);
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
        Assertions.assertEquals(4.2, result.getKd(), 0.001);

        // These fields don't get cleared out just because they aren't in KdCsvRecord
        Assertions.assertEquals(3, result.getCompoundConcentration().intValue());
        Assertions.assertEquals(0.3, result.getPercentControl(), 0.001);
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

        Assertions.assertEquals("compoundA", result.getCompoundName());
        Assertions.assertEquals("b", result.getChemotype());
        Assertions.assertEquals("b", result.getSmiles());
        Assertions.assertEquals("b", result.getSource());
        Assertions.assertEquals(0.4, result.getS10(), 0.01);
        Assertions.assertEquals("referenceB", result.getPrimaryReference());
        Assertions.assertEquals("urlB", result.getPrimaryReferenceUrl());
        Assertions.assertFalse(result.isHidden());
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

        Assertions.assertEquals("compoundA", result.getCompoundName());
        Assertions.assertEquals("a", result.getChemotype());
        Assertions.assertEquals("a", result.getSmiles());
        Assertions.assertEquals("a", result.getSource());
        Assertions.assertEquals(0.3, result.getS10(), 0.01);
        Assertions.assertEquals("referenceA", result.getPrimaryReference());
        Assertions.assertEquals("urlA", result.getPrimaryReferenceUrl());
        Assertions.assertTrue(result.isHidden());
    }

    @Test
    public void testPatchNanoBretActivityProfile_nonNullValuesOverwritePriorValues() {

        Kinase existingKinase = new Kinase();
        existingKinase.setId(3);
        existingKinase.setDiscoverxGeneSymbol("existingDiscoverx");

        Date date = new Date(0);
        NanoBretActivityProfile existing = TestUtil.createNanoBretActivityProfile("compoundA", date,
            "commentA", "nlucA", 0d, 0, "discoverxA", "entrezA",
            NanoBretActivityProfileModifier.GREATER_THAN, 0d, 0);

        NanoBretActivityProfileCsvRecord newProfile = new NanoBretActivityProfileCsvRecord();
        newProfile.setComment("commentNew");
        newProfile.setCompoundConcentration(4);
        newProfile.setCompoundName("compoundA");
        newProfile.setDate(Util.realDateToNanoBretCsvDate(date));
        newProfile.setDiscoverxGeneSymbol("existingDiscoverx");
        newProfile.setIc50(50d);
        newProfile.setNlucOrientation("nlucNew");
        newProfile.setPercentInhibition(50d);
        newProfile.setPoints(50);

        NanoBretActivityProfile result = Util.patchNanoBretActivityProfile(existing, newProfile);

        // New values for these fields overwrite prior values
        Assertions.assertEquals("commentNew", result.getComment());
        Assertions.assertEquals(4, result.getConcentration().intValue());
        Assertions.assertEquals(date, result.getDate());
        Assertions.assertEquals(50d, result.getIc50(), 0.0001);
        Assertions.assertEquals("nlucNew", result.getNlucOrientation());
        Assertions.assertEquals(50d, result.getPercentInhibition(), 0.0001);
        Assertions.assertEquals(50, result.getPoints().intValue());
    }

    @Test
    public void testPatchNanoBretActivityProfile_nullValuesDontOverwriteNonNullValues() {

        Kinase existingKinase = new Kinase();
        existingKinase.setId(3);
        existingKinase.setDiscoverxGeneSymbol("existingDiscoverx");

        Date date = new Date(0);
        NanoBretActivityProfile existing = TestUtil.createNanoBretActivityProfile("compoundA", date,
            "commentA", "nlucA", 1d, 1, "discoverxA", "entrezA",
            NanoBretActivityProfileModifier.GREATER_THAN, 1d, 1);

        NanoBretActivityProfileCsvRecord newProfile = new NanoBretActivityProfileCsvRecord();
        newProfile.setCompoundName("compoundA");
        newProfile.setDate(Util.realDateToNanoBretCsvDate(date));
        newProfile.setDiscoverxGeneSymbol("existingDiscoverx");

        NanoBretActivityProfile result = Util.patchNanoBretActivityProfile(existing, newProfile);

        // New values for these fields overwrite prior values
        Assertions.assertEquals("commentA", result.getComment());
        Assertions.assertEquals(1, result.getConcentration().intValue());
        Assertions.assertEquals(date, result.getDate());
        Assertions.assertEquals(1d, result.getIc50(), 0.0001);
        Assertions.assertEquals("nlucA", result.getNlucOrientation());
        Assertions.assertEquals(1d, result.getPercentInhibition(), 0.0001);
        Assertions.assertEquals(1, result.getPoints().intValue());
    }

    @Test
    public void testSanitizeForFileName() {
        String fileName = "abcdefghijlmnopqrstuvwxyz-_/\\ ()";
        Assertions.assertEquals("abcdefghijlmnopqrstuvwxyz-___ __", Util.sanitizeForFileName(fileName));
    }

    @Test
    public void testSScoreCsvRecordToCompound() {

        SScoreCsvRecord rep = new SScoreCsvRecord();
        rep.setCompoundName("compoundA");
        rep.setSelectivityScore(0.3);

        Compound compound = Util.sScoreCsvRecordToCompound(rep);
        Assertions.assertEquals(rep.getCompoundName(), compound.getCompoundName());
        Assertions.assertEquals(rep.getSelectivityScore(), compound.getS10());
    }
}
