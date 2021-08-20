package org.sgc.rak.util;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Audit;
import org.sgc.rak.model.AuditAction;
import org.sgc.rak.model.Compound;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Specifications used to simplify JPA repository queries.
 */
public final class QuerySpecifications {

    private QuerySpecifications() {
        // Do nothing (comment for Sonar)
    }

    /**
     * Returns a specification that looks for any {@code ActivityProfile}s matching the given criteria.
     *
     * @param compoundName The compound name.  Case is ignored.  This may be {@code null} if the returned
     *        list should not be restricted to a particular compound.
     * @param kinaseIds The kinase involved in the activity profile.  This may be {@code null} to not limit
     *        the search to one particular kinase.
     * @param percentControl The value that the percent control of the activity profile must be less than or
     *        equal to. This may be {@code null} to not restrict by percent control.
     * @return The specification.
     */
    public static Specification<ActivityProfile> activityProfilesMatching(String compoundName, List<Long> kinaseIds,
                                                                          Double percentControl) {

        return new Specification<>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<ActivityProfile> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate predicate = null;

                if (StringUtils.isNotBlank(compoundName)) {
                    predicate = builder.like(builder.lower(root.get("compoundName")),
                        compoundName.toLowerCase(Locale.US));
                }

                if (kinaseIds != null && !kinaseIds.isEmpty()) {
                    CriteriaBuilder.In<Object> inPredicate = builder.in(root.get("kinase"));
                    for (Long kinaseId : kinaseIds) {
                        inPredicate.value(kinaseId);
                    }
                    predicate = predicate == null ? inPredicate : builder.and(predicate, inPredicate);
                }

                if (percentControl != null) {
                    Predicate pcPredicate = builder.le(root.get("percentControl"), percentControl);
                    predicate = predicate == null ? pcPredicate : builder.and(predicate, pcPredicate);
                }

                return predicate;
            }
        };
    }

    /**
     * Returns a specification that looks for any {@code Audit} records matching the given criteria.
     *
     * @param user The start of a user name to filter on.  Case is ignored.  This may be {@code null} or empty string
     *        to not filter on user.
     * @param action An action to filter on.  This may be {@code null} to not filter on action.
     * @param ipAddress The start of an IP address to filter on.  Case is ignored.  This may be {@code null} or empty
     *        string to not filter on IP address.
     * @param success Whether the action was successful.  May be {@code null} to denote to ignore this property.
     * @param fromDate The starting date from which to fetch audits.  May be {@code null}.
     * @param toDate The ending date to which to fetch audits.  May be {@code null}.
     * @return The specification.
     */
    public static Specification<Audit> auditRecordsMatching(String user, AuditAction action, String ipAddress,
                                                            Boolean success, Date fromDate, Date toDate) {

        return new Specification<>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<Audit> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate predicate = null;

                if (StringUtils.isNotBlank(user)) {
                    predicate = builder.like(builder.lower(root.get("userName")), user.toLowerCase(Locale.US) + "%");
                }

                if (action != null) { // Exact matches because no good way to do startsWith for enums
                    Predicate actionPredicate = builder.equal(root.get("action"), action);
                    predicate = predicate == null ? actionPredicate : builder.and(predicate, actionPredicate);
                }

                if (StringUtils.isNotBlank(ipAddress)) {
                    Predicate ipPredicate = builder.like(root.get("ipAddress"), ipAddress + "%");
                    predicate = predicate == null ? ipPredicate : builder.and(predicate, ipPredicate);
                }

                if (success != null) {
                    Predicate successPredicate = builder.equal(root.get("success"), success);
                    predicate = predicate == null ? successPredicate : builder.and(predicate, successPredicate);
                }

                if (fromDate != null) {
                    Predicate datePredicate = builder.greaterThanOrEqualTo(root.get("createDate"), fromDate);
                    predicate = predicate == null ? datePredicate : builder.and(predicate, datePredicate);
                }

                if (toDate != null) {
                    Predicate datePredicate = builder.lessThanOrEqualTo(root.get("createDate"), toDate);
                    predicate = predicate == null ? datePredicate : builder.and(predicate, datePredicate);
                }

                return predicate;
            }
        };
    }

    /**
     * Returns a specification that looks for any {@code Compound}s matching the following criteria.
     *
     * <ol>
     *     <li>If part of a compound name is specified, any matching compounds must contain that
     *         text in their names, ignoring case</li>
     *     <li>One or more specified fields must be {@code null}</li>
     * </ol>
     * @param compoundNamePart The optional part of a compound name that must be matched, ignoring case.
     *        This may be {@code null}.
     * @param possiblyNullFields The list of fields, of which at least 1 must be {@code null}.
     * @return The specification.
     */
    public static Specification<Compound> hasNullFields(String compoundNamePart, String... possiblyNullFields) {

        return new Specification<>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<Compound> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                // Convert the array of possibly-null field names to an array of Predicates
                Predicate[] possiblyNullFieldRestrictions = Arrays.stream(possiblyNullFields)
                    .map(f -> builder.isNull(root.get(f)))
                    .toArray(Predicate[]::new);

                // Main condition is any of those fields are null
                Predicate predicate = builder.or(possiblyNullFieldRestrictions);

                // If part of a compound name was specified, it must match as well
                if (StringUtils.isNotBlank(compoundNamePart)) {
                    predicate = builder.and(
                        predicate,
                        builder.like(builder.lower(root.get("compoundName")),
                            '%' + Util.escapeForLike(compoundNamePart.toLowerCase()) + '%')
                    );
                }

                return predicate;
            }
        };
    }

    /**
     * Returns a specification that looks for any {@code Compound}s that are either hidden or not hidden, and
     * optionally, have their name match a given pattern.
     *
     * @param compoundNamePart The optional part of a compound name that must be matched, ignoring case.
     *        This may be {@code null}.
     * @param hidden Whether to return hidden compounds (vs. not hidden compounds).
     * @return The specification.
     */
    public static Specification<Compound> isHidden(String compoundNamePart, boolean hidden) {

        return new Specification<>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<Compound> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                // Main condition is any compound that is hidden
                Predicate predicate = builder.equal(root.get("hidden"), hidden);

                // If part of a compound name was specified, it must match as well
                if (StringUtils.isNotBlank(compoundNamePart)) {
                    predicate = builder.and(
                        predicate,
                        builder.like(builder.lower(root.get("compoundName")),
                            '%' + Util.escapeForLike(compoundNamePart.toLowerCase()) + '%')
                    );
                }

                return predicate;
            }
        };
    }

    /**
     * Returns a specification that looks for any {@code Compound}s matching the given criteria.
     *
     * @param compoundNamePart The optional part of a compound name that must be matched, ignoring case.
     *        This may be {@code null}.
     * @param includeHidden Whether to include hidden compounds in the response.
     * @return The specification.
     */
    public static Specification<Compound> standardSearch(String compoundNamePart, boolean includeHidden) {

        return new Specification<>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<Compound> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate predicate = null;

                if (StringUtils.isNotBlank(compoundNamePart)) {
                    predicate = builder.like(builder.lower(root.get("compoundName")),
                            '%' + Util.escapeForLike(compoundNamePart.toLowerCase()) + '%');
                }

                if (!includeHidden) {
                    Predicate noHiddenPredicate = builder.isFalse(root.get("hidden"));
                    predicate = predicate == null ? noHiddenPredicate : builder.and(predicate, noHiddenPredicate);
                }

                return predicate;
            }
        };
    }
}
