package org.sgc.rak.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.dao.KinaseDao;
import org.sgc.rak.i18n.Messages;

public class KinaseServiceTest {

    @Mock
    private KinaseDao mockKinaseDao;

    @Mock
    private Messages mockMessages;

    @InjectMocks
    private KinaseService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetKinase() {

    }

    @Test
    public void testGetKinases_filterSupplied() {

    }

    @Test
    public void testGetKinases_noFilter() {

    }
}
