package org.sgc.rak.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.dao.NanoBretActivityProfileDao;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.NanoBretActivityProfile;
import org.sgc.rak.model.NanoBretActivityProfileModifier;
import org.sgc.rak.model.csv.NanoBretActivityProfileCsvRecord;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.util.TestUtil;
import org.sgc.rak.util.Util;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

public class NanoBretActivityProfileServiceTest {

    @Mock
    private NanoBretActivityProfileDao nanoBretActivityProfileDao;

    @Mock
    private CompoundService compoundService;

    @Mock
    private KinaseService kinaseService;

    @Mock
    private Messages messages;

    @InjectMocks
    private NanoBretActivityProfileService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testImportNanoBretActivityProfiles_happyPath_commit() {
        testImportNanoBretActivityProfiles_happyPath_impl(true);
    }

    @Test
    public void testImportNanoBretActivityProfiles_happyPath_noCommit() {
        testImportNanoBretActivityProfiles_happyPath_impl(false);
    }

    private void testImportNanoBretActivityProfiles_happyPath_impl(boolean commit) {

        List<NanoBretActivityProfileCsvRecord> records = Arrays.asList(
            TestUtil.createNanoBretActivityProfileCsvRecord("compoundA", "19_01_01",
                "commentNew", "nlucNew", 60d, 60, "discoverxA",
                NanoBretActivityProfileModifier.EQUAL_TO, 99,99),
            TestUtil.createNanoBretActivityProfileCsvRecord("compoundB", "19_02_02",
                "commentB", "nlucB", 50d, 60, "discoverxB",
                NanoBretActivityProfileModifier.GREATER_THAN, 1, 1)
        );

        Set<NanoBretActivityProfile> existingProfiles = new HashSet<>();
        Date compoundADate = Util.nanoBretCsvDateToRealDate("19_01_01");
        existingProfiles.add(TestUtil.createNanoBretActivityProfile("compoundA", compoundADate,
            "commentA", "nlucA", 0d, 0, "discoverxA", "entrezA",
            NanoBretActivityProfileModifier.GREATER_THAN, 1, 1));
        doReturn(existingProfiles).when(nanoBretActivityProfileDao).getNanoBretActivityProfiles(any(), any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(true).when(compoundService).getCompoundExists(anyString());
        Kinase kinase = TestUtil.createKinase("discoverxA", "entrezA");
        doReturn(kinase).when(kinaseService).getKinaseByDiscoverx(eq(kinase.getDiscoverxGeneSymbol()));
        kinase = TestUtil.createKinase("discoverxB", "entrezB");
        doReturn(kinase).when(kinaseService).getKinaseByDiscoverx(eq(kinase.getDiscoverxGeneSymbol()));

        ObjectImportRep importRep = service.importNanoBretActivityProfiles(records, commit);
        List<List<ObjectImportRep.FieldStatus>> fieldStatuses = importRep.getFieldStatuses();
        Assert.assertEquals(2, fieldStatuses.size());

        // Verify that the first response row shows properly merged values
        List<ObjectImportRep.FieldStatus> rowData = fieldStatuses.get(0);
        Assert.assertEquals("date", rowData.get(0).getFieldName());
        Assert.assertEquals("19_01_01", rowData.get(0).getNewValue());
        Assert.assertEquals("19_01_01", rowData.get(0).getOldValue());
        Assert.assertEquals("comment", rowData.get(1).getFieldName());
        Assert.assertEquals("commentNew", rowData.get(1).getNewValue());
        Assert.assertEquals("commentA", rowData.get(1).getOldValue());
        Assert.assertEquals("compoundName", rowData.get(2).getFieldName());
        Assert.assertEquals("compoundA", rowData.get(2).getNewValue());
        Assert.assertEquals("compoundA", rowData.get(2).getOldValue());
        Assert.assertEquals("concentration", rowData.get(3).getFieldName());
        Assert.assertEquals(60, rowData.get(3).getNewValue());
        Assert.assertEquals(0, rowData.get(3).getOldValue());
        Assert.assertEquals("ic50", rowData.get(4).getFieldName());
        Assert.assertEquals(60d, rowData.get(4).getNewValue());
        Assert.assertEquals(0d, rowData.get(4).getOldValue());
        Assert.assertEquals("discoverxGeneSymbol", rowData.get(5).getFieldName());
        Assert.assertEquals("discoverxA", rowData.get(5).getNewValue());
        Assert.assertEquals("discoverxA", rowData.get(5).getOldValue());
        Assert.assertEquals("modifier", rowData.get(6).getFieldName());
        Assert.assertEquals(NanoBretActivityProfileModifier.EQUAL_TO, rowData.get(6).getNewValue());
        Assert.assertEquals(NanoBretActivityProfileModifier.GREATER_THAN, rowData.get(6).getOldValue());
        Assert.assertEquals("nlucOrientation", rowData.get(7).getFieldName());
        Assert.assertEquals("nlucNew", rowData.get(7).getNewValue());
        Assert.assertEquals("nlucA", rowData.get(7).getOldValue());
        Assert.assertEquals("percentInhibition", rowData.get(8).getFieldName());
        Assert.assertEquals(99d, rowData.get(8).getNewValue());
        Assert.assertEquals(1d, rowData.get(8).getOldValue());
        Assert.assertEquals("points", rowData.get(9).getFieldName());
        Assert.assertEquals(99, rowData.get(9).getNewValue());
        Assert.assertEquals(1, rowData.get(9).getOldValue());

        // Verify that the second response row shows all new values
        rowData = fieldStatuses.get(1);
        Assert.assertEquals("date", rowData.get(0).getFieldName());
        Assert.assertEquals("19_02_02", rowData.get(0).getNewValue());
        Assert.assertNull(rowData.get(0).getOldValue());
        Assert.assertEquals("comment", rowData.get(1).getFieldName());
        Assert.assertEquals("commentB", rowData.get(1).getNewValue());
        Assert.assertNull(rowData.get(1).getOldValue());
        Assert.assertEquals("compoundName", rowData.get(2).getFieldName());
        Assert.assertEquals("compoundB", rowData.get(2).getNewValue());
        Assert.assertNull(rowData.get(2).getOldValue());
        Assert.assertEquals("concentration", rowData.get(3).getFieldName());
        Assert.assertEquals(60, rowData.get(3).getNewValue());
        Assert.assertNull(rowData.get(3).getOldValue());
        Assert.assertEquals("ic50", rowData.get(4).getFieldName());
        Assert.assertEquals(50d, rowData.get(4).getNewValue());
        Assert.assertNull(rowData.get(4).getOldValue());
        Assert.assertEquals("discoverxGeneSymbol", rowData.get(5).getFieldName());
        Assert.assertEquals("discoverxB", rowData.get(5).getNewValue());
        Assert.assertNull(rowData.get(5).getOldValue());
        Assert.assertEquals("modifier", rowData.get(6).getFieldName());
        Assert.assertEquals(NanoBretActivityProfileModifier.GREATER_THAN, rowData.get(6).getNewValue());
        Assert.assertNull(rowData.get(6).getOldValue());
        Assert.assertEquals("nlucOrientation", rowData.get(7).getFieldName());
        Assert.assertEquals("nlucB", rowData.get(7).getNewValue());
        Assert.assertNull(rowData.get(7).getOldValue());
        Assert.assertEquals("percentInhibition", rowData.get(8).getFieldName());
        Assert.assertEquals(1d, rowData.get(8).getNewValue());
        Assert.assertNull(rowData.get(8).getOldValue());
        Assert.assertEquals("points", rowData.get(9).getFieldName());
        Assert.assertEquals(1, rowData.get(9).getNewValue());
        Assert.assertNull(rowData.get(9).getOldValue());

        // Verify the commit only happens if commit == true
        verify(nanoBretActivityProfileDao, times(commit ? 1 : 0)).save(any());
    }

    @Test(expected = BadRequestException.class)
    public void testImportNanoBretActivityProfiles_error_unknownCompound() throws BadRequestException {

        // compound name "unknown" doesn't match any existing compounds
        List<NanoBretActivityProfileCsvRecord> records = Collections.singletonList(
            TestUtil.createNanoBretActivityProfileCsvRecord("unknown", "19_01_01",
                "commentNew", "nlucNew", 60d, 60, "discoverxA",
                NanoBretActivityProfileModifier.EQUAL_TO, 99,99)
        );

        Set<NanoBretActivityProfile> existingProfiles = new HashSet<>();
        Date compoundADate = Util.nanoBretCsvDateToRealDate("19_01_01");
        existingProfiles.add(TestUtil.createNanoBretActivityProfile("compoundA", compoundADate,
            "commentA", "nlucA", 0d, 0, "discoverxA", "entrezA",
            NanoBretActivityProfileModifier.GREATER_THAN, 1, 1));
        doReturn(existingProfiles).when(nanoBretActivityProfileDao).getNanoBretActivityProfiles(any(), any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(false).when(compoundService).getCompoundExists(anyString());
        Kinase kinase = TestUtil.createKinase("discoverxA", "entrezA");
        doReturn(Collections.singletonList(kinase)).when(kinaseService).getKinase(eq(kinase.getEntrezGeneSymbol()));

        service.importNanoBretActivityProfiles(records, true);
    }

    @Test(expected = BadRequestException.class)
    public void testImportNanoBretActivityProfiles_error_unknownKinase() throws BadRequestException {

        List<NanoBretActivityProfileCsvRecord> records = Collections.singletonList(
            TestUtil.createNanoBretActivityProfileCsvRecord("compoundA", "19_01_01",
                "commentNew", "nlucNew", 60d, 60, "unknown",
                NanoBretActivityProfileModifier.EQUAL_TO, 99,99)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        doReturn(existingProfiles).when(nanoBretActivityProfileDao).getNanoBretActivityProfiles(any(), any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(true).when(compoundService).getCompoundExists(anyString());
        doReturn(null).when(kinaseService).getKinase(eq("unknown"));

        service.importNanoBretActivityProfiles(records, true);
    }
}
