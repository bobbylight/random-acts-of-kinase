package org.sgc.rak.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.AuditAction;
import org.sgc.rak.services.AuditService;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RakAccessDeniedHandlerTest {

    @Mock
    private AuditService mockAuditService;

    @InjectMocks
    private RakAccessDeniedHandler handler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandle_queryString() throws Exception {
        testHandleImpl("?id=1");
    }

    @Test
    public void testHandle_noQueryString() throws Exception {
        testHandleImpl(null);
    }

    private void testHandleImpl(String queryString) throws Exception {

        String uri = "/customers";
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), uri);
        request.setQueryString(queryString);
        request.setRemoteUser("gclooney");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException e = new AccessDeniedException("Test exception");

        handler.handle(request, response, e);
        verify(mockAuditService, times(1)).createAudit(eq("gclooney"),
            eq(AuditAction.ACCESS_DENIED), eq(Boolean.FALSE), any());
    }
}
