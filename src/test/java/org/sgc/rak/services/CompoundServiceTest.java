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
import org.sgc.rak.model.Kinase;
import org.sgc.rak.repositories.ActivityProfileRepository;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

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
        MockitoAnnotations.initMocks(this);
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
        doReturn(expectedPage).when(mockCompoundDao).getCompounds(any(Pageable.class));

        Page<Compound> actualCompounds = service.getCompounds(pr);
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
        doReturn(expectedPage).when(mockCompoundDao).getCompoundsByCompoundNameContainsIgnoreCase(anyString(),
            any(Pageable.class));

        Page<Compound> actualCompounds = service.getCompoundsByCompoundName("foo", pr);
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
        doReturn(kinase).when(mockKinaseService).getKinase(eq(KINASE_DISCOVERX));

        List<ActivityProfile> activityProfiles = Collections.singletonList(
            TestUtil.createActivityProfile(3L, COMPOUND_NAME, KINASE_DISCOVERX, KINASE_ENTREZ,
                0.3, 4)
        );
        PageImpl<ActivityProfile> profilePage= new PageImpl<>(activityProfiles, pr, 1);
        doReturn(profilePage).when(mockActivityProfileRepository)
            .getActivityProfilesByKinaseIdAndPercentControlLessThanEqual(eq(KINASE_ID), anyDouble(),
                any(Pageable.class));

        List<Compound> expectedCompounds = Collections.singletonList(TestUtil.createCompound(COMPOUND_NAME));
        doReturn(expectedCompounds).when(mockCompoundDao).getCompounds(anyList());

        Page<Compound> actualPage = service.getCompoundsByKinaseAndActivity(KINASE_DISCOVERX, 0.3, pr);
        Assert.assertEquals(1, actualPage.getNumberOfElements());
        Assert.assertEquals(1, actualPage.getTotalElements());
        Assert.assertEquals(1, actualPage.getTotalPages());
        for (int i = 0; i < expectedCompounds.size(); i++) {
            TestUtil.assertCompoundsEqual(expectedCompounds.get(i), actualPage.getContent().get(i));
        }
    }

    @Test(expected = NotFoundException.class)
    public void testGetCompoundsByKinaseAndActivity_error_noSuchKinase() {

        doReturn(null).when(mockKinaseService).getKinase(eq(KINASE_DISCOVERX));

        Sort sort = Sort.by(Sort.Order.desc("compoundName"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        service.getCompoundsByKinaseAndActivity(KINASE_DISCOVERX, 0.3, pr);
    }
}
