package org.sgc.rak.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Partner;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.PartnerService;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PartnerControllerTest {

    @Mock
    private PartnerService mockService;

    @InjectMocks
    private PartnerController controller;

    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
        mapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        // It seems MockMvcBuilders.standaloneSetup() populates RequestContextHolder, which breaks other test classes
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetPartners() throws Exception {

        PageRequest pr = PageRequest.of(0, 20);

        List<Partner> partners = Collections.singletonList(
            TestUtil.createPartner(1, "DrugCo", "https://www.drugco.com"));
        PageImpl<Partner> expectedPage = new PageImpl<>(partners, pr, 1);
        doReturn(expectedPage).when(mockService).getPartners(any(Pageable.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/partners")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();

        PagedDataRep<Partner> actualPartners = mapper.readValue(result.getResponse().getContentAsString(),
            PagedDataRep.class);
        // jackson converts collections of objects to Collection<LinkedHashMap>, so we must manually deserialize
        // the list that's one-level deep
        actualPartners.setData(mapper.convertValue(actualPartners.getData(), new TypeReference<List<Partner>>() {}));
        Assert.assertEquals(0, actualPartners.getStart());
        Assert.assertEquals(1, actualPartners.getTotal());
        Assert.assertEquals(1, actualPartners.getCount());
        for (int i = 0; i < partners.size(); i++) {
            TestUtil.assertPartnersEqual(partners.get(i), actualPartners.getData().get(i));
        }
    }
}
