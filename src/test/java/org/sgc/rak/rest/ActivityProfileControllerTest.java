package org.sgc.rak.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.services.ActivityProfileService;
import org.sgc.rak.services.KinaseService;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

public class ActivityProfileControllerTest {

    @Mock
    private ActivityProfileService mockActivityProfileService;

    @Mock
    private KinaseService mockKinaseService;

    @Mock
    private Messages messages;

    @InjectMocks
    private ActivityProfileController controller;

    private MockMvc mockMvc;

    private static final String COMPOUND_NAME = "compoundA";
    private static final String KINASE_DISCOVERX = "kinase";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @After
    public void tearDown() {
        // It seems MockMvcBuilders.standaloneSetup() populates RequestContextHolder, which breaks other test classes
        RequestContextHolder.resetRequestAttributes();
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfiles_activityLessThanZero() throws Exception {

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/activityProfiles")
                .param("kinaseDiscoverx", KINASE_DISCOVERX)
                .param("activity", "-1")
                .param("compound", COMPOUND_NAME)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfiles_activityGreaterThanOne() throws Exception {

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/activityProfiles")
                .param("kinaseDiscoverx", KINASE_DISCOVERX)
                .param("activity", "2")
                .param("compound", COMPOUND_NAME)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testGetActivityProfiles_noSuchKinase() throws Exception {

        doReturn(null).when(mockKinaseService).getKinase(eq(KINASE_DISCOVERX));

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/activityProfiles")
                .param("kinaseDiscoverx", KINASE_DISCOVERX)
                .param("activity", "1")
                .param("compound", COMPOUND_NAME)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }

    @Test
    public void testGetActivityProfiles_happyPath_nothingSpecified() throws Exception{

        List<ActivityProfile> kapList = Collections.singletonList(
            TestUtil.createActivityProfile(33L)
        );
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(kapList);

        doReturn(expectedPage).when(mockActivityProfileService)
            .getActivityProfiles(any(), any(), any(), any(Pageable.class));

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/activityProfiles")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.start", is(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count", is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", is(33)));
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }

    @Test
    public void testGetActivityProfiles_happyPath_compoundOnly() throws Exception {

        List<ActivityProfile> kapList = Collections.singletonList(
            TestUtil.createActivityProfile(33L)
        );
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(kapList);

        doReturn(expectedPage).when(mockActivityProfileService)
            .getActivityProfiles(eq(COMPOUND_NAME), any(), any(), any(Pageable.class));

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/activityProfiles")
                .param("compound", COMPOUND_NAME)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.start", is(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count", is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", is(33)));
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }

    @Test
    public void testGetActivityProfiles_happyPath_kinaseAndPercentControl() throws Exception {

        Kinase kinase = new Kinase();
        kinase.setId(42L);
        kinase.setDiscoverxGeneSymbol(KINASE_DISCOVERX);
        doReturn(kinase).when(mockKinaseService).getKinase(eq(KINASE_DISCOVERX));

        List<ActivityProfile> kapList = Collections.singletonList(
            TestUtil.createActivityProfile(33L)
        );
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(kapList);

        doReturn(expectedPage).when(mockActivityProfileService)
            .getActivityProfiles(any(), eq(42L), anyDouble(), any(Pageable.class));

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/activityProfiles")
                .param("kinaseDiscoverx", KINASE_DISCOVERX)
                .param("activity", "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.start", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", is(33)));
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }

    @Test
    public void testGetActivityProfiles_happyPath_compoundAndKinaseAndPercentControl() throws Exception {

        Kinase kinase = new Kinase();
        kinase.setId(42L);
        kinase.setDiscoverxGeneSymbol(KINASE_DISCOVERX);
        doReturn(kinase).when(mockKinaseService).getKinase(eq(KINASE_DISCOVERX));

        List<ActivityProfile> kapList = Collections.singletonList(
            TestUtil.createActivityProfile(33L)
        );
        PageImpl<ActivityProfile> expectedPage = new PageImpl<>(kapList);

        doReturn(expectedPage).when(mockActivityProfileService)
            .getActivityProfiles(eq(COMPOUND_NAME), eq(42L), anyDouble(), any(Pageable.class));

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/activityProfiles")
                .param("compound", COMPOUND_NAME)
                .param("kinaseDiscoverx", KINASE_DISCOVERX)
                .param("activity", "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.start", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", is(33)));
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }
}
