package org.sgc.rak.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.CompoundCountPair;
import org.sgc.rak.repositories.CompoundRepository;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
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
    private EntityManager entityManager;

    @InjectMocks
    private final CompoundDao compoundDao = new CompoundDao();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCompound() {

        String expectedCompoundName = "compoundA";

        Compound expectedCompound = TestUtil.createCompound(expectedCompoundName);
        Optional<Compound> optional = Optional.of(expectedCompound);
        doReturn(optional).when(compoundRepository).findById(eq(expectedCompoundName));

        Compound compound = compoundDao.getCompound(expectedCompoundName);
        Assertions.assertEquals(expectedCompoundName, compound.getCompoundName());
    }

    @Test
    public void testGetCompoundExists() {

        String expectedCompoundName = "compoundA";

        doReturn(true).when(compoundRepository).existsById(eq(expectedCompoundName));
        Assertions.assertTrue(compoundDao.getCompoundExists(expectedCompoundName));
    }

    @Test
    public void testGetCompounds_pageable() {

        Compound compound1 = TestUtil.createCompound("compoundA");
        Compound compound2 = TestUtil.createCompound("compoundB");
        Page<Compound> expectedPage = new PageImpl<>(Arrays.asList(compound1, compound2));
        //doReturn(expectedPage).when(compoundRepository).findAll(any(Pageable.class));
        doReturn(expectedPage).when(compoundRepository).findAll(any(), any(Pageable.class));

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<Compound> actualPage = compoundDao.getCompounds(null, pageInfo, true);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testGetCompounds_list() {

        Compound compound1 = TestUtil.createCompound("compoundA");
        Compound compound2 = TestUtil.createCompound("compoundB");
        List<Compound> expectedList = Arrays.asList(compound1, compound2);
        doReturn(expectedList).when(compoundRepository).findByCompoundNameInIgnoreCase(any());

        List<String> compoundNames = Arrays.asList(
            compound1.getCompoundName(),
            compound2.getCompoundName()
        );
        List<Compound> actualList = compoundDao.getCompounds(compoundNames);

        compareLists(expectedList, actualList);
    }

    @Test
    public void testGetCompoundsMissingActivityProfiles() {

        Compound compound1 = TestUtil.createCompound("compoundA");
        Compound compound2 = TestUtil.createCompound("compoundB");
        Page<Compound> expectedPage = new PageImpl<>(Arrays.asList(compound1, compound2));
        doReturn(expectedPage).when(compoundRepository).findAll(any(), any(Pageable.class));

        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "compoundName"),
            new Sort.Order(Sort.Direction.ASC, "count"));
        Pageable pageInfo = PageRequest.of(0, 20, sort);
        Page<Compound> actualPage = compoundDao.getCompoundsMissingPublicationInfo("foo", pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testGetCompoundsMissingPublicationInfo() {

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
        Page<CompoundCountPair> actualPage = compoundDao.getCompoundsMissingActivityProfiles("foo", pageInfo);

        Assertions.assertEquals(expectedResult.size(), actualPage.getNumberOfElements());
        Assertions.assertEquals(601, actualPage.getTotalElements());
        for (int i = 0; i < expectedResult.size(); i++) {
            Object[] temp = expectedResult.get(i);
            CompoundCountPair expected = new CompoundCountPair(temp[0].toString(), ((Number)temp[1]).intValue());
            CompoundCountPair actual = actualPage.getContent().get(i);
            Assertions.assertEquals(expected.getCompoundName(), actual.getCompoundName());
            Assertions.assertEquals(expected.getCount(), actual.getCount());
        }
    }

    @Test
    public void testGetHiddenCompounds() {

        Compound compound1 = TestUtil.createCompound("compoundA");
        Compound compound2 = TestUtil.createCompound("compoundB");
        Page<Compound> expectedPage = new PageImpl<>(Arrays.asList(compound1, compound2));
        doReturn(expectedPage).when(compoundRepository).findAll(any(), any(Pageable.class));

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<Compound> actualPage = compoundDao.getHiddenCompounds("foo", pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testGetIncompleteCompounds() {

        Compound compound1 = TestUtil.createCompound("compoundA");
        Compound compound2 = TestUtil.createCompound("compoundB");
        Page<Compound> expectedPage = new PageImpl<>(Arrays.asList(compound1, compound2));
        doReturn(expectedPage).when(compoundRepository).findAll(any(), any(Pageable.class));

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<Compound> actualPage = compoundDao.getIncompleteCompounds("foo", pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testSave_single() {
        compoundDao.save((Compound)null);
        verify(compoundRepository, times(1)).save(any());
    }

    @Test
    public void testSave_multiple() {
        compoundDao.save(Collections.emptyList());
        verify(compoundRepository, times(1)).saveAll(any());
    }

    @Test
    public void testSortToOrderBy_oneOrder_compoundName() {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "compoundName"));
        Assertions.assertEquals("order by compound_nm DESC", CompoundDao.sortToOrderBy(sort));
    }

    @Test
    public void testSortToOrderBy_oneOrder_count() {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "count"));
        Assertions.assertEquals("order by count ASC", CompoundDao.sortToOrderBy(sort));
    }

    @Test
    public void testSortToOrderBy_twoOrders() {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "compoundName"),
            new Sort.Order(Sort.Direction.ASC, "count"));
        Assertions.assertEquals("order by compound_nm DESC, count ASC", CompoundDao.sortToOrderBy(sort));
    }

    private static void compareLists(List<Compound> expectedList, List<Compound> actualList) {
        Assertions.assertEquals(expectedList.size(), actualList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            String expectedName = expectedList.get(i).getCompoundName();
            String actualName = actualList.get(i).getCompoundName();
            Assertions.assertEquals(expectedName, actualName);
        }
    }

    private static void comparePages(Page<Compound> expectedPage, Page<Compound> actualPage) {
        Assertions.assertEquals(expectedPage.getNumberOfElements(), actualPage.getNumberOfElements());
        for (int i = 0; i < expectedPage.getNumberOfElements(); i++) {
            String expectedName = expectedPage.getContent().get(i).getCompoundName();
            String actualName = actualPage.getContent().get(i).getCompoundName();
            Assertions.assertEquals(expectedName, actualName);
        }
    }
}
