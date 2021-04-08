package org.sgc.rak.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetActivityProfiles_noFilterParams() {

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<ActivityProfile> expectedProfiles = Collections.singletonList(TestUtil.createActivityProfile(42L));
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(expectedProfiles, pr, 1);
        doReturn(expectedPage).when(mockActivityProfileDao).getActivityProfiles(any(), any(), any(),
            any(Pageable.class));

        Page<ActivityProfile> actualProfiles = service.getActivityProfiles(null, null, null, pr);
        Assertions.assertEquals(1, actualProfiles.getNumberOfElements());
        Assertions.assertEquals(1, actualProfiles.getTotalElements());
        Assertions.assertEquals(1, actualProfiles.getTotalPages());
        for (int i = 0; i < expectedProfiles.size(); i++) {
            TestUtil.assertActivityProfilesEqual(expectedProfiles.get(i), actualProfiles.getContent().get(i));
        }
    }

    @Test
    public void testGetActivityProfiles_compound_happyPath() {

        doReturn(true).when(mockCompoundService).getCompoundExists(eq(COMPOUND_NAME));

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<ActivityProfile> expectedProfiles = Collections.singletonList(TestUtil.createActivityProfile(42L));
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(expectedProfiles, pr, 1);
        doReturn(expectedPage).when(mockActivityProfileDao).getActivityProfiles(eq(COMPOUND_NAME), any(), any(),
            any(Pageable.class));

        Page<ActivityProfile> actualProfiles = service.getActivityProfiles(COMPOUND_NAME, null, null, pr);
        Assertions.assertEquals(1, actualProfiles.getNumberOfElements());
        Assertions.assertEquals(1, actualProfiles.getTotalElements());
        Assertions.assertEquals(1, actualProfiles.getTotalPages());
        for (int i = 0; i < expectedProfiles.size(); i++) {
            TestUtil.assertActivityProfilesEqual(expectedProfiles.get(i), actualProfiles.getContent().get(i));
        }
    }

    @Test
    public void testGetActivityProfiles_compound_error_noSuchCompound() {

        doReturn(false).when(mockCompoundService).getCompoundExists(eq(COMPOUND_NAME));

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.getActivityProfiles(COMPOUND_NAME, null, null, pr);
        });
    }

    @Test
    public void testGetActivityProfiles_compoundKinaseAndPercentControl_happyPath() {

        doReturn(true).when(mockCompoundService).getCompoundExists(eq(COMPOUND_NAME));

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<ActivityProfile> expectedProfiles = Collections.singletonList(TestUtil.createActivityProfile(42L));
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(expectedProfiles, pr, 1);
        doReturn(expectedPage).when(mockActivityProfileDao).getActivityProfiles(eq(COMPOUND_NAME), anyList(),
            anyDouble(), any(Pageable.class));

        List<Long> kinaseIds = Collections.singletonList(42L);
        Page<ActivityProfile> actualProfiles = service.getActivityProfiles(COMPOUND_NAME, kinaseIds, 0.3, pr);
        Assertions.assertEquals(1, actualProfiles.getNumberOfElements());
        Assertions.assertEquals(1, actualProfiles.getTotalElements());
        Assertions.assertEquals(1, actualProfiles.getTotalPages());
        for (int i = 0; i < expectedProfiles.size(); i++) {
            TestUtil.assertActivityProfilesEqual(expectedProfiles.get(i), actualProfiles.getContent().get(i));
        }
    }

    @Test
    public void testGetActivityProfiles_compoundKinasePercentControl_error_noSuchCompound() {

        doReturn(false).when(mockCompoundService).getCompoundExists(eq(COMPOUND_NAME));

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Long> kinaseIds = Collections.singletonList(42L);
        Assertions.assertThrows(BadRequestException.class, () -> {
            service.getActivityProfiles(COMPOUND_NAME, kinaseIds, 0.3, pr);
        });
    }

    @Test
    public void testGetActivityProfiles_kinasePercentControl_happyPath() {

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<ActivityProfile> expectedProfiles = Collections.singletonList(TestUtil.createActivityProfile(42L));
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(expectedProfiles, pr, 1);
        doReturn(expectedPage).when(mockActivityProfileDao).getActivityProfiles(any(), anyList(), anyDouble(),
            any(Pageable.class));

        List<Long> kinaseIds = Collections.singletonList(42L);
        Page<ActivityProfile> actualProfiles = service.getActivityProfiles(null, kinaseIds, 0.3, pr);
        Assertions.assertEquals(1, actualProfiles.getNumberOfElements());
        Assertions.assertEquals(1, actualProfiles.getTotalElements());
        Assertions.assertEquals(1, actualProfiles.getTotalPages());
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
        doReturn(kinase).when(mockKinaseService).getKinaseByDiscoverx(eq(kinase.getDiscoverxGeneSymbol()));
        kinase = TestUtil.createKinase("discoverxB", "entrezB");
        doReturn(kinase).when(mockKinaseService).getKinaseByDiscoverx(eq(kinase.getDiscoverxGeneSymbol()));

        ObjectImportRep importRep = service.importActivityProfiles(records, commit);
        List<List<ObjectImportRep.FieldStatus>> fieldStatuses = importRep.getFieldStatuses();
        Assertions.assertEquals(2, fieldStatuses.size());

        // Verify that the first response row shows properly merged values
        List<ObjectImportRep.FieldStatus> rowData = fieldStatuses.get(0);
        Assertions.assertEquals("compoundName", rowData.get(0).getFieldName());
        Assertions.assertEquals("compoundA", rowData.get(0).getNewValue());
        Assertions.assertEquals("compoundA", rowData.get(0).getOldValue());
        Assertions.assertEquals("discoverxGeneSymbol", rowData.get(1).getFieldName());
        Assertions.assertEquals("discoverxA", rowData.get(1).getNewValue());
        Assertions.assertEquals("discoverxA", rowData.get(1).getOldValue());
        Assertions.assertEquals("entrezGeneSymbol", rowData.get(2).getFieldName());
        Assertions.assertEquals("entrezA", rowData.get(2).getNewValue());
        Assertions.assertEquals("entrezA", rowData.get(2).getOldValue());
        Assertions.assertEquals("percentControl", rowData.get(3).getFieldName());
        Assertions.assertEquals(0.9, (Double)rowData.get(3).getNewValue(), 0.001);
        Assertions.assertEquals(0.1, (Double)rowData.get(3).getOldValue(), 0.001);
        Assertions.assertEquals("compoundConcentration", rowData.get(4).getFieldName());
        Assertions.assertEquals(4, ((Integer)rowData.get(4).getNewValue()).intValue());
        Assertions.assertEquals(1, ((Integer)rowData.get(4).getOldValue()).intValue());

        // Verify that the second response row shows all new values
        rowData = fieldStatuses.get(1);
        Assertions.assertEquals("compoundName", rowData.get(0).getFieldName());
        Assertions.assertEquals("compoundB", rowData.get(0).getNewValue());
        Assertions.assertNull(rowData.get(0).getOldValue());
        Assertions.assertEquals("discoverxGeneSymbol", rowData.get(1).getFieldName());
        Assertions.assertEquals("discoverxB", rowData.get(1).getNewValue());
        Assertions.assertNull(rowData.get(1).getOldValue());
        Assertions.assertEquals("entrezGeneSymbol", rowData.get(2).getFieldName());
        Assertions.assertEquals("entrezB", rowData.get(2).getNewValue());
        Assertions.assertNull(rowData.get(2).getOldValue());
        Assertions.assertEquals("percentControl", rowData.get(3).getFieldName());
        Assertions.assertEquals(0.8, (Double)rowData.get(3).getNewValue(), 0.001);
        Assertions.assertNull(rowData.get(3).getOldValue());
        Assertions.assertEquals("compoundConcentration", rowData.get(4).getFieldName());
        Assertions.assertEquals(3, ((Integer)rowData.get(4).getNewValue()).intValue());
        Assertions.assertNull(rowData.get(4).getOldValue());

        // Verify the commit only happens if commit == true
        verify(mockActivityProfileDao, times(commit ? 1 : 0)).save(any());
    }

    @Test
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
        doReturn(Collections.singletonList(kinase)).when(mockKinaseService).getKinase(eq(kinase.getEntrezGeneSymbol()));

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.importActivityProfiles(records, true);
        });
    }

    @Test
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

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.importActivityProfiles(records, true);
        });
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
        doReturn(kinase).when(mockKinaseService).getKinaseByDiscoverx(eq(kinase.getDiscoverxGeneSymbol()));
        kinase = TestUtil.createKinase("discoverxB", "entrezB");
        doReturn(kinase).when(mockKinaseService).getKinaseByDiscoverx(eq(kinase.getDiscoverxGeneSymbol()));

        ObjectImportRep importRep = service.importKdValues(records, commit);
        List<List<ObjectImportRep.FieldStatus>> fieldStatuses = importRep.getFieldStatuses();
        Assertions.assertEquals(2, fieldStatuses.size());

        // Verify that the first response row shows properly merged values
        List<ObjectImportRep.FieldStatus> rowData = fieldStatuses.get(0);
        Assertions.assertEquals("compoundName", rowData.get(0).getFieldName());
        Assertions.assertEquals("compoundA", rowData.get(0).getNewValue());
        Assertions.assertEquals("compoundA", rowData.get(0).getOldValue());
        Assertions.assertEquals("discoverxGeneSymbol", rowData.get(1).getFieldName());
        Assertions.assertEquals("discoverxA", rowData.get(1).getNewValue());
        Assertions.assertEquals("discoverxA", rowData.get(1).getOldValue());
        Assertions.assertEquals("entrezGeneSymbol", rowData.get(2).getFieldName());
        Assertions.assertEquals("entrezA", rowData.get(2).getNewValue());
        Assertions.assertEquals("entrezA", rowData.get(2).getOldValue());
        Assertions.assertEquals("kd", rowData.get(3).getFieldName());
        Assertions.assertEquals(0.3, ((Double)rowData.get(3).getNewValue()), 0.001);
        Assertions.assertNull(rowData.get(3).getOldValue());

        // Verify that the second response row shows all new values
        rowData = fieldStatuses.get(1);
        Assertions.assertEquals("compoundName", rowData.get(0).getFieldName());
        Assertions.assertEquals("compoundB", rowData.get(0).getNewValue());
        Assertions.assertNull(rowData.get(0).getOldValue());
        Assertions.assertEquals("discoverxGeneSymbol", rowData.get(1).getFieldName());
        Assertions.assertEquals("discoverxB", rowData.get(1).getNewValue());
        Assertions.assertNull(rowData.get(1).getOldValue());
        Assertions.assertEquals("entrezGeneSymbol", rowData.get(2).getFieldName());
        Assertions.assertEquals("entrezB", rowData.get(2).getNewValue());
        Assertions.assertNull(rowData.get(2).getOldValue());
        Assertions.assertEquals("kd", rowData.get(3).getFieldName());
        Assertions.assertEquals(0.4, (Double)rowData.get(3).getNewValue(), 0.001);
        Assertions.assertNull(rowData.get(3).getOldValue());

        // Verify the commit only happens if commit == true
        verify(mockActivityProfileDao, times(commit ? 1 : 0)).save(any());
    }

    @Test
    public void testImportKdValues_error_unknownCompound() {

        List<KdCsvRecord> records = Collections.singletonList(
            TestUtil.createKdCsvRecord("unknown", "discoverxA", "entrezA",
                "=", 0.3)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        doReturn(existingProfiles).when(mockActivityProfileDao).getActivityProfiles(any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(false).when(mockCompoundService).getCompoundExists(anyString());
        Kinase kinase = TestUtil.createKinase("discoverxA", "entrezA");
        doReturn(Collections.singletonList(kinase)).when(mockKinaseService).getKinase(eq(records.get(0)
            .getEntrezGeneSymbol()));

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.importKdValues(records, true);
        });
    }

    @Test
    public void testImportKdValues_error_unknownKinase() {

        List<KdCsvRecord> records = Collections.singletonList(
            TestUtil.createKdCsvRecord(COMPOUND_NAME, "unknown", "unknown", "=", 0.3)
        );

        Set<ActivityProfile> existingProfiles = new HashSet<>();
        doReturn(existingProfiles).when(mockActivityProfileDao).getActivityProfiles(any(), any());

        // Mocks required during csv rep => activity profile conversion
        doReturn(true).when(mockCompoundService).getCompoundExists(anyString());
        doReturn(null).when(mockKinaseService).getKinase(eq(records.get(0).getDiscoverxGeneSymbol()));

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.importKdValues(records, true);
        });
    }
}
