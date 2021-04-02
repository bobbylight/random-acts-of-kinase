package org.sgc.rak.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Partner;
import org.sgc.rak.repositories.PartnerRepository;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PartnerServiceTest {

    @Mock
    private PartnerRepository mockRepository;

    @InjectMocks
    private PartnerService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetPartners() {

        Sort sort = Sort.by(Sort.Order.desc("name"));
        PageRequest pr = PageRequest.of(0, 100, sort);

        List<Partner> partners = Collections.singletonList(
            TestUtil.createPartner(1, "DrugCo", "https://www.drugco.com"));
        PageImpl<Partner> expectedPage = new PageImpl<>(partners, pr, 1);
        doReturn(expectedPage).when(mockRepository).findAll(any(Pageable.class));

        Page<Partner> actualPartners = service.getPartners(pr);
        Assertions.assertEquals(1, actualPartners.getNumberOfElements());
        Assertions.assertEquals(1, actualPartners.getTotalElements());
        Assertions.assertEquals(1, actualPartners.getTotalPages());
        for (int i = 0; i < partners.size(); i++) {
            TestUtil.assertPartnersEqual(partners.get(i), actualPartners.getContent().get(i));
        }
    }
}
