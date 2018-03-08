package org.sgc.rak.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.CompoundCountPair;
import org.sgc.rak.repositories.CompoundRepository;
import org.sgc.rak.repositories.KinaseRepository;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for the {@code CompoundDao} class.  This is really just testing
 * the pass-through of queries to the repository.
 */
public class CompoundDaoTest {

    @Mock
    private CompoundRepository compoundRepository;

    @Mock
    private KinaseRepository kinaseRepository;

    @Mock
    private EntityManager entityManager;

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
        Optional<Compound> optional = Optional.of(expectedCompound);
        doReturn(optional).when(compoundRepository).findById(eq(expectedCompoundName));

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

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<Compound> actualPage = compoundDao.getCompounds(pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testGetCompoundsByCompoundNameStartsWithIgnoreCase() {

        String startsWith = "comp";

        Compound compound1 = new Compound();
        compound1.setCompoundName("compoundA");
        Compound compound2= new Compound();
        compound2.setCompoundName("compoundB");
        Page<Compound> expectedPage = new PageImpl<>(Arrays.asList(compound1, compound2));
        doReturn(expectedPage).when(compoundRepository).getCompoundsByCompoundNameStartsWithIgnoreCaseAndSourceIsNull(
            eq(startsWith), any(Pageable.class));

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<Compound> actualPage = compoundDao.getCompoundsByCompoundNameStartsWithIgnoreCase(startsWith, pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testGetCompoundsMissingActivityProfiles() {

        doReturn(1000L).when(kinaseRepository).count();

        Query mockQuery = Mockito.mock(Query.class);
        doReturn(mockQuery).when(entityManager).createNativeQuery(anyString());

        // Mock the returned result set
        List<Object[]> expectedResult = Arrays.asList(
            new Object[] { "compoundA", 3 },
            new Object[] { "compoundB", 4 }
        );
        doReturn(expectedResult).when(mockQuery).getResultList();

        // Mock the query to get the total count in the result set
        BigInteger expectedTotal = new BigInteger("601");
        doReturn(expectedTotal).when(mockQuery).getSingleResult();

        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "compoundName"),
            new Sort.Order(Sort.Direction.ASC, "count"));
        Pageable pageInfo = PageRequest.of(0, 20, sort);
        Page<CompoundCountPair> actualPage = compoundDao.getCompoundsMissingActivityProfiles(pageInfo);

        Assert.assertEquals(expectedResult.size(), actualPage.getNumberOfElements());
        Assert.assertEquals(601, actualPage.getTotalElements());
        for (int i = 0; i < expectedResult.size(); i++) {
            Object[] temp = expectedResult.get(i);
            CompoundCountPair expected = new CompoundCountPair(temp[0].toString(), ((Number)temp[1]).intValue());
            CompoundCountPair actual = actualPage.getContent().get(i);
            Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
            Assert.assertEquals(expected.getCount(), actual.getCount());
        }
    }

    @Test
    public void testGetIncompleteCompounds() {

        Compound compound1 = new Compound();
        compound1.setCompoundName("compoundA");
        Compound compound2= new Compound();
        compound2.setCompoundName("compoundB");
        Page<Compound> expectedPage = new PageImpl<>(Arrays.asList(compound1, compound2));
        doReturn(expectedPage).when(compoundRepository).findSmilesIsNullOrS10IsNull(any(Pageable.class));

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<Compound> actualPage = compoundDao.getIncompleteCompounds(pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testSortToOrderBy_oneOrder_compoundName() {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "compoundName"));
        Assert.assertEquals("order by compound_nm DESC", CompoundDao.sortToOrderBy(sort));
    }

    @Test
    public void testSortToOrderBy_oneOrder_count() {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "count"));
        Assert.assertEquals("order by count ASC", CompoundDao.sortToOrderBy(sort));
    }

    @Test
    public void testSortToOrderBy_twoOrders() {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "compoundName"),
            new Sort.Order(Sort.Direction.ASC, "count"));
        Assert.assertEquals("order by compound_nm DESC, count ASC", CompoundDao.sortToOrderBy(sort));
    }

    private static void comparePages(Page<Compound> expectedPage, Page<Compound> actualPage) {
        Assert.assertEquals(expectedPage.getNumberOfElements(), actualPage.getNumberOfElements());
        for (int i = 0; i < expectedPage.getNumberOfElements(); i++) {
            String expectedName = expectedPage.getContent().get(i).getCompoundName();
            String actualName = actualPage.getContent().get(i).getCompoundName();
            Assert.assertEquals(expectedName, actualName);
        }
    }
}
