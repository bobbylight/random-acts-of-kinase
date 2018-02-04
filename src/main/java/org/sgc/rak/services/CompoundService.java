package org.sgc.rak.services;

import org.sgc.rak.dao.CompoundDao;
import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Compound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for manipulating compounds.
 */
@Service
public class CompoundService {

    @Autowired
    private CompoundDao compoundDao;

    @Autowired
    private Messages messages;

    /**
     * Returns information on a specific compound.
     *
     * @param compoundName The compound name, ignoring case.
     * @return Information on the compound.  If no such compound is known, an exception
     *         is thrown.
     */
    public Compound getCompound(String compoundName) {
        Compound compound = compoundDao.getCompound(compoundName);
        if (compound == null) {
            throw new NotFoundException(messages.get("error.noSuchCompound", compoundName));
        }
        return compound;
    }

    /**
     * Returns compound information.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompoundsByCompoundName(String, Pageable)
     * @see #getIncompleteSmilesStrings(Pageable)
     */
    public Page<Compound> getCompounds(Pageable pageInfo) {
        return compoundDao.getCompounds(pageInfo);
    }

    /**
     * Returns compounds whose names start with a given string, ignoring case.
     *
     * @param compoundNamePart The start of a compound name.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompounds(Pageable)
     */
    public Page<Compound> getCompoundsByCompoundName(String compoundNamePart,
                                                     Pageable pageInfo) {
        return compoundDao.getCompoundsByCompoundNameStartsWithIgnoreCase(compoundNamePart, pageInfo);
    }

    /**
     * Returns information about compounds without SMILES strings, or any other missing fields.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of compounds.
     * @see #getCompounds(Pageable)
     */
    public Page<Compound> getIncompleteSmilesStrings(Pageable pageInfo) {
        return compoundDao.getIncompleteCompounds(pageInfo);
    }
}
