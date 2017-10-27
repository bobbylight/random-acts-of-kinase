package org.sgc.rak.services;

import org.sgc.rak.dao.KinaseDao;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Kinase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for manipulating kinases.
 */
@Service
public class KinaseService {

    @Autowired
    private KinaseDao kinaseDao;

    @Autowired
    private Messages messages;

    /**
     * Returns kinase information.
     *
     * @param discoverx An optional filter.  If non-{@code null}, only kinases
     *        whose discoverx gene symbols start with this string (ignoring
     *        case) are returned.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinases.
     */
    public Page<Kinase> getKinases(String discoverx, Pageable pageInfo) {
        if (discoverx != null) {
            return kinaseDao.getKinasesByDiscoverxGeneSymbolStartingWith(
                discoverx, pageInfo);
        }
        return kinaseDao.getKinases(pageInfo);
    }
}
