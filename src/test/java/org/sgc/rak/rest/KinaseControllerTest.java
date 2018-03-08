package org.sgc.rak.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.KinaseService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

public class KinaseControllerTest {

    @Mock
    private KinaseService mockKinaseService;

    @InjectMocks
    private KinaseController controller = new KinaseController();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetKinases_happyPath() {

        PageRequest pageRequest = PageRequest.of(1, 20);

        Kinase kinase = new Kinase();
        kinase.setId(42);
        List<Kinase> kinases = Collections.singletonList(kinase);
        PageImpl<Kinase> returnedPage = new PageImpl<>(kinases, pageRequest, 21);
        doReturn(returnedPage).when(mockKinaseService).getKinases(anyString(), any());

        PagedDataRep<Kinase> response = controller.getKinases("discoverx", pageRequest);
        Assert.assertEquals(20, response.getStart());
        Assert.assertEquals(1, response.getCount());
        Assert.assertEquals(21, response.getTotal());
        Assert.assertEquals(1, response.getData().size());
        Assert.assertEquals(42, response.getData().get(0).getId());
    }
}
