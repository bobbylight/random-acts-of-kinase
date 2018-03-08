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
import org.sgc.rak.model.Kinase;
import org.sgc.rak.model.KinaseActivityProfile;
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
    private ActivityProfileController controller = new ActivityProfileController();

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = BadRequestException.class)
    public void testGetKinaseActivityProfiles_kinaseWithoutActivity() {
        controller.getKinaseActivityProfiles("compound", "kinase", null, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetKinaseActivityProfiles_activityWithoutKinase() {
        controller.getKinaseActivityProfiles("compound", null, 0d, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetKinaseActivityProfiles_activityLessThanZero() {
        controller.getKinaseActivityProfiles("compound", "kinase", -1d, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetKinaseActivityProfiles_activityGreaterThanOne() {
        controller.getKinaseActivityProfiles("compound", "kinase", 2d, null);
    }

    @Test
    public void testGetKinaseActivityprofiles_happyPath_nothingSpecified() {

        List<KinaseActivityProfile> kapList = new ArrayList<>();
        KinaseActivityProfile kap = new KinaseActivityProfile();
        kap.setId(33);
        kapList.add(kap);
        PageImpl<KinaseActivityProfile> expectedPage = new PageImpl<>(kapList);
        doReturn(expectedPage).when(mockActivityProfileService)
            .getKinaseActivityProfiles(any(Pageable.class));

        PageRequest pageInfo = new PageRequest(0, 20);
        PagedDataRep<KinaseActivityProfile> actual = controller.getKinaseActivityProfiles(
            null, null, null, pageInfo);

        Assert.assertEquals(0, actual.getStart());
        Assert.assertEquals(kapList.size(), actual.getCount());
        Assert.assertEquals(kapList.size(), actual.getTotal());
    }

    @Test
    public void testGetKinaseActivityprofiles_happyPath_compoundOnly() {

        String compoundName = "compound";

        List<KinaseActivityProfile> kapList = new ArrayList<>();
        KinaseActivityProfile kap = new KinaseActivityProfile();
        kap.setId(33);
        kapList.add(kap);
        PageImpl<KinaseActivityProfile> expectedPage = new PageImpl<>(kapList);
        doReturn(expectedPage).when(mockActivityProfileService)
            .getKinaseActivityProfilesForCompound(eq(compoundName), any(Pageable.class));

        PageRequest pageInfo = new PageRequest(0, 20);
        PagedDataRep<KinaseActivityProfile> actual = controller.getKinaseActivityProfiles(
            compoundName, null, null, pageInfo);

        Assert.assertEquals(0, actual.getStart());
        Assert.assertEquals(kapList.size(), actual.getCount());
        Assert.assertEquals(kapList.size(), actual.getTotal());
    }

    @Test
    public void testGetKinaseActivityprofiles_happyPath_kinaseAndActivityProfile() {

        long kinaseId = 42;
        String kinaseDiscoverx = "discoverx";

        Kinase kinase = new Kinase();
        kinase.setId(kinaseId);
        kinase.setDiscoverxGeneSymbol(kinaseDiscoverx);
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinaseDiscoverx));

        List<KinaseActivityProfile> kapList = new ArrayList<>();
        KinaseActivityProfile kap = new KinaseActivityProfile();
        kap.setId(33);
        kapList.add(kap);
        PageImpl<KinaseActivityProfile> expectedPage = new PageImpl<>(kapList);
        doReturn(expectedPage).when(mockActivityProfileService)
            .getKinaseActivityProfilesForKinaseAndPercentControl(eq(kinaseId), anyDouble(), any(Pageable.class));

        PageRequest pageInfo = new PageRequest(0, 20);
        PagedDataRep<KinaseActivityProfile> actual = controller.getKinaseActivityProfiles(
            null, kinaseDiscoverx, 0.3, pageInfo);

        Assert.assertEquals(0, actual.getStart());
        Assert.assertEquals(kapList.size(), actual.getCount());
        Assert.assertEquals(kapList.size(), actual.getTotal());
    }

    @Test
    public void testGetKinaseActivityprofiles_happyPath_compoundAndKinaseAndActivityProfile() {

        String compoundName = "compound";
        long kinaseId = 42;
        String kinaseDiscoverx = "discoverx";

        Kinase kinase = new Kinase();
        kinase.setId(kinaseId);
        kinase.setDiscoverxGeneSymbol(kinaseDiscoverx);
        doReturn(kinase).when(mockKinaseService).getKinase(eq(kinaseDiscoverx));

        List<KinaseActivityProfile> kapList = new ArrayList<>();
        KinaseActivityProfile kap = new KinaseActivityProfile();
        kap.setId(33);
        kapList.add(kap);
        PageImpl<KinaseActivityProfile> expectedPage = new PageImpl<>(kapList);
        doReturn(expectedPage).when(mockActivityProfileService)
            .getKinaseActivityProfilesForCompoundAndKinaseAndPercentControl(eq(compoundName),
                eq(kinaseId), anyDouble(), any(Pageable.class));

        PageRequest pageInfo = new PageRequest(0, 20);
        PagedDataRep<KinaseActivityProfile> actual = controller.getKinaseActivityProfiles(
            compoundName, kinaseDiscoverx, 0.3, pageInfo);

        Assert.assertEquals(0, actual.getStart());
        Assert.assertEquals(kapList.size(), actual.getCount());
        Assert.assertEquals(kapList.size(), actual.getTotal());
    }
}
