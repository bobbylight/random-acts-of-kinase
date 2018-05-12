package org.sgc.rak.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.ActivityProfileService;
import org.sgc.rak.services.KinaseService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class ActivityProfileControllerTest {

    @Mock
    private ActivityProfileService mockActivityProfileService;

    @Mock
    private KinaseService mockKinaseService;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private ActivityProfileController controller;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfiles_kinaseWithoutActivity() {
        controller.getActivityProfiles("compound", "kinase", null, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfiles_activityWithoutKinase() {
        controller.getActivityProfiles("compound", null, 0d, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfiles_activityLessThanZero() {
        controller.getActivityProfiles("compound", "kinase", -1d, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfiles_activityGreaterThanOne() {
        controller.getActivityProfiles("compound", "kinase", 2d, null);
    }

    @Test
    public void testGetActivityProfiles_happyPath_nothingSpecified() {

        List<ActivityProfile> kapList = new ArrayList<>();
        ActivityProfile kap = new ActivityProfile();
        kap.setId(33L);
        kapList.add(kap);
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(kapList);
        doReturn(expectedPage).when(mockActivityProfileService)
            .getActivityProfiles(any(Pageable.class));

        PageRequest pageInfo = PageRequest.of(0, 20);
        PagedDataRep<ActivityProfile> actual = controller.getActivityProfiles(
            null, null, null, pageInfo);

        Assert.assertEquals(0, actual.getStart());
        Assert.assertEquals(kapList.size(), actual.getCount());
        Assert.assertEquals(kapList.size(), actual.getTotal());
    }

    @Test
    public void testGetActivityProfiles_happyPath_compoundOnly() {

        String compoundName = "compound";

        List<ActivityProfile> kapList = new ArrayList<>();
        ActivityProfile kap = new ActivityProfile();
        kap.setId(33L);
        kapList.add(kap);
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(kapList);
        doReturn(expectedPage).when(mockActivityProfileService)
            .getActivityProfilesForCompound(eq(compoundName), any(Pageable.class));

        PageRequest pageInfo = PageRequest.of(0, 20);
        PagedDataRep<ActivityProfile> actual = controller.getActivityProfiles(
            compoundName, null, null, pageInfo);

        Assert.assertEquals(0, actual.getStart());
        Assert.assertEquals(kapList.size(), actual.getCount());
        Assert.assertEquals(kapList.size(), actual.getTotal());
    }

    @Test
    public void testGetActivityProfiles_happyPath_kinaseAndActivityProfile() {

        long kinaseId = 42;
        String kinaseDiscoverx = "discoverx";

        Kinase kinase = new Kinase();
        kinase.setId(kinaseId);
        kinase.setDiscoverxGeneSymbol(kinaseDiscoverx);
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinaseDiscoverx));

        List<ActivityProfile> kapList = new ArrayList<>();
        ActivityProfile kap = new ActivityProfile();
        kap.setId(33L);
        kapList.add(kap);
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(kapList);
        doReturn(expectedPage).when(mockActivityProfileService)
            .getActivityProfilesForKinaseAndPercentControl(eq(kinaseId), anyDouble(), any(Pageable.class));

        PageRequest pageInfo = PageRequest.of(0, 20);
        PagedDataRep<ActivityProfile> actual = controller.getActivityProfiles(
            null, kinaseDiscoverx, 0.3, pageInfo);

        Assert.assertEquals(0, actual.getStart());
        Assert.assertEquals(kapList.size(), actual.getCount());
        Assert.assertEquals(kapList.size(), actual.getTotal());
    }

    @Test
    public void testGetActivityProfiles_happyPath_compoundAndKinaseAndActivityProfile() {

        String compoundName = "compound";
        long kinaseId = 42;
        String kinaseDiscoverx = "discoverx";

        Kinase kinase = new Kinase();
        kinase.setId(kinaseId);
        kinase.setDiscoverxGeneSymbol(kinaseDiscoverx);
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinaseDiscoverx));

        List<ActivityProfile> kapList = new ArrayList<>();
        ActivityProfile kap = new ActivityProfile();
        kap.setId(33L);
        kapList.add(kap);
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(kapList);
        doReturn(expectedPage).when(mockActivityProfileService)
            .getActivityProfilesForCompoundAndKinaseAndPercentControl(eq(compoundName),
                eq(kinaseId), anyDouble(), any(Pageable.class));

        PageRequest pageInfo = PageRequest.of(0, 20);
        PagedDataRep<ActivityProfile> actual = controller.getActivityProfiles(
            compoundName, kinaseDiscoverx, 0.3, pageInfo);

        Assert.assertEquals(0, actual.getStart());
        Assert.assertEquals(kapList.size(), actual.getCount());
        Assert.assertEquals(kapList.size(), actual.getTotal());
    }
}
