package org.sgc.rak.dao;

import org.sgc.rak.model.NanoBretActivityProfile;
import org.sgc.rak.repositories.NanoBretActivityProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * DAO for manipulating NanoBRET activity profiles.
 */
public class NanoBretActivityProfileDao {

    private final NanoBretActivityProfileRepository activityProfileRepository;

    @Autowired
    public NanoBretActivityProfileDao(NanoBretActivityProfileRepository repository) {
        this.activityProfileRepository = repository;
    }

    /**
     * Fetches all kinase activity profiles that match one of a number of compound name/entrez pairs.
     *
     * @param compoundNames The compound names to check for.  This should be the same length as {@code entrezes} and
     *        {@code dates}.
     * @param discoverxes The discoverx gene symbols to check for.  This should be the same length as
     *        {@code compoundNames} and {@code dates}.
     * @param dates The dates to check for.  This should be the same length as {@code compoundNames} and
     *        {@code entrezes}.
     * @return The found activity profiles.  This may be empty, but will never be {@code null}.
     */
    public Set<NanoBretActivityProfile> getNanoBretActivityProfiles(List<String> compoundNames,
                                                                    List<String> discoverxes, List<Date> dates) {

        if (compoundNames.size() != discoverxes.size()) {
            throw new IllegalStateException("List of compound names and discoverx gene symbols aren't the same length");
        }
        if (compoundNames.size() != dates.size()) {
            throw new IllegalStateException("List of compound names and dates aren't the same length");
        }

        Set<NanoBretActivityProfile> profiles = new HashSet<>();

        for (int i = 0; i < compoundNames.size(); i++) {

            String compoundName = compoundNames.get(i);
            String discoverx = discoverxes.get(i);
            Date date = dates.get(i);

            Optional<NanoBretActivityProfile> possibleProfile = activityProfileRepository
                .findByCompoundNameAndKinaseDiscoverxGeneSymbolAndDate(compoundName, discoverx, date);
            possibleProfile.ifPresent(profiles::add);
        }

        return profiles;
    }

    /**
     * Saves a collection of activity profiles.
     *
     * @param activityProfiles The activity profiles to save.
     */
    public void save(Iterable<NanoBretActivityProfile> activityProfiles) {
        activityProfileRepository.saveAll(activityProfiles);
    }
}
