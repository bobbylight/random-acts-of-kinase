package org.sgc.rak.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sgc.rak.core.Application;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Audit;
import org.sgc.rak.model.AuditAction;
import org.sgc.rak.model.Compound;
import org.sgc.rak.repositories.ActivityProfileRepository;
import org.sgc.rak.repositories.AuditRepository;
import org.sgc.rak.repositories.CompoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.sgc.rak.util.QuerySpecifications.*;

/**
 * Unit tests for our query specifications.  These are hard to unit test, so we instead
 * use an in-memory database and essentially integration-test them.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = Application.class)
@Sql("query-specifications-test-data.sql")
public class QuerySpecificationsTest {

    @Autowired
    private CompoundRepository compoundRepository;

    @Autowired
    private ActivityProfileRepository apRepository;

    @Autowired
    private AuditRepository auditRepository;

    private static PageRequest createPageRequest(int page, int size, String sortBy) {

        String sortField = sortBy;
        boolean asc = true;
        if (sortField.contains(":")) {
            int colon = sortField.indexOf(':');
            asc = "asc".equalsIgnoreCase(sortField.substring(colon + 1));
            sortField = sortField.substring(0, colon);
        }

        Sort sort = Sort.by(asc ? Sort.Order.asc(sortField) : Sort.Order.desc(sortField));
        return  PageRequest.of(page, size, sort);
    }

    @Test
    public void testActivityProfilesMatching_happyPath_allNullParams() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching(null, null, null), pr);

        Assertions.assertEquals(3, actual.getNumberOfElements());
        Assertions.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assertions.assertEquals("compoundB", actual.getContent().get(1).getCompoundName());
        Assertions.assertEquals("compoundC", actual.getContent().get(2).getCompoundName());
    }

    @Test
    public void testActivityProfilesMatching_happyPath_compoundName() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching("compoundB", null, null), pr);

        Assertions.assertEquals(1, actual.getNumberOfElements());
        Assertions.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testActivityProfilesMatching_happyPath_kinaseId() {

        PageRequest pr = createPageRequest(0, 20, "compoundName:desc");
        List<Long> kinaseIds = Collections.singletonList(2L);
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching(null, kinaseIds, null), pr);

        Assertions.assertEquals(1, actual.getNumberOfElements());
        Assertions.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testActivityProfilesMatching_happyPath_percentControl() {

        PageRequest pr = createPageRequest(0, 20, "compoundName:desc");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching(null, null, 0.4), pr);

        Assertions.assertEquals(2, actual.getNumberOfElements());
        Assertions.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
        Assertions.assertEquals("compoundA", actual.getContent().get(1).getCompoundName());
    }

    @Test
    public void testActivityProfilesMatching_happyPath_compoundNamePercentControl() {

        PageRequest pr = createPageRequest(0, 20, "compoundName:desc");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching("compoundA", null, 0.2), pr);

        // No activity profiles with compoundA and % control 0.2 or less
        Assertions.assertEquals(0, actual.getNumberOfElements());
    }

    @Test
    public void testAuditRecordsMatching_happyPath_allNullParams() {

        PageRequest pr = createPageRequest(0, 20, "createDate:desc");
        Page<Audit> actual = auditRepository.findAll(auditRecordsMatching(null, null, null, null,
            null, null), pr);

        Assertions.assertEquals(2, actual.getNumberOfElements());
        Assertions.assertEquals("capplegate", actual.getContent().get(0).getUserName());
        Assertions.assertEquals("gclooney", actual.getContent().get(1).getUserName());
    }

    @Test
    public void testAuditRecordsMatching_happyPath_allNonNullParams() {

        PageRequest pr = createPageRequest(0, 20, "createDate:desc");
        Page<Audit> actual = auditRepository.findAll(auditRecordsMatching("gclooney", AuditAction.LOGIN, "1.2.3.4",
            true, new Date(0), new Date()), pr);

        Assertions.assertEquals(1, actual.getNumberOfElements());
        Assertions.assertEquals("gclooney", actual.getContent().get(0).getUserName());
    }

    @Test
    public void testAuditRecordsMatching_happyPath_identicalToAndFromDayMatchesRecordsOnThatDay() {

        Date fromDate = Date.from(Instant.parse("2019-02-06T00:00:00Z")); // Matches "day" part of the date in CSV file
        Date toDate = Date.from(Instant.parse("2019-02-06T23:59:59Z")); // Matches "day" part of the date in CSV file

        PageRequest pr = createPageRequest(0, 20, "createDate:desc");
        Page<Audit> actual = auditRepository.findAll(auditRecordsMatching(null, null, null,
            null, fromDate, toDate), pr);

        Assertions.assertEquals(1, actual.getNumberOfElements());
        Assertions.assertEquals("gclooney", actual.getContent().get(0).getUserName());
    }

    @Test
    public void testHasNullFields_happyPath_nullCompoundNamePart() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(hasNullFields(null, "smiles", "s10"), pr);

        Assertions.assertEquals(2, actual.getNumberOfElements());
        Assertions.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assertions.assertEquals("compoundB", actual.getContent().get(1).getCompoundName());
    }

    @Test
    public void testHasNullFields_happyPath_withCompoundNamePart() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(hasNullFields("a", "smiles", "s10"), pr);

        Assertions.assertEquals(1, actual.getNumberOfElements());
        Assertions.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testIsHidden_happyPath_nullCompoundNamePart() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(isHidden(null, true), pr);

        Assertions.assertEquals(1, actual.getNumberOfElements());
        Assertions.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testIsHidden_happyPath_withCompoundNamePart() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(isHidden("xxx", true), pr);

        Assertions.assertEquals(0, actual.getNumberOfElements());
    }

    @Test
    public void testStandardSearch_happyPath_nullCompoundNamePart_includeHidden() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(standardSearch(null, true), pr);

        Assertions.assertEquals(3, actual.getNumberOfElements());
        Assertions.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assertions.assertEquals("compoundB", actual.getContent().get(1).getCompoundName());
        Assertions.assertEquals("compoundC", actual.getContent().get(2).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_nullCompoundNamePart_excludeHidden() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(standardSearch(null, false), pr);

        Assertions.assertEquals(2, actual.getNumberOfElements());
        Assertions.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assertions.assertEquals("compoundC", actual.getContent().get(1).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_withCompoundNamePart_includeHidden() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(standardSearch("b", true), pr);

        Assertions.assertEquals(1, actual.getNumberOfElements());
        Assertions.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_withCompoundNamePart_excludeHidden() {

        PageRequest pr = createPageRequest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(standardSearch("b", false), pr);

        Assertions.assertEquals(0, actual.getNumberOfElements());
    }
}
