package org.sgc.rak.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.services.KinaseService;
import org.sgc.rak.services.NanoBretActivityProfileService;
import org.sgc.rak.util.TestUtil;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

public class NanoBretActivityProfileControllerTest {

    @Mock
    private NanoBretActivityProfileService mockNanoBretActivityProfileService;

    @Mock
    private KinaseService mockKinaseService;

    @Mock
    private Messages messages;

    @InjectMocks
    private NanoBretActivityProfileController controller;

    private MockMvc mockMvc;

    private static final String COMPOUND_NAME = "compoundA";
    private static final String KINASE_DISCOVERX = "kinaseDiscoverx";
    private static final String IC50 = "100";
    private static final String KINASE_ENTREZ = "kinaseEntrez";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @AfterEach
    public void tearDown() {
        // It seems MockMvcBuilders.standaloneSetup() populates RequestContextHolder, which breaks other test classes
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetActivityProfiles_ic50LessThanZero() {

        Assertions.assertThrows(BadRequestException.class, () -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/api/nanoBretActivityProfiles")
                    .param("kinaseEntrez", KINASE_DISCOVERX)
                    .param("ic50", "-1")
                    .param("compound", COMPOUND_NAME)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                );
            } catch (NestedServletException e) {
                throw e.getCause();
            }
        });

    }

    @Test
    public void testGetActivityProfiles_ic50LessTooLarge() {

        Assertions.assertThrows(BadRequestException.class, () -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/api/nanoBretActivityProfiles")
                    .param("kinaseEntrez", KINASE_DISCOVERX)
                    .param("ic50", "10001")
                    .param("compound", COMPOUND_NAME)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                );
            } catch (NestedServletException e) {
                throw e.getCause();
            }
        });

    }

    @Test
    public void testGetActivityProfiles_noSuchKinase() throws Exception {

        doReturn(Collections.emptyList()).when(mockKinaseService).getKinase(eq(KINASE_ENTREZ));

        Assertions.assertThrows(BadRequestException.class, () -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/api/nanoBretActivityProfiles")
                    .param("kinaseEntrez", KINASE_DISCOVERX)
                    .param("ic50", IC50)
                    .param("compound", COMPOUND_NAME)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                );
            } catch (NestedServletException e) {
                throw (Exception) e.getCause();
            }
        });
    }

    @Test
    public void testGetActivityProfiles_happyPath() throws Exception {

        Kinase kinase = TestUtil.createKinase(42L, KINASE_DISCOVERX, KINASE_ENTREZ);
        List<Kinase> kinaseList = Collections.singletonList(kinase);
        doReturn(kinaseList).when(mockKinaseService).getKinase(eq(KINASE_DISCOVERX));

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/nanoBretActivityProfiles")
                    .param("kinaseEntrez", KINASE_DISCOVERX)
                    .param("ic50", IC50)
                    .param("compound", COMPOUND_NAME)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk());
            // TODO: Do .andExpect()'s once we start returning an actual valid response
        } catch (NestedServletException e) {
            throw (Exception)e.getCause();
        }
    }
}
