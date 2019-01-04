package org.sgc.rak.services;

import org.sgc.rak.dao.KinaseDao;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Kinase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for manipulating kinases.
 */
@Service
public class KinaseService {

    private final KinaseDao kinaseDao;

    private final Messages messages;

    @Autowired
    public KinaseService(KinaseDao kinaseDao, Messages messages) {
        this.kinaseDao = kinaseDao;
        this.messages = messages;
    }

    /**
     * Returns information on a kinase by its entrez gene symbol.
     *
     * @param entrez The entrez gene symbol.  Case is ignored.
     * @return All kinase records for that entrez gene symbol, or
     *         {@code null} if no such kinase is known.
     */
    public List<Kinase> getKinase(String entrez) {
        return kinaseDao.getKinases(entrez);
    }

    /**
     * Returns information on a kinase by its discoverx gene symbol.
     *
     * @param discoverx The discoverx gene symbol.  Case is ignored.
     * @return The kinase record, or {@code null} if no such kinase is known.
     */
    public Kinase getKinaseByDiscoverx(String discoverx) {
        return kinaseDao.getKinaseByDiscoverx(discoverx);
    }

    /**
     * Returns kinase information.
     *
     * @param entrez An optional filter.  If non-{@code null}, only kinases
     *        whose entrez gene symbols start with this string (ignoring
     *        case) are returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinases.
     */
    public Page<Kinase> getKinases(String entrez, Pageable pageInfo) {
        if (entrez != null) {
            return kinaseDao.getKinasesByEntrezGeneSymbolStartingWith(
                entrez, pageInfo);
        }
        return kinaseDao.getKinases(pageInfo);
    }
}
