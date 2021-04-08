package org.sgc.rak.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sgc.rak.core.Application;
import org.sgc.rak.model.Compound;
import org.sgc.rak.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { Application.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CompoundControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Disabled("Not yet implemented")
    public void testGetCompound() {
    }

    @Test
    @Disabled("Not yet implemented")
    public void testGetCompounds() {
    }

    @Test
    @Disabled("Not yet implemented")
    public void testGetCompoundImageAsPng() {
    }

    @Test
    @Disabled("Not yet implemented")
    public void testGetCompoundImageAsSvg() {
    }

    @WithMockUser // Defaults to a non-admin user
    @Test
    public void testUpdateCompound_unauthenticatedUsersCantUpdate() throws Exception {

        String compoundName = "compoundA";
        String url = "/api/compounds/" + compoundName;

        Compound requestBodyCompound = TestUtil.createCompound(compoundName);
        String requestBody = objectMapper.writeValueAsString(requestBodyCompound);

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            try {
                mockMvc.perform(put(url)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                    .andDo(print());
            } catch (NestedServletException e) {
                throw e.getCause(); // For tests that test failure paths, throw the underlying exception
            }
        });
    }

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void testUpdateCompound_adminUsersCanUpdate() throws Exception {

        String compoundName = "compoundA";
        String url = "/api/compounds/" + compoundName;

        Compound requestBodyCompound = TestUtil.createCompound(compoundName);
        String requestBody = objectMapper.writeValueAsString(requestBodyCompound);

        mockMvc.perform(put(url)
            .content(requestBody)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
    }
}
