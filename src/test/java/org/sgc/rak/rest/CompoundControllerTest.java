package org.sgc.rak.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Compound;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.CompoundService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

public class CompoundControllerTest {

    @Mock
    private CompoundService compoundService;

    @Mock
    private Messages messages;

    @InjectMocks
    private CompoundController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCompound_happyPath() {

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);

        doReturn(expectedCompound).when(compoundService).getCompound(anyString());

        Compound actualCompound = controller.getCompound(compoundName);
        Assert.assertEquals(compoundName, actualCompound.getCompoundName());
    }

    @Test(expected = NotFoundException.class)
    public void testGetCompound_notFound() {
        doReturn(null).when(compoundService).getCompound(anyString());
        controller.getCompound("compoundA");
    }

    @Test
    public void testGetCompounds_firstPage_nullCompoundKinaseAndActivity() {

        PageRequest pr = PageRequest.of(0, 20);

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(compoundService).getCompounds(any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, null, null, pr);
        Assert.assertEquals(0, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(1, compounds.getTotal());
        Assert.assertEquals(compoundName, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_notFirstPage_nullCompoundKinaseAndActivity() {

        PageRequest pr = PageRequest.of(1, 20);

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 21);
        doReturn(expectedPage).when(compoundService).getCompounds(any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, null, null, pr);
        Assert.assertEquals(20, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(21, compounds.getTotal());
        Assert.assertEquals(compoundName, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_firstPage_nonNullCompound() {

        PageRequest pr = PageRequest.of(0, 20);

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(compoundService).getCompoundsByCompoundName(eq(compoundName), any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(compoundName, null, null, pr);
        Assert.assertEquals(0, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(1, compounds.getTotal());
        Assert.assertEquals(compoundName, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_firstPage_nonNullKinaseAndActivity() {

        PageRequest pr = PageRequest.of(0, 20);

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(compoundService).getCompoundsByKinaseAndActivity(eq("kinase"), anyDouble(),
            any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, "kinase", 0.8, pr);
        Assert.assertEquals(0, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(1, compounds.getTotal());
        Assert.assertEquals(compoundName, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompoundSmiles() {
        Assert.assertNotNull(controller.getCompoundSmiles("compoundA"));
    }
}
