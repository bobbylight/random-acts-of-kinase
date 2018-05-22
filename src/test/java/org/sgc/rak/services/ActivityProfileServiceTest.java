package org.sgc.rak.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.dao.ActivityProfileDao;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.KdCsvRecord;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ActivityProfileServiceTest {

    @Mock
    private ActivityProfileDao mockActivityProfileDao;

    @Mock
    private CompoundService mockCompoundService;

    @Mock
    private KinaseService mockKinaseService;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private ActivityProfileService service;

    private static final String COMPOUND_NAME = "compoundA";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetActivityProfiles() {

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<ActivityProfile> expectedProfiles = Collections.singletonList(TestUtil.createActivityProfile(42L));
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(expectedProfiles, pr, 1);
        doReturn(expectedPage).when(mockActivityProfileDao).getActivityProfiles(any(Pageable.class));

        Page<ActivityProfile> actualProfiles = service.getActivityProfiles(pr);
        Assert.assertEquals(1, actualProfiles.getNumberOfElements());
        Assert.assertEquals(1, actualProfiles.getTotalElements());
        Assert.assertEquals(1, actualProfiles.getTotalPages());
        for (int i = 0; i < expectedProfiles.size(); i++) {
            TestUtil.assertActivityProfilesEqual(expectedProfiles.get(i), actualProfiles.getContent().get(i));
        }
    }

    @Test
    public void testGetActivityProfilesForCompound_happyPath() {

        doReturn(true).when(mockCompoundService).getCompoundExists(eq(COMPOUND_NAME));

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<ActivityProfile> expectedProfiles = Collections.singletonList(TestUtil.createActivityProfile(42L));
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(expectedProfiles, pr, 1);
        doReturn(expectedPage).when(mockActivityProfileDao).getActivityProfilesByCompoundNameIgnoreCase(
            eq(COMPOUND_NAME), any(Pageable.class));

        Page<ActivityProfile> actualProfiles = service.getActivityProfilesForCompound(COMPOUND_NAME, pr);
        Assert.assertEquals(1, actualProfiles.getNumberOfElements());
        Assert.assertEquals(1, actualProfiles.getTotalElements());
        Assert.assertEquals(1, actualProfiles.getTotalPages());
        for (int i = 0; i < expectedProfiles.size(); i++) {
            TestUtil.assertActivityProfilesEqual(expectedProfiles.get(i), actualProfiles.getContent().get(i));
        }
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfilesForCompound_error_noSuchCompound() {

        doReturn(false).when(mockCompoundService).getCompoundExists(eq(COMPOUND_NAME));

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        service.getActivityProfilesForCompound(COMPOUND_NAME, pr);
    }

    @Test
    public void testGetActivityProfilesForCompoundAndKinaseAndPercentControl_happyPath() {

        doReturn(true).when(mockCompoundService).getCompoundExists(eq(COMPOUND_NAME));

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<ActivityProfile> expectedProfiles = Collections.singletonList(TestUtil.createActivityProfile(42L));
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(expectedProfiles, pr, 1);
        doReturn(expectedPage).when(mockActivityProfileDao)
            .getActivityProfilesByCompoundNameIgnoreCaseAndKinaseIgnoreCaseAndPercentControl(
                eq(COMPOUND_NAME), anyLong(), anyDouble(), any(Pageable.class));

        Page<ActivityProfile> actualProfiles = service.getActivityProfilesForCompoundAndKinaseAndPercentControl(
            COMPOUND_NAME, 42, 0.3, pr);
        Assert.assertEquals(1, actualProfiles.getNumberOfElements());
        Assert.assertEquals(1, actualProfiles.getTotalElements());
        Assert.assertEquals(1, actualProfiles.getTotalPages());
        for (int i = 0; i < expectedProfiles.size(); i++) {
            TestUtil.assertActivityProfilesEqual(expectedProfiles.get(i), actualProfiles.getContent().get(i));
        }
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfilesForCompoundAndKinaseAndPercentControl_error_noSuchCompound() {

        doReturn(false).when(mockCompoundService).getCompoundExists(eq(COMPOUND_NAME));

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        service.getActivityProfilesForCompoundAndKinaseAndPercentControl(COMPOUND_NAME, 32, 0.3, pr);
    }

    @Test
    public void testGetActivityProfilesForKinaseAndPercentControl_happyPath() {

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<ActivityProfile> expectedProfiles = Collections.singletonList(TestUtil.createActivityProfile(42L));
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(expectedProfiles, pr, 1);
        doReturn(expectedPage).when(mockActivityProfileDao).getActivityProfilesByKinaseIgnoreCaseAndPercentControl(
                anyLong(), anyDouble(), any(Pageable.class));

        Page<ActivityProfile> actualProfiles = service.getActivityProfilesForKinaseAndPercentControl(42, 0.3, pr);
        Assert.assertEquals(1, actualProfiles.getNumberOfElements());
        Assert.assertEquals(1, actualProfiles.getTotalElements());
        Assert.assertEquals(1, actualProfiles.getTotalPages());
        for (int i = 0; i < expectedProfiles.size(); i++) {
            TestUtil.assertActivityProfilesEqual(expectedProfiles.get(i), actualProfiles.getContent().get(i));
        }
    }

    @Test
    public void testImportActivityProfiles_happyPath_commit() {
        testImportActivityProfiles_happyPath_impl(true);
    }

    @Test
    public void testImportActivityProfiles_happyPath_noCommit() {
        testImportActivityProfiles_happyPath_impl(false);
    }

    private void testImportActivityProfiles_happyPath_impl(boolean commit) {

        List<ActivityProfileCsvRecord> records = Arrays.asList(
            TestUtil.createActivityProfileCsvRecord("compoundA", "discoverxA", "entrezA",
                0.9, 4),
            TestUtil.createActivityProfileCsvRecord("compoundB", "discoverxB", "entrezB",
                0.8, 3)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        existingProfiles.add(TestUtil.createActivityProfile(42L, "compoundA",
            "discoverxA", "entrezA", 0.1, 1));
        doReturn(existingProfiles).when(mockActivityProfileDao).getActivityProfiles(any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(true).when(mockCompoundService).getCompoundExists(anyString());
        Kinase kinase = TestUtil.createKinase("discoverxA", "entrezA");
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinase.getDiscoverxGeneSymbol()));
        kinase = TestUtil.createKinase("discoverxB", "entrezB");
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinase.getDiscoverxGeneSymbol()));

        ObjectImportRep importRep = service.importActivityProfiles(records, commit);
        List<List<ObjectImportRep.FieldStatus>> fieldStatuses = importRep.getFieldStatuses();
        Assert.assertEquals(2, fieldStatuses.size());

        // Verify that the first response row shows properly merged values
        List<ObjectImportRep.FieldStatus> rowData = fieldStatuses.get(0);
        Assert.assertEquals("compoundName", rowData.get(0).getFieldName());
        Assert.assertEquals("compoundA", rowData.get(0).getNewValue());
        Assert.assertEquals("compoundA", rowData.get(0).getOldValue());
        Assert.assertEquals("discoverxGeneSymbol", rowData.get(1).getFieldName());
        Assert.assertEquals("discoverxA", rowData.get(1).getNewValue());
        Assert.assertEquals("discoverxA", rowData.get(1).getOldValue());
        Assert.assertEquals("percentControl", rowData.get(2).getFieldName());
        Assert.assertEquals(0.9, (Double)rowData.get(2).getNewValue(), 0.001);
        Assert.assertEquals(0.1, (Double)rowData.get(2).getOldValue(), 0.001);
        Assert.assertEquals("compoundConcentration", rowData.get(3).getFieldName());
        Assert.assertEquals(4, ((Integer)rowData.get(3).getNewValue()).intValue());
        Assert.assertEquals(1, ((Integer)rowData.get(3).getOldValue()).intValue());

        // Verify that the second response row shows all new values
        rowData = fieldStatuses.get(1);
        Assert.assertEquals("compoundName", rowData.get(0).getFieldName());
        Assert.assertEquals("compoundB", rowData.get(0).getNewValue());
        Assert.assertNull(rowData.get(0).getOldValue());
        Assert.assertEquals("discoverxGeneSymbol", rowData.get(1).getFieldName());
        Assert.assertEquals("discoverxB", rowData.get(1).getNewValue());
        Assert.assertNull(rowData.get(1).getOldValue());
        Assert.assertEquals("percentControl", rowData.get(2).getFieldName());
        Assert.assertEquals(0.8, (Double)rowData.get(2).getNewValue(), 0.001);
        Assert.assertNull(rowData.get(2).getOldValue());
        Assert.assertEquals("compoundConcentration", rowData.get(3).getFieldName());
        Assert.assertEquals(3, ((Integer)rowData.get(3).getNewValue()).intValue());
        Assert.assertNull(rowData.get(3).getOldValue());

        // Verify the commit only happens if commit == true
        verify(mockActivityProfileDao, times(commit ? 1 : 0)).save(any());
    }

    @Test(expected = BadRequestException.class)
    public void testImportActivityProfiles_error_unknownCompound() throws BadRequestException {

        List<ActivityProfileCsvRecord> records = Collections.singletonList(
            TestUtil.createActivityProfileCsvRecord("unknown", "discoverxA", "entrezA",
                0.9, 4)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        existingProfiles.add(TestUtil.createActivityProfile(42L, "compoundA",
            "discoverxA", "entrezA", 0.1, 1));
        doReturn(existingProfiles).when(mockActivityProfileDao).getActivityProfiles(any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(false).when(mockCompoundService).getCompoundExists(anyString());
        Kinase kinase = TestUtil.createKinase("discoverxA", "entrezA");
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinase.getDiscoverxGeneSymbol()));

        service.importActivityProfiles(records, true);
    }

    @Test(expected = BadRequestException.class)
    public void testImportActivityProfiles_error_unknownKinase() throws BadRequestException {

        List<ActivityProfileCsvRecord> records = Collections.singletonList(
            TestUtil.createActivityProfileCsvRecord(COMPOUND_NAME, "unknown", "unknown",
                0.9, 4)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        doReturn(existingProfiles).when(mockActivityProfileDao).getActivityProfiles(any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(true).when(mockCompoundService).getCompoundExists(anyString());
        doReturn(null).when(mockKinaseService).getKinase(eq("unknown"));

        service.importActivityProfiles(records, true);
    }

    @Test
    public void testImportKdValues_happyPath_commit() {
        testImportKdValues_happyPath_impl(true);
    }

    @Test
    public void testImportKdValues_happyPath_noCommit() {
        testImportKdValues_happyPath_impl(false);
    }

    private void testImportKdValues_happyPath_impl(boolean commit) {

        List<KdCsvRecord> records = Arrays.asList(
            TestUtil.createKdCsvRecord("compoundA", "discoverxA", "entrezA",
                "=", 0.3),
            TestUtil.createKdCsvRecord("compoundB", "discoverxB", "entrezB",
                "=", 0.4)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        existingProfiles.add(TestUtil.createActivityProfile(42L, "compoundA",
            "discoverxA", "entrezA", 0.1, 1));
        doReturn(existingProfiles).when(mockActivityProfileDao).getActivityProfiles(any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(true).when(mockCompoundService).getCompoundExists(anyString());
        Kinase kinase = TestUtil.createKinase("discoverxA", "entrezA");
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinase.getDiscoverxGeneSymbol()));
        kinase = TestUtil.createKinase("discoverxB", "entrezB");
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinase.getDiscoverxGeneSymbol()));

        ObjectImportRep importRep = service.importKdValues(records, commit);
        List<List<ObjectImportRep.FieldStatus>> fieldStatuses = importRep.getFieldStatuses();
        Assert.assertEquals(2, fieldStatuses.size());

        // Verify that the first response row shows properly merged values
        List<ObjectImportRep.FieldStatus> rowData = fieldStatuses.get(0);
        Assert.assertEquals("compoundName", rowData.get(0).getFieldName());
        Assert.assertEquals("compoundA", rowData.get(0).getNewValue());
        Assert.assertEquals("compoundA", rowData.get(0).getOldValue());
        Assert.assertEquals("discoverxGeneSymbol", rowData.get(1).getFieldName());
        Assert.assertEquals("discoverxA", rowData.get(1).getNewValue());
        Assert.assertEquals("discoverxA", rowData.get(1).getOldValue());
        Assert.assertEquals("entrezGeneSymbol", rowData.get(2).getFieldName());
        Assert.assertEquals("entrezA", rowData.get(2).getNewValue());
        Assert.assertEquals("entrezA", rowData.get(2).getOldValue());
        Assert.assertEquals("kd", rowData.get(3).getFieldName());
        Assert.assertEquals(0.3, ((Double)rowData.get(3).getNewValue()), 0.001);
        Assert.assertNull(rowData.get(3).getOldValue());

        // Verify that the second response row shows all new values
        rowData = fieldStatuses.get(1);
        Assert.assertEquals("compoundName", rowData.get(0).getFieldName());
        Assert.assertEquals("compoundB", rowData.get(0).getNewValue());
        Assert.assertNull(rowData.get(0).getOldValue());
        Assert.assertEquals("discoverxGeneSymbol", rowData.get(1).getFieldName());
        Assert.assertEquals("discoverxB", rowData.get(1).getNewValue());
        Assert.assertNull(rowData.get(1).getOldValue());
        Assert.assertEquals("entrezGeneSymbol", rowData.get(2).getFieldName());
        Assert.assertEquals("entrezB", rowData.get(2).getNewValue());
        Assert.assertNull(rowData.get(2).getOldValue());
        Assert.assertEquals("kd", rowData.get(3).getFieldName());
        Assert.assertEquals(0.4, (Double)rowData.get(3).getNewValue(), 0.001);
        Assert.assertNull(rowData.get(3).getOldValue());

        // Verify the commit only happens if commit == true
        verify(mockActivityProfileDao, times(commit ? 1 : 0)).save(any());
    }

    @Test(expected = BadRequestException.class)
    public void testImportKdValues_error_unknownCompound() throws BadRequestException {

        List<KdCsvRecord> records = Collections.singletonList(
            TestUtil.createKdCsvRecord("unknown", "discoverxA", "entrezA",
                "=", 0.3)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        doReturn(existingProfiles).when(mockActivityProfileDao).getActivityProfiles(any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(false).when(mockCompoundService).getCompoundExists(anyString());
        Kinase kinase = TestUtil.createKinase("discoverxA", "entrezA");
        doReturn(kinase).when(mockKinaseService).getKinase(eq(records.get(0).getDiscoverxGeneSymbol()));

        service.importKdValues(records, true);
    }

    @Test(expected = BadRequestException.class)
    public void testImportKdValues_error_unknownKinase() throws BadRequestException {

        List<KdCsvRecord> records = Collections.singletonList(
            TestUtil.createKdCsvRecord(COMPOUND_NAME, "unknown", "unknown", "=", 0.3)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        doReturn(existingProfiles).when(mockActivityProfileDao).getActivityProfiles(any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(true).when(mockCompoundService).getCompoundExists(anyString());
        doReturn(null).when(mockKinaseService).getKinase(eq(records.get(0).getDiscoverxGeneSymbol()));

        service.importKdValues(records, true);
    }
}
