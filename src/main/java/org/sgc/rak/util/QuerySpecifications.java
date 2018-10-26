package org.sgc.rak.util;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.model.Compound;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

/**
 * Specifications used to simplify JPA repository queries.
 */
public final class QuerySpecifications {

    private QuerySpecifications() {
        // Do nothing (comment for Sonar)
    }

    /**
     * Returns a specification that looks for any {@code Compound}s matching the following criteria:
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

        return new Specification<Compound>() {

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
                        builder.like(builder.upper(root.get("compoundName")),
                            '%' + Util.escapeForLike(compoundNamePart.toUpperCase()) + '%')
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

        return new Specification<Compound>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<Compound> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                // Main condition is any compound that is hidden
                Predicate predicate = builder.equal(root.get("hidden"), hidden);

                // If part of a compound name was specified, it must match as well
                if (StringUtils.isNotBlank(compoundNamePart)) {
                    predicate = builder.and(
                        predicate,
                        builder.like(builder.upper(root.get("compoundName")),
                            '%' + Util.escapeForLike(compoundNamePart.toUpperCase()) + '%')
                    );
                }

                return predicate;
            }
        };
    }
}
