package org.sgc.rak.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.InternalServerErrorException;
import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Compound;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.CompoundService;
import org.sgc.rak.util.ImageTranscoder;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class CompoundControllerTest {

    @Mock
    private CompoundService mockCompoundService;

    @Mock
    private ImageTranscoder mockImageTranscoder;

    @Mock
    private Messages messages;

    @InjectMocks
    private CompoundController controller;

    private static final String COMPOUND_NAME = "compoundA";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCompound_happyPath() {

        Compound expectedCompound = TestUtil.createCompound(COMPOUND_NAME);

        doReturn(expectedCompound).when(mockCompoundService).getCompound(anyString());

        Compound actualCompound = controller.getCompound(COMPOUND_NAME);
        Assert.assertEquals(COMPOUND_NAME, actualCompound.getCompoundName());
    }

    @Test(expected = NotFoundException.class)
    public void testGetCompound_notFound() {
        doReturn(null).when(mockCompoundService).getCompound(anyString());
        controller.getCompound("compoundA");
    }

    @Test
    public void testGetCompounds_firstPage_nullCompoundKinaseKdAndActivity() {

        PageRequest pr = PageRequest.of(0, 20);

        Compound expectedCompound = TestUtil.createCompound(COMPOUND_NAME);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(mockCompoundService).getCompounds(any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, null, null, null, pr);
        Assert.assertEquals(0, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(1, compounds.getTotal());
        Assert.assertEquals(COMPOUND_NAME, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_notFirstPage_nullCompoundKinaseKdAndActivity() {

        PageRequest pr = PageRequest.of(1, 20);

        Compound expectedCompound = TestUtil.createCompound(COMPOUND_NAME);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 21);
        doReturn(expectedPage).when(mockCompoundService).getCompounds(any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, null, null, null, pr);
        Assert.assertEquals(20, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(21, compounds.getTotal());
        Assert.assertEquals(COMPOUND_NAME, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_firstPage_nonNullCompound() {

        PageRequest pr = PageRequest.of(0, 20);

        Compound expectedCompound = TestUtil.createCompound(COMPOUND_NAME);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(mockCompoundService).getCompoundsByCompoundName(eq(COMPOUND_NAME), any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(COMPOUND_NAME, null, null, null, pr);
        Assert.assertEquals(0, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(1, compounds.getTotal());
        Assert.assertEquals(COMPOUND_NAME, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_firstPage_nonNullKinaseAndActivity() {

        PageRequest pr = PageRequest.of(0, 20);

        Compound expectedCompound = TestUtil.createCompound(COMPOUND_NAME);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(mockCompoundService).getCompoundsByKinaseAndActivity(eq("kinase"), anyDouble(),
            any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, "kinase", 0.8, null, pr);
        Assert.assertEquals(0, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(1, compounds.getTotal());
        Assert.assertEquals(COMPOUND_NAME, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_firstPage_nonNullKinaseAndKd() {

        PageRequest pr = PageRequest.of(0, 20);

        Compound expectedCompound = TestUtil.createCompound(COMPOUND_NAME);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(mockCompoundService).getCompoundsByKinaseAndKd(eq("kinase"), anyDouble(),
            any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, "kinase", null, 42d, pr);
        Assert.assertEquals(0, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(1, compounds.getTotal());
        Assert.assertEquals(COMPOUND_NAME, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompoundImageAsPng() throws Exception {

        doReturn(new byte[0]).when(mockImageTranscoder).svgToPng(anyString(), any(), anyFloat(), anyFloat());

        Assert.assertNotNull(controller.getCompoundImageAsPng("compoundA", 200, 200));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testGetCompoundImageAsPng_errorGeneratingPng() throws Exception {

        doThrow(new IOException()).when(mockImageTranscoder).svgToPng(anyString(), any(), anyFloat(), anyFloat());

        controller.getCompoundImageAsPng("compoundA", 200, 200);
    }

    @Test
    public void testGetCompoundImageAsSvg() {
        Assert.assertNotNull(controller.getCompoundImageAsSvg("compoundA"));
    }
}
