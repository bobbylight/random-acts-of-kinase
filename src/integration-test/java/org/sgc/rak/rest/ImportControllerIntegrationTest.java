package org.sgc.rak.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sgc.rak.core.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.io.InputStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { Application.class })
public class ImportControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static InputStream getCsv(String resource) {
        return ImportControllerIntegrationTest.class.getResourceAsStream(resource);
    }

    @Test
    public void testImportActivityProfiles_happyPath_noOptionalParams() throws Exception {

        ResultActions actions = testImportActivityProfiles_impl("import-activity-profiles-header-all-unmodified.csv",
            null, null, true);

        actions.andExpect(jsonPath("$.fieldStatuses", hasSize(1)));
    }

    @Test
    public void testImportActivityProfiles_happyPath_noHeader() throws Exception {

        ResultActions actions = testImportActivityProfiles_impl("import-activity-profiles-no-header-all-unmodified.csv",
            false, null, true);

        actions.andExpect(jsonPath("$.fieldStatuses", hasSize(1)));
    }

    private ResultActions testImportActivityProfiles_impl(String csv, Boolean headerRow, Boolean commitParam,
                                                 boolean expectSuccess) throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", getCsv(csv));
        boolean requestParams = false;

        String url = "/admin/api/activityProfiles";
        if (headerRow != null) {
            url += "?headerRow=" + headerRow;
            requestParams = true;
        }
        if (commitParam != null) {
            url += (requestParams ? "&" : "?") + "commit=" + commitParam;
        }

        ResultActions actions;
        try {
            actions = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file)
                .with(new ImportControllerTest.PatchRequestPostProcessor())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print());
        } catch (NestedServletException e) {
            throw (Exception)e.getCause(); // For tests that test failure paths, throw the underlying exception
        }

        if (expectSuccess) {
            actions.andExpect(status().isOk()
            ).andReturn();
        }

        return actions;
    }
}
