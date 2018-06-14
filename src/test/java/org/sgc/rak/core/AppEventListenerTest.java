package org.sgc.rak.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.services.AuditService;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AppEventListenerTest {

    @Mock
    private AuditService mockAuditService;

    @InjectMocks
    private AppEventListener listener;

    private static final String USER_NAME = "gclooney";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleAuthenticationSuccessEvent() {

        AuthenticationSuccessEvent e = Mockito.mock(AuthenticationSuccessEvent.class);
        Authentication auth = Mockito.mock(Authentication.class);
        doReturn(auth).when(e).getAuthentication();

        User user = new User(USER_NAME, "0ceans11", Collections.emptyList());
        doReturn(user).when(auth).getPrincipal();

        listener.handle(e);
        verify(mockAuditService, times(1)).createAudit(eq(USER_NAME), any());
    }

    @Test
    public void testHandleAuthenticationFailureBadCredentialsEvent() {

        AuthenticationFailureBadCredentialsEvent e = Mockito.mock(AuthenticationFailureBadCredentialsEvent.class);
        Authentication auth = Mockito.mock(Authentication.class);
        doReturn(auth).when(e).getAuthentication();

        doReturn(USER_NAME).when(auth).getPrincipal();

        listener.handle(e);
        verify(mockAuditService, times(1)).createAudit(eq(USER_NAME), any(), eq(false));
    }
}
