package org.sgc.rak.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.services.AuditService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LogoutSuccessHandlerTest {

    @Mock
    private AuditService mockAuditService;

    @InjectMocks
    private LogoutSuccessHandler handler;

    private static final String USER_NAME = "gclooney";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onLogoutSuccess_happyPath() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = Mockito.mock(Authentication.class);
        doReturn(USER_NAME).when(authentication).getName();

        handler.onLogoutSuccess(request, response, authentication);

        verify(mockAuditService, times(1)).createAudit(eq(USER_NAME), any());
    }
}
