package org.sgc.rak.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Compound;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.CompoundService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

public class CompoundControllerTest {

    @Mock
    private CompoundService compoundService;

    @InjectMocks
    private CompoundController controller = new CompoundController();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCompound() {

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);

        doReturn(expectedCompound).when(compoundService).getCompound(anyString());

        Compound actualCompound = controller.getCompound(compoundName);
        Assert.assertEquals(compoundName, actualCompound.getCompoundName());
    }

    @Test
    public void testGetCompounds_firstPage_nullInhibitor() {

        PageRequest pr = new PageRequest(0, 20);

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(compoundService).getCompounds(any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, pr);
        Assert.assertEquals(0, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(1, compounds.getTotal());
        Assert.assertEquals(compoundName, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_notFirstPage_nullInhibitor() {

        PageRequest pr = new PageRequest(1, 20);

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 21);
        doReturn(expectedPage).when(compoundService).getCompounds(any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(null, pr);
        Assert.assertEquals(20, compounds.getStart());
        Assert.assertEquals(1, compounds.getCount());
        Assert.assertEquals(21, compounds.getTotal());
        Assert.assertEquals(compoundName, compounds.getData().get(0).getCompoundName());
    }

    @Test
    public void testGetCompounds_firstPage_nonNullInhibitor() {

        PageRequest pr = new PageRequest(0, 20);

        String compoundName = "compoundA";
        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(compoundName);
        List<Compound> expectedResults = Collections.singletonList(expectedCompound);

        PageImpl<Compound> expectedPage = new PageImpl<>(expectedResults, pr, 1);
        doReturn(expectedPage).when(compoundService).getCompoundsByCompoundName(eq(compoundName), any(Pageable.class));

        PagedDataRep<Compound> compounds = controller.getCompounds(compoundName, pr);
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
