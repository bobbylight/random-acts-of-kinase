package org.sgc.rak.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Audit;
import org.sgc.rak.model.AuditAction;
import org.sgc.rak.model.ModelConstants;
import org.sgc.rak.repositories.AuditRepository;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuditServiceTest {

    @Mock
    private AuditRepository mockRepository;

    @InjectMocks
    private AuditService service;

    private static final String USER_NAME = "gclooney";
    private static final String IP_ADDRESS = "1.2.3.4";
    private static final AuditAction ACTION = AuditAction.LOGIN;

    @Before
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        when(mockRepository.save(any())).thenAnswer(invocation -> {
            Audit audit = (Audit)invocation.getArguments()[0];
            audit.setId(1L);
            return audit;
        });
    }

    @After
    public void tearDown() {
        // Some tests populate RequestContextHolder.  Make sure it's clear for the next guy.
        RequestContextHolder.resetRequestAttributes();
    }

    private static String getStringOfLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append('a');
        }
        return sb.toString();
    }

    @Test
    public void testAuditService_createAudit_2Arg() {
        service.createAudit(USER_NAME, ACTION);
        verify(mockRepository, times(1)).save(any());
    }

    @Test
    public void testAuditService_createAudit_3Arg_requestThread() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteUser(USER_NAME);
        request.setRemoteAddr(IP_ADDRESS);

        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        Audit audit = service.createAudit(null, ACTION, true);
        verify(mockRepository, times(1)).save(any());
        Assert.assertEquals(USER_NAME, audit.getUserName());
        Assert.assertEquals(IP_ADDRESS, audit.getIpAddress());
    }

    @Test
    public void testAuditService_createAudit_3Arg_notRequestThread() {

        Audit audit = service.createAudit(null, ACTION, true);
        verify(mockRepository, times(1)).save(any());
        Assert.assertEquals(AuditService.UNKNOWN_USER_NAME, audit.getUserName());
        Assert.assertNull(audit.getIpAddress());
    }

    @Test
    public void testAuditService_createAudit_4Arg_notRequestThread() {

        Audit audit = service.createAudit(null, ACTION, true, "details");
        verify(mockRepository, times(1)).save(any());
        Assert.assertEquals(AuditService.UNKNOWN_USER_NAME, audit.getUserName());
        Assert.assertNull(audit.getIpAddress());
        Assert.assertEquals("details", audit.getDetails());
    }

    @Test
    public void testAuditService_createAudit_4Arg_tooLongDetailsIsTrimmed() {

        String details = getStringOfLength(ModelConstants.AUDIT_DETAILS_MAX + 1);

        Audit audit = service.createAudit(null, ACTION, true, details);
        Assert.assertEquals(ModelConstants.AUDIT_DETAILS_MAX, audit.getDetails().length());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAudits() {

        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Audit> posts = Collections.singletonList(TestUtil.createAudit(USER_NAME, ACTION, true));
        PageImpl<Audit> expectedPage = new PageImpl<>(posts, pr, 1);
        doReturn(expectedPage).when(mockRepository).findAll(any(Specification.class), any(Pageable.class));

        Page<Audit> actualPosts = service.getAudits(pr, null, null, null, null, null, null);
        Assert.assertEquals(1, actualPosts.getNumberOfElements());
        Assert.assertEquals(1, actualPosts.getTotalElements());
        Assert.assertEquals(1, actualPosts.getTotalPages());
        for (int i = 0; i < posts.size(); i++) {
            TestUtil.assertAuditsEqual(posts.get(i), actualPosts.getContent().get(i));
        }
    }
}
