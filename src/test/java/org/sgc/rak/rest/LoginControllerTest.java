package org.sgc.rak.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.reps.UserRep;

import java.security.Principal;

public class LoginControllerTest extends AbstractControllerTest {

    @InjectMocks
    private LoginController controller;

    private static final String USERID = "gclooney";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUser_null() {
        Assert.assertNull(controller.user(null));
    }

    @Test
    public void testUser_happyPath() {

        Principal principal = () -> USERID;

        UserRep rep = controller.user(principal);
        Assert.assertEquals(USERID, rep.getUserName());
    }
}
