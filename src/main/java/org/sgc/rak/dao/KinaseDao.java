package org.sgc.rak.dao;

import org.sgc.rak.model.Kinase;
import org.sgc.rak.repositories.KinaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * DAO for manipulating kinases.
 */
public class KinaseDao {

    @Autowired
    private KinaseRepository kinaseRepository;

    /**
     * Returns kinase information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinases.
     */
    public Page<Kinase> getKinases(Pageable pageInfo) {
        return kinaseRepository.findAll(pageInfo);
    }
}
