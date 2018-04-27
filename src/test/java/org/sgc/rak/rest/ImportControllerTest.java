package org.sgc.rak.rest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.services.CompoundService;

public class ImportControllerTest {

    @Mock
    private CompoundService mockCompoundService;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private ImportController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Ignore("Add me when we've got MockMvc working")
    public void testImportActivityProfiles_happyPath() {
    }

    @Test
    @Ignore("Add me when we've got MockMvc working")
    public void testImportCompounds_happyPath() {
    }
}
