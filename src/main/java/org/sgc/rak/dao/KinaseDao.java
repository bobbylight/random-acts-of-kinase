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
     * Returns information on a kinase by its discoverx gene symbol.
     *
     * @param discoverx The discoverx gene symbol.  Case is ignored.
     * @return The kinase, or {@code null} if no such kinase is known.
     */
    public Kinase getKinase(String discoverx) {
        return kinaseRepository.findOneByDiscoverxGeneSymbolIgnoreCase(discoverx);
    }

    /**
     * Returns kinase information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinases.
     */
    public Page<Kinase> getKinases(Pageable pageInfo) {
        return kinaseRepository.findAll(pageInfo);
    }

    /**
     * Returns kinase information.
     *
     * @param discoverx If non-{@code null}, only kinases whose discoverx gene
     *        symbols start with this string (ignoring case) are returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinases.
     */
    public Page<Kinase> getKinasesByDiscoverxGeneSymbolStartingWith(
            String discoverx, Pageable pageInfo) {
        return kinaseRepository.getKinasesByDiscoverxGeneSymbolStartsWithIgnoreCase(
            discoverx, pageInfo);
    }
}
