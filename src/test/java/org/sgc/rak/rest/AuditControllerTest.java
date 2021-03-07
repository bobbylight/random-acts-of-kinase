package org.sgc.rak.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.exceptions.ErrorResponse;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Audit;
import org.sgc.rak.model.AuditAction;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.AuditService;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuditControllerTest {

    @Mock
    private AuditService mockService;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private AuditController controller;

    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
        mapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        // It seems MockMvcBuilders.standaloneSetup() populates RequestContextHolder, which breaks other test classes
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetAudits_happyPath() throws Exception {

        PageRequest pr = PageRequest.of(0, 20);

        List<Audit> audits = Collections.singletonList(TestUtil.createAudit("gclooney", AuditAction.LOGIN, true));
        PageImpl<Audit> expectedPage = new PageImpl<>(audits, pr, 1);
        doReturn(expectedPage).when(mockService).getAudits(any(Pageable.class), any(), any(), any(), any(), any(), any());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/api/audits")
            .param("fromDate", "2019-01-01")
            .param("toDate", "2019-01-01")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        PagedDataRep<Audit> actualPosts = mapper.readValue(result.getResponse().getContentAsString(),
            PagedDataRep.class);
        // jackson converts collections of objects to Collection<LinkedHashMap>, so we must manually deserialize
        // the list that's one-level deep
        actualPosts.setData(mapper.convertValue(actualPosts.getData(), new TypeReference<List<Audit>>() {}));
        Assert.assertEquals(0, actualPosts.getStart());
        Assert.assertEquals(1, actualPosts.getTotal());
        Assert.assertEquals(1, actualPosts.getCount());
        for (int i = 0; i < audits.size(); i++) {
            TestUtil.assertAuditsEqual(audits.get(i), actualPosts.getData().get(i));
        }
    }

    @Test(expected = BadRequestException.class)
    public void testGetAudits_error_invalidDateParameter() throws Throwable {

        PageRequest pr = PageRequest.of(0, 20);

        List<Audit> audits = Collections.singletonList(TestUtil.createAudit("gclooney", AuditAction.LOGIN, true));
        PageImpl<Audit> expectedPage = new PageImpl<>(audits, pr, 1);
        doReturn(expectedPage).when(mockService).getAudits(any(Pageable.class), any(), any(), any(), any(), any(), any());

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/admin/api/audits")
                .param("fromDate", "xxx")
                .accept(MediaType.APPLICATION_JSON)
            );
        } catch (NestedServletException nse) {
            throw nse.getCause();
        }
    }
}
