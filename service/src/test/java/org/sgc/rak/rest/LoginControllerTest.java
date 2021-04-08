package org.sgc.rak.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.reps.UserRep;

import java.security.Principal;

public class LoginControllerTest extends AbstractControllerTest {

    @InjectMocks
    private LoginController controller;

    private static final String USERID = "gclooney";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUser_null() {
        Assertions.assertNull(controller.user(null));
    }

    @Test
    public void testUser_happyPath() {

        Principal principal = () -> USERID;

        UserRep rep = controller.user(principal);
        Assertions.assertEquals(USERID, rep.getUserName());
    }
}
