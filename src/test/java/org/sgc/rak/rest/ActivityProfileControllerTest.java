package org.sgc.rak.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.services.ActivityProfileService;

public class ActivityProfileControllerTest {

    @Mock
    private ActivityProfileService service;

    @Mock
    private Messages messages;

    @InjectMocks
    private ActivityProfileController controller = new ActivityProfileController();

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = BadRequestException.class)
    public void testGetKinaseActivityProfiles_kinaseWithoutActivity() {
        controller.getKinaseActivityProfiles("inhibitor", "kinase", null, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetKinaseActivityProfiles_activityWithoutKinase() {
        controller.getKinaseActivityProfiles("inhibitor", null, 0d, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetKinaseActivityProfiles_activityLessThanZero() {
        controller.getKinaseActivityProfiles("inhibitor", "kinase", -1d, null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetKinaseActivityProfiles_activityGreaterThanOne() {
        controller.getKinaseActivityProfiles("inhibitor", "kinase", 2d, null);
    }

    // TODO: Non-error conditions
}
