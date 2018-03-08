package org.sgc.rak.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.repositories.KinaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

public class KinaseDaoTest {

    @Mock
    private KinaseRepository kinaseRepository;

    @InjectMocks
    private KinaseDao kinaseDao = new KinaseDao();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetKinase() {

        Kinase expected = new Kinase();
        expected.setId(42);
        doReturn(expected).when(kinaseRepository).findOneByDiscoverxGeneSymbolIgnoreCase(anyString());

        Kinase actual = kinaseDao.getKinase("discoverx");
        Assert.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void testGetKinases() {

        Kinase kinase1 = new Kinase();
        kinase1.setId(42);
        Kinase kinase2 = new Kinase();
        kinase2.setId(43);
        Page<Kinase> expectedPage = new PageImpl<>(Arrays.asList(kinase1, kinase2));
        doReturn(expectedPage).when(kinaseRepository).findAll(any(Pageable.class));

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<Kinase> actualPage = kinaseDao.getKinases(pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testGetKinasesByDiscoverxGeneSymbolStartingWith() {

        String prefix = "foo";

        Kinase kinase1 = new Kinase();
        kinase1.setId(42);
        Kinase kinase2 = new Kinase();
        kinase2.setId(43);
        Page<Kinase> expectedPage = new PageImpl<>(Arrays.asList(kinase1, kinase2));
        doReturn(expectedPage).when(kinaseRepository).getKinasesByDiscoverxGeneSymbolStartsWithIgnoreCase(
            eq(prefix), any(Pageable.class));

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<Kinase> actualPage = kinaseDao.getKinasesByDiscoverxGeneSymbolStartingWith(prefix, pageInfo);

        comparePages(expectedPage, actualPage);
    }

    private static void comparePages(Page<Kinase> expectedPage, Page<Kinase> actualPage) {
        Assert.assertEquals(expectedPage.getNumberOfElements(), actualPage.getNumberOfElements());
        for (int i = 0; i < expectedPage.getNumberOfElements(); i++) {
            long expectedId = expectedPage.getContent().get(i).getId();
            long actualId  = expectedPage.getContent().get(i).getId();
            Assert.assertEquals(expectedId, actualId);
        }
    }
}
