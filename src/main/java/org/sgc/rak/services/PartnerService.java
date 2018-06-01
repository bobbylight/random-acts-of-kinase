package org.sgc.rak.services;

import org.sgc.rak.model.Partner;
import org.sgc.rak.repositories.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for manipulating partners.
 */
@Service
public class PartnerService {

    private final PartnerRepository repository;

    @Autowired
    public PartnerService(PartnerRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns partners.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of partners.
     */
    public Page<Partner> getPartners(Pageable pageInfo) {
        return repository.findAll(pageInfo);
    }
}
