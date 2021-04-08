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
import org.sgc.rak.services.ActivityProfileService;
import org.sgc.rak.services.CompoundService;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.NestedServletException;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ImportControllerTest {

    @Mock
    private CompoundService mockCompoundService;

    @Mock
    private ActivityProfileService mockActivityProfileService;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private ImportController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    public void tearDown() {
        // It seems MockMvcBuilders.standaloneSetup() populates RequestContextHolder, which breaks other test classes
        RequestContextHolder.resetRequestAttributes();
    }

    private static InputStream getCsv(String resource) {
        return ImportControllerTest.class.getResourceAsStream(resource);
    }

    @Test
    public void testImportActivityProfiles_happyPath_noOptionalParams() throws Exception {
        testImportActivityProfiles_impl("import-activity-profiles-happy-path.csv", null, null, true);
    }

    @Test
    public void testImportActivityProfiles_happyPath_commitExplicitlyFalse() throws Exception {
        testImportActivityProfiles_impl("import-activity-profiles-happy-path.csv", null, false, true);
    }

    @Test
    public void testImportActivityProfiles_happyPath_commitExplicitlyTrue() throws Exception {
        testImportActivityProfiles_impl("import-activity-profiles-happy-path.csv", null, true, true);
    }

    @Test
    public void testImportActivityProfiles_happyPath_headerRowExplicitlyFalse() throws Exception {
        testImportActivityProfiles_impl("import-activity-profiles-happy-path-no-header.csv", false, true, true);
    }

    @Test
    public void testImportActivityProfiles_error_missingAColumn() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            testImportActivityProfiles_impl("import-activity-profiles-missing-entrez-column.csv", null, false, false);
        });
    }

    @Test
    public void testImportActivityProfiles_error_notCsv() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            testImportActivityProfiles_impl("not-csv-data.csv", null, false, false);
        });
    }

    private void testImportActivityProfiles_impl(String csv, Boolean headerRow, Boolean commitParam,
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
        boolean expectedCommit = commitParam != null ? commitParam : true;

        ResultActions actions;
        try {
            actions = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file)
                .with(new PatchRequestPostProcessor())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause(); // For tests that test failure paths, throw the underlying exception
        }

        if (expectSuccess) {
            actions.andExpect(MockMvcResultMatchers.status().isOk()
            ).andReturn();

            verify(mockActivityProfileService, times(1))
                .importActivityProfiles(any(), eq(expectedCommit));
        }
    }

    @Test
    public void testImportCompounds_happyPath_noOptionalParams() throws Exception {
        testImportCompounds_impl("import-compounds-happy-path.csv", null, true);
    }

    @Test
    public void testImportCompounds_happyPath_commitExplicitlyFalse() throws Exception {
        testImportCompounds_impl("import-compounds-happy-path.csv", false, true);
    }

    @Test
    public void testImportCompounds_happyPath_commitExplicitlyTrue() throws Exception {
        testImportCompounds_impl("import-compounds-happy-path.csv", true, true);
    }

    @Test
    public void testImportCompounds_error_missingAColumn() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            testImportCompounds_impl("import-compounds-missing-chemotype-column.csv", false, false);
        });
    }

    @Test
    public void testImportCompounds_error_notCsv() throws Exception {
        Assertions.assertThrows(BadRequestException.class, () -> {
            testImportCompounds_impl("not-csv-data.csv", false, false);
        });
    }

    private void testImportCompounds_impl(String csv, Boolean commitParam, boolean expectSuccess)
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", getCsv(csv));

        String url = "/admin/api/compounds";
        if (commitParam != null) {
            url += "?commit=" + commitParam;
        }
        boolean expectedCommit = commitParam != null ? commitParam : true;

        ResultActions actions;
        try {
            actions = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file)
                .with(new PatchRequestPostProcessor())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause(); // For tests that test failure paths, throw the underlying exception
        }

        if (expectSuccess) {
            actions.andExpect(MockMvcResultMatchers.status().isOk()
            ).andReturn();

            verify(mockCompoundService, times(1))
                .importCompounds(any(), eq(expectedCommit));
        }
    }

    @Test
    public void testImportKdValues_happyPath_noOptionalParams() throws Exception {
        testImportKdValues_impl("import-kdValues-happy-path.csv", null, true);
    }

    @Test
    public void testImportKdValues_happyPath_commitExplicitlyFalse() throws Exception {
        testImportKdValues_impl("import-kdValues-happy-path.csv", false, true);
    }

    @Test
    public void testImportKdValues_happyPath_commitExplicitlyTrue() throws Exception {
        testImportKdValues_impl("import-kdValues-happy-path.csv", true, true);
    }

    @Test
    public void testImportKdValues_error_missingAColumn() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            testImportKdValues_impl("import-kdValues-missing-kd-column.csv", false, false);
        });
    }

    @Test
    public void testImportKdValues_error_notCsv() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            testImportKdValues_impl("not-csv-data.csv", false, false);
        });
    }

    private void testImportKdValues_impl(String csv, Boolean commitParam, boolean expectSuccess)
        throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", getCsv(csv));

        String url = "/admin/api/kdValues";
        if (commitParam != null) {
            url += "?commit=" + commitParam;
        }
        boolean expectedCommit = commitParam != null ? commitParam : true;

        ResultActions actions;
        try {
            actions = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file)
                .with(new PatchRequestPostProcessor())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause(); // For tests that test failure paths, throw the underlying exception
        }

        if (expectSuccess) {
            actions.andExpect(MockMvcResultMatchers.status().isOk()
            ).andReturn();

            verify(mockActivityProfileService, times(1))
                .importKdValues(any(), eq(expectedCommit));
        }
    }

    @Test
    public void testImportSScores_happyPath_noOptionalParams() throws Exception {
        testImportSScores_impl("import-sScores-happy-path.csv", null, true);
    }

    @Test
    public void testImportSScores_happyPath_commitExplicitlyFalse() throws Exception {
        testImportSScores_impl("import-sScores-happy-path.csv", false, true);
    }

    @Test
    public void testImportSScores_happyPath_commitExplicitlyTrue() throws Exception {
        testImportSScores_impl("import-sScores-happy-path.csv", true, true);
    }

    @Test
    public void testImportSScores_error_missingAColumn() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            testImportSScores_impl("import-sScores-missing-selectivityScore-column.csv", false, false);
        });
    }

    @Test
    public void testImportSScores_error_notCsv() throws Exception {
        Assertions.assertThrows(BadRequestException.class, () -> {
            testImportSScores_impl("not-csv-data.csv", false, false);
        });
    }

    private void testImportSScores_impl(String csv, Boolean commitParam, boolean expectSuccess)
        throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", getCsv(csv));

        String url = "/admin/api/sScores";
        if (commitParam != null) {
            url += "?commit=" + commitParam;
        }
        boolean expectedCommit = commitParam != null ? commitParam : true;

        ResultActions actions;
        try {
            actions = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file)
                .with(new PatchRequestPostProcessor())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
            );
        } catch (NestedServletException e) {
            throw (Exception)e.getCause(); // For tests that test failure paths, throw the underlying exception
        }

        if (expectSuccess) {
            actions.andExpect(MockMvcResultMatchers.status().isOk()
            ).andReturn();

            verify(mockCompoundService, times(1)).importCompounds(any(), eq(expectedCommit));
        }
    }

    /**
     * Modifies the mock request used by tests to be a {@code PATCH}, as expected by our API.  Unfortunately
     * Spring Test assumes multipart requests are always {@code POST}.
     */
    static class PatchRequestPostProcessor implements RequestPostProcessor {

        @Override
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            request.setMethod(HttpMethod.PATCH.name());
            return request;
        }
    }
}
