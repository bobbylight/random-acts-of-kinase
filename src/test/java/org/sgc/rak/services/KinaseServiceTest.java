package org.sgc.rak.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.dao.KinaseDao;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

public class KinaseServiceTest {

    @Mock
    private KinaseDao mockKinaseDao;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private KinaseService service;

    private static final String DISCOVERX = "discoverxA";
    private static final String ENTREZ = "entrezA";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetKinase() {

        Kinase kinase = TestUtil.createKinase(DISCOVERX, ENTREZ);
        doReturn(kinase).when(mockKinaseDao).getKinase(eq(DISCOVERX));

        Kinase actual = service.getKinase(DISCOVERX);
        TestUtil.assertKinasesEqual(kinase, actual);
    }

    @Test
    public void testGetKinases_discoverxFilterSupplied() {

        Sort sort = Sort.by(Sort.Order.desc("discoverxGeneSymbol"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Kinase> expectedKinases = Collections.singletonList(TestUtil.createKinase(DISCOVERX, ENTREZ));
        PageImpl<Kinase> expectedPage = new PageImpl<>(expectedKinases, pr, 1);
        doReturn(expectedPage).when(mockKinaseDao).getKinasesByDiscoverxGeneSymbolStartingWith(
            eq(DISCOVERX), any(Pageable.class));

        Page<Kinase> actualKinases = service.getKinases(DISCOVERX, pr);
        Assert.assertEquals(1, actualKinases.getNumberOfElements());
        Assert.assertEquals(1, actualKinases.getTotalElements());
        Assert.assertEquals(1, actualKinases.getTotalPages());
        for (int i = 0; i < expectedKinases.size(); i++) {
            TestUtil.assertKinasesEqual(expectedKinases.get(i), actualKinases.getContent().get(i));
        }
    }

    @Test
    public void testGetKinases_noDiscoverxFilter() {

        Sort sort = Sort.by(Sort.Order.desc("discoverxGeneSymbol"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Kinase> expectedKinases = Collections.singletonList(TestUtil.createKinase(DISCOVERX, ENTREZ));
        PageImpl<Kinase> expectedPage = new PageImpl<>(expectedKinases, pr, 1);
        doReturn(expectedPage).when(mockKinaseDao).getKinases(any(Pageable.class));

        Page<Kinase> actualKinases = service.getKinases(null, pr);
        Assert.assertEquals(1, actualKinases.getNumberOfElements());
        Assert.assertEquals(1, actualKinases.getTotalElements());
        Assert.assertEquals(1, actualKinases.getTotalPages());
        for (int i = 0; i < expectedKinases.size(); i++) {
            TestUtil.assertKinasesEqual(expectedKinases.get(i), actualKinases.getContent().get(i));
        }
    }
}
