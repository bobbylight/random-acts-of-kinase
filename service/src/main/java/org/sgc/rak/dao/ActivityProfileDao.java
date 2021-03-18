package org.sgc.rak.dao;

import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.repositories.ActivityProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.sgc.rak.util.QuerySpecifications.activityProfilesMatching;

/**
 * DAO for manipulating kinase activity profiles.
 */
public class ActivityProfileDao {

    private final ActivityProfileRepository activityProfileRepository;

    @Autowired
    public ActivityProfileDao(ActivityProfileRepository repository) {
        this.activityProfileRepository = repository;
    }

    /**
     * Fetches all kinase activity profiles that match one of a number of compound name/entrez pairs.
     *
     * @param compoundNames The compound names to check for.  This should be the same length as {@code discoverxes}.
     * @param discoverxes The discoverx gene symbols to check for.  This should be the same length as
     *        {@code compoundNames}.
     * @return The found activity profiles.  This may be empty, but will never be {@code null}.
     */
    public Set<ActivityProfile> getActivityProfiles(List<String> compoundNames, List<String> discoverxes) {

        if (compoundNames.size() != discoverxes.size()) {
            throw new IllegalStateException("List of compound names and discoverx gene symbols aren't the same length");
        }

        Set<ActivityProfile> profiles = new HashSet<>();

        for (int i = 0; i < compoundNames.size(); i++) {

            String compoundName = compoundNames.get(i);
            String discoverx = discoverxes.get(i);

            Optional<ActivityProfile> possibleProfile = activityProfileRepository
                .findByCompoundNameAndKinaseDiscoverxGeneSymbol(compoundName, discoverx);
            possibleProfile.ifPresent(profiles::add);
        }

        return profiles;
    }

    /**
     * Returns kinase activity profiles for a given compound/inhibitor, kinase and activity.
     *
     * @param compoundName The compound name.  Case is ignored.  This may be {@code null} if the returned
     *        list should not be restricted to a particular compound.
     * @param kinaseIds The kinase involved in the activity profile.  This may be {@code null} to not limit
     *        the search to one particular kinase.
     * @param percentControl The value that the percent control of the activity profile must be less than or
     *        equal to. This may be {@code null} to not restrict by percent control.
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of kinase activity profiles.
     */
    public Page<ActivityProfile> getActivityProfiles(String compoundName, List<Long> kinaseIds, Double percentControl,
                                                     Pageable pageInfo) {
        return activityProfileRepository.findAll(activityProfilesMatching(compoundName, kinaseIds, percentControl),
            pageInfo);
    }

    /**
     * Saves a collection of activity profiles.
     *
     * @param activityProfiles The activity profiles to save.
     */
    public void save(Iterable<ActivityProfile> activityProfiles) {
        activityProfileRepository.saveAll(activityProfiles);
    }
}
