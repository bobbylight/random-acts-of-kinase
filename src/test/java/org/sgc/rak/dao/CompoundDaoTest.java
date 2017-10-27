package org.sgc.rak.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Compound;
import org.sgc.rak.repositories.CompoundRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

/**
 * Unit tests for the {@code CompoundDao} class.  This is really just testing
 * the pass-through of queries to the repository.
 */
public class CompoundDaoTest {

    @Mock
    private CompoundRepository compoundRepository;

    @InjectMocks
    private CompoundDao compoundDao = new CompoundDao();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCompound() {

        String expectedCompoundName = "compoundA";

        Compound expectedCompound = new Compound();
        expectedCompound.setCompoundName(expectedCompoundName);
        doReturn(expectedCompound).when(compoundRepository).findOne(eq(expectedCompoundName));

        Compound compound = compoundDao.getCompound(expectedCompoundName);
        Assert.assertEquals(expectedCompoundName, compound.getCompoundName());
    }

    @Test
    public void testGetCompounds() {

        Compound compound1 = new Compound();
        compound1.setCompoundName("compoundA");
        Compound compound2= new Compound();
        compound2.setCompoundName("compoundB");
        Page<Compound> expectedPage = new PageImpl<>(Arrays.asList(compound1, compound2));
        //doReturn(expectedPage).when(compoundRepository).findAll(any(Pageable.class));
        doReturn(expectedPage).when(compoundRepository).findSourceIsNull(any(Pageable.class));

        Pageable pageInfo = new PageRequest(0, 20);
        Page<Compound> actualPage = compoundDao.getCompounds(pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void getCompoundsByCompoundNameStartsWithIgnoreCase() {

        String startsWith = "comp";

        Compound compound1 = new Compound();
        compound1.setCompoundName("compoundA");
        Compound compound2= new Compound();
        compound2.setCompoundName("compoundB");
        Page<Compound> expectedPage = new PageImpl<>(Arrays.asList(compound1, compound2));
        doReturn(expectedPage).when(compoundRepository).getCompoundsByCompoundNameStartsWithIgnoreCaseAndSourceIsNull(
            eq(startsWith), any(Pageable.class));

        Pageable pageInfo = new PageRequest(0, 20);
        Page<Compound> actualPage = compoundDao.getCompoundsByCompoundNameStartsWithIgnoreCase(startsWith, pageInfo);

        comparePages(expectedPage, actualPage);
    }

    private static void comparePages(Page<Compound> expectedPage, Page<Compound> actualPage) {
        Assert.assertEquals(expectedPage.getNumberOfElements(), actualPage.getNumberOfElements());
        for (int i = 0; i < expectedPage.getNumberOfElements(); i++) {
            String expectedName = expectedPage.getContent().get(i).getCompoundName();
            String actualName = expectedPage.getContent().get(i).getCompoundName();
            Assert.assertEquals(expectedName, actualName);
        }
    }
}
