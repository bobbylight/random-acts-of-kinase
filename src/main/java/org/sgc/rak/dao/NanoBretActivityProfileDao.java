package org.sgc.rak.dao;

import org.sgc.rak.model.NanoBretActivityProfile;
import org.sgc.rak.repositories.NanoBretActivityProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
     * Fetches all NanoBRET activity profiles that match one of a number of compound name/discoverx pairs.
     *
     * @param compoundNames The compound names to check for.  This should be the same length as {@code discoverxes}.
     * @param discoerxes The discoverx gene symbols to check for.  This should be the same length as
     *        {@code compoundNames}.
     * @return The found activity profiles.  This may be empty, but will never be {@code null}.
     */
    public Set<NanoBretActivityProfile> getActivityProfiles(List<String> compoundNames, List<String> discoerxes) {

        if (compoundNames.size() != discoerxes.size()) {
            throw new IllegalStateException("List of compound names and discoverx gene symbols aren't the same length");
        }

        Set<NanoBretActivityProfile> profiles = new HashSet<>();

        for (int i = 0; i < compoundNames.size(); i++) {

            String compoundName = compoundNames.get(i);
            String discoverx = discoerxes.get(i);

            Optional<NanoBretActivityProfile> possibleProfile = activityProfileRepository
                .findByCompoundNameAndKinaseDiscoverxGeneSymbol(compoundName, discoverx);
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
