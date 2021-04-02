package org.sgc.rak.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetKinase_entrezName() {

        Kinase kinase = TestUtil.createKinase(DISCOVERX, ENTREZ);
        List<Kinase> expected = Collections.singletonList(kinase);
        doReturn(expected).when(mockKinaseDao).getKinases(eq(ENTREZ));

        List<Kinase> actual = service.getKinase(ENTREZ);
        TestUtil.assertKinasesEqual(expected, actual);
    }

    @Test
    public void testGetKinaseByDiscoverx() {

        Kinase kinase = TestUtil.createKinase(DISCOVERX, ENTREZ);
        doReturn(kinase).when(mockKinaseDao).getKinaseByDiscoverx(eq(DISCOVERX));

        Kinase actual = service.getKinaseByDiscoverx(DISCOVERX);
        TestUtil.assertKinasesEqual(kinase, actual);
    }

    @Test
    public void testGetKinases_pageable() {

        Sort sort = Sort.by(Sort.Order.desc("discoverxGeneSymbol"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Kinase> expectedKinases = Collections.singletonList(TestUtil.createKinase(DISCOVERX, ENTREZ));
        PageImpl<Kinase> expectedPage = new PageImpl<>(expectedKinases, pr, 1);
        doReturn(expectedPage).when(mockKinaseDao).getKinases(any(Pageable.class));

        Page<Kinase> actualKinases = service.getKinases(null, pr);
        Assertions.assertEquals(1, actualKinases.getNumberOfElements());
        Assertions.assertEquals(1, actualKinases.getTotalElements());
        Assertions.assertEquals(1, actualKinases.getTotalPages());
        for (int i = 0; i < expectedKinases.size(); i++) {
            TestUtil.assertKinasesEqual(expectedKinases.get(i), actualKinases.getContent().get(i));
        }
    }

    @Test
    public void testGetKinasesByEntrezGeneSymbolStartingWith() {

        Sort sort = Sort.by(Sort.Order.desc("entrezGeneSymbol"));
        PageRequest pr = PageRequest.of(0, 20, sort);

        List<Kinase> expectedKinases = Collections.singletonList(TestUtil.createKinase(DISCOVERX, ENTREZ));
        PageImpl<Kinase> expectedPage = new PageImpl<>(expectedKinases, pr, 1);
        doReturn(expectedPage).when(mockKinaseDao).getKinasesByEntrezGeneSymbolStartingWith(
            eq(ENTREZ), any(Pageable.class));

        Page<Kinase> actualKinases = service.getKinases(ENTREZ, pr);
        Assertions.assertEquals(1, actualKinases.getNumberOfElements());
        Assertions.assertEquals(1, actualKinases.getTotalElements());
        Assertions.assertEquals(1, actualKinases.getTotalPages());
        for (int i = 0; i < expectedKinases.size(); i++) {
            TestUtil.assertKinasesEqual(expectedKinases.get(i), actualKinases.getContent().get(i));
        }
    }
}
