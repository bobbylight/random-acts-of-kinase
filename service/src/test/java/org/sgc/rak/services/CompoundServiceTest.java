package org.sgc.rak.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.dao.CompoundDao;
import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.CompoundCountPair;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.repositories.ActivityProfileRepository;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CompoundServiceTest {

    @Mock
    private CompoundDao mockCompoundDao;

    @Mock
    private KinaseService mockKinaseService;

    @Mock
    private ActivityProfileRepository mockActivityProfileRepository;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private CompoundService service;

    private static final String COMPOUND_NAME = "compoundA";
    private static final long KINASE_ID = 42;
    private static final String KINASE_DISCOVERX = "discoverxA";
    private static final String KINASE_ENTREZ = "entrezA";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCompound() {

        Compound expected = TestUtil.createCompound(COMPOUND_NAME);

        doReturn(expected).when(mockCompoundDao).getCompound(eq(COMPOUND_NAME));

        Compound actual = service.getCompound(COMPOUND_NAME);
        TestUtil.assertCompoundsEqual(expected, actual);
    }

    @Test
    public void testGetCompoundExists() {
        doReturn(true).when(mockCompoundDao).getCompoundExists(eq(COMPOUND_NAME));
        Assert.assertTrue(service.getCompoundExists(COMPOUND_NAME));
    }

    @Test
    public void testGetCompounds() {

        Sort sort = Sort.by(Sort.Order.desc("compoundName"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Compound> expectedCompounds = Collections.singletonList(TestUtil.createCompound(COMPOUND_NAME));
        PageImpl<Compound> expectedPage = new PageImpl<>(expectedCompounds, pr, 1);
        doReturn(expectedPage).when(mockCompoundDao).getCompounds(any(), any(Pageable.class), anyBoolean());

        Page<Compound> actualCompounds = service.getCompounds(null, pr, true);
        Assert.assertEquals(1, actualCompounds.getNumberOfElements());
        Assert.assertEquals(1, actualCompounds.getTotalElements());
        Assert.assertEquals(1, actualCompounds.getTotalPages());
        for (int i = 0; i < expectedCompounds.size(); i++) {
            TestUtil.assertCompoundsEqual(expectedCompounds.get(i), actualCompounds.getContent().get(i));
        }
    }

    @Test
    public void testGetCompoundsByCompoundName() {

        Sort sort = Sort.by(Sort.Order.desc("compoundName"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Compound> expectedCompounds = Collections.singletonList(TestUtil.createCompound(COMPOUND_NAME));
        PageImpl<Compound> expectedPage = new PageImpl<>(expectedCompounds, pr, 1);
        doReturn(expectedPage).when(mockCompoundDao).getCompounds(anyString(), any(Pageable.class), anyBoolean());

        Page<Compound> actualCompounds = service.getCompounds("foo", pr, true);
        Assert.assertEquals(1, actualCompounds.getNumberOfElements());
        Assert.assertEquals(1, actualCompounds.getTotalElements());
        Assert.assertEquals(1, actualCompounds.getTotalPages());
        for (int i = 0; i < expectedCompounds.size(); i++) {
            TestUtil.assertCompoundsEqual(expectedCompounds.get(i), actualCompounds.getContent().get(i));
        }
    }

    @Test
    public void testGetCompoundsByKinaseAndActivity_happyPath() {

        Sort sort = Sort.by(Sort.Order.desc("compoundName"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        Kinase kinase = TestUtil.createKinase(KINASE_ID, KINASE_DISCOVERX, KINASE_ENTREZ);
        doReturn(Collections.singletonList(kinase)).when(mockKinaseService).getKinase(eq(KINASE_ENTREZ));

        List<ActivityProfile> activityProfiles = Collections.singletonList(
            TestUtil.createActivityProfile(3L, COMPOUND_NAME, KINASE_DISCOVERX, KINASE_ENTREZ,
                0.3, 4)
        );
        PageImpl<ActivityProfile> profilePage = new PageImpl<>(activityProfiles, pr, 1);
        List<Long> kinaseIds = Collections.singletonList(KINASE_ID);
        doReturn(profilePage).when(mockActivityProfileRepository)
            .getActivityProfilesByKinaseIdInAndPercentControlLessThanEqual(eq(kinaseIds), anyDouble(),
                any(Pageable.class));

        List<Compound> expectedCompounds = Collections.singletonList(TestUtil.createCompound(COMPOUND_NAME));
        doReturn(expectedCompounds).when(mockCompoundDao).getCompounds(anyList());

        Page<Compound> actualPage = service.getCompoundsByKinaseAndActivity(KINASE_ENTREZ, 0.3, pr);
        Assert.assertEquals(1, actualPage.getNumberOfElements());
        Assert.assertEquals(1, actualPage.getTotalElements());
        Assert.assertEquals(1, actualPage.getTotalPages());
        for (int i = 0; i < expectedCompounds.size(); i++) {
            TestUtil.assertCompoundsEqual(expectedCompounds.get(i), actualPage.getContent().get(i));
        }
    }

    @Test
    public void testGetCompoundsByKinaseAndKd_happyPath() {

        Sort sort = Sort.by(Sort.Order.desc("compoundName"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        Kinase kinase = TestUtil.createKinase(KINASE_ID, KINASE_DISCOVERX, KINASE_ENTREZ);
        doReturn(Collections.singletonList(kinase)).when(mockKinaseService).getKinase(eq(KINASE_ENTREZ));

        List<ActivityProfile> activityProfiles = Collections.singletonList(
            TestUtil.createActivityProfile(3L, COMPOUND_NAME, KINASE_DISCOVERX, KINASE_ENTREZ,
                0.3, 4)
        );
        PageImpl<ActivityProfile> profilePage = new PageImpl<>(activityProfiles, pr, 1);
        List<Long> kinaseIds = Collections.singletonList(KINASE_ID);
        doReturn(profilePage).when(mockActivityProfileRepository)
            .getActivityProfilesByKinaseIdInAndKdLessThanEqual(eq(kinaseIds), anyDouble(),
                any(Pageable.class));

        List<Compound> expectedCompounds = Collections.singletonList(TestUtil.createCompound(COMPOUND_NAME));
        doReturn(expectedCompounds).when(mockCompoundDao).getCompounds(anyList());

        Page<Compound> actualPage = service.getCompoundsByKinaseAndKd(KINASE_ENTREZ, 42, pr);
        Assert.assertEquals(1, actualPage.getNumberOfElements());
        Assert.assertEquals(1, actualPage.getTotalElements());
        Assert.assertEquals(1, actualPage.getTotalPages());
        for (int i = 0; i < expectedCompounds.size(); i++) {
            TestUtil.assertCompoundsEqual(expectedCompounds.get(i), actualPage.getContent().get(i));
        }
    }

    @Test(expected = NotFoundException.class)
    public void testGetCompoundsByKinaseAndKd_error_noSuchKinase() {

        doReturn(Collections.emptyList()).when(mockKinaseService).getKinase(eq(KINASE_ENTREZ));

        Sort sort = Sort.by(Sort.Order.desc("compoundName"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        service.getCompoundsByKinaseAndKd(KINASE_ENTREZ, 42, pr);
    }

    @Test
    public void testGetCompoundsMissingActivityProfiles_happyPath() {

        List<Compound> compounds = Arrays.asList(
            TestUtil.createCompound("compoundA"),
            TestUtil.createCompound("compoundB")
        );

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<Compound> page = new PageImpl<>(compounds, pageable, 100);
        doReturn(page).when(mockCompoundDao).getCompoundsMissingPublicationInfo(any(), any(Pageable.class));

        Page<Compound> actualResponse = service.getCompoundsMissingPublicationInfo(null, pageable);
        Assert.assertEquals(2, actualResponse.getNumberOfElements());
        Assert.assertEquals(100, actualResponse.getTotalElements());
        Assert.assertEquals(50, actualResponse.getTotalPages());
        Assert.assertEquals(compounds.size(), actualResponse.getSize());
        for (int i = 0; i < compounds.size(); i++) {
            Compound expected = compounds.get(i);
            Compound actual = actualResponse.getContent().get(i);
            Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
        }
    }

    @Test
    public void testGetCompoundsMissingPublicationInfo_happyPath() {

        CompoundCountPair pairA = new CompoundCountPair("compoundA", 3);
        CompoundCountPair pairB = new CompoundCountPair("compoundB", 4);
        List<CompoundCountPair> pairs = Arrays.asList(pairA, pairB);

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<CompoundCountPair> page = new PageImpl<>(pairs, pageable, 100);
        doReturn(page).when(mockCompoundDao).getCompoundsMissingActivityProfiles(any(), any(Pageable.class));

        Page<CompoundCountPair> actualResponse = service.getCompoundsMissingActivityProfiles(null, pageable);
        Assert.assertEquals(2, actualResponse.getNumberOfElements());
        Assert.assertEquals(100, actualResponse.getTotalElements());
        Assert.assertEquals(50, actualResponse.getTotalPages());
        Assert.assertEquals(pairs.size(), actualResponse.getSize());
        for (int i = 0; i < pairs.size(); i++) {
            CompoundCountPair expected = pairs.get(i);
            CompoundCountPair actual = actualResponse.getContent().get(i);
            Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
            Assert.assertEquals(expected.getCount(), actual.getCount());
        }
    }

    @Test
    public void testGetHiddenCompounds() {

        List<Compound> compounds = Arrays.asList(
            TestUtil.createCompound("compoundA"),
            TestUtil.createCompound("compoundB")
        );

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<Compound> page = new PageImpl<>(compounds, pageable, 100);
        doReturn(page).when(mockCompoundDao).getHiddenCompounds(any(), any(Pageable.class));

        Page<Compound> actualResponse = service.getHiddenCompounds(null, pageable);
        Assert.assertEquals(2, actualResponse.getNumberOfElements());
        Assert.assertEquals(100, actualResponse.getTotalElements());
        Assert.assertEquals(50, actualResponse.getTotalPages());
        Assert.assertEquals(compounds.size(), actualResponse.getSize());
        for (int i = 0; i < compounds.size(); i++) {
            Compound expected = compounds.get(i);
            Compound actual = actualResponse.getContent().get(i);
            Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
        }
    }

    @Test
    public void testGetIncompleteCompounds_happyPath() {

        List<Compound> compounds = Arrays.asList(
            TestUtil.createCompound("compoundA"),
            TestUtil.createCompound("compoundB")
        );

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<Compound> page = new PageImpl<>(compounds, pageable, 100);
        doReturn(page).when(mockCompoundDao).getIncompleteCompounds(any(), any(Pageable.class));

        Page<Compound> actualResponse = service.getIncompleteCompounds(null, pageable);
        Assert.assertEquals(2, actualResponse.getNumberOfElements());
        Assert.assertEquals(100, actualResponse.getTotalElements());
        Assert.assertEquals(50, actualResponse.getTotalPages());
        Assert.assertEquals(compounds.size(), actualResponse.getSize());
        for (int i = 0; i < compounds.size(); i++) {
            Compound expected = compounds.get(i);
            Compound actual = actualResponse.getContent().get(i);
            Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
        }
    }

    @Test
    public void testImportCompounds_happyPath_commit() {
        testImportCompoundsImpl(false);
    }

    @Test
    public void testImportCompounds_happyPath_noCommit() {
        testImportCompoundsImpl(true);
    }

    @SuppressWarnings("unchecked")
    private void testImportCompoundsImpl(boolean commit) {

        List<Compound> compounds = Arrays.asList(
            TestUtil.createCompound("compoundA"),
            TestUtil.createCompound("compoundB")
        );

        List<Compound> priorCompounds = Collections.singletonList(
            TestUtil.createCompound("compoundA")
        );
        doReturn(priorCompounds).when(mockCompoundDao).getCompounds(anyList());

        service.importCompounds(compounds, commit);

        // Verify save was only performed if commit was true
        if (commit) {
            verify(mockCompoundDao, times(1)).save(any(Iterable.class));
        }
    }

    @Test
    public void testUpdateCompound() {

        Compound expected = TestUtil.createCompound(COMPOUND_NAME);

        doReturn(expected).when(mockCompoundDao).save(any(Compound.class));

        Compound actual = service.updateCompound(expected);
        TestUtil.assertCompoundsEqual(expected, actual);
    }
}
