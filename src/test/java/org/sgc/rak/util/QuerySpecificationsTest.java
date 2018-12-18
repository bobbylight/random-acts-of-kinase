package org.sgc.rak.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sgc.rak.core.Application;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.Compound;
import org.sgc.rak.repositories.ActivityProfileRepository;
import org.sgc.rak.repositories.CompoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.sgc.rak.util.QuerySpecifications.*;

/**
 * Unit tests for our query specifications.  These are hard to unit test, so we instead
 * use an in-memory database and essentially integration-test them.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = Application.class)
@Sql("query-specifications-test-data.sql")
public class QuerySpecificationsTest {

    @Autowired
    private CompoundRepository compoundRepository;

    @Autowired
    private ActivityProfileRepository apRepository;

    private static PageRequest createPageReuest(int page, int size, String sortBy) {

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

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching(null, null, null), pr);

        Assert.assertEquals(3, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assert.assertEquals("compoundB", actual.getContent().get(1).getCompoundName());
        Assert.assertEquals("compoundC", actual.getContent().get(2).getCompoundName());
    }

    @Test
    public void testActivityProfilesMatching_happyPath_compoundName() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching("compoundB", null, null), pr);

        Assert.assertEquals(1, actual.getNumberOfElements());
        Assert.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testActivityProfilesMatching_happyPath_kinaseId() {

        PageRequest pr = createPageReuest(0, 20, "compoundName:desc");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching(null, 2L, null), pr);

        Assert.assertEquals(1, actual.getNumberOfElements());
        Assert.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testActivityProfilesMatching_happyPath_percentControl() {

        PageRequest pr = createPageReuest(0, 20, "compoundName:desc");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching(null, null, 0.4), pr);

        Assert.assertEquals(2, actual.getNumberOfElements());
        Assert.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
        Assert.assertEquals("compoundA", actual.getContent().get(1).getCompoundName());
    }

    @Test
    public void testActivityProfilesMatching_happyPath_compoundNamePercentControl() {

        PageRequest pr = createPageReuest(0, 20, "compoundName:desc");
        Page<ActivityProfile> actual = apRepository.findAll(activityProfilesMatching("compoundA", null, 0.2), pr);

        // No activity profiles with compoundA and % control 0.2 or less
        Assert.assertEquals(0, actual.getNumberOfElements());
    }

    @Test
    public void testHasNullFields_happyPath_nullCompoundNamePart() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(hasNullFields(null, "smiles", "s10"), pr);

        Assert.assertEquals(2, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assert.assertEquals("compoundB", actual.getContent().get(1).getCompoundName());
    }

    @Test
    public void testHasNullFields_happyPath_withCompoundNamePart() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(hasNullFields("a", "smiles", "s10"), pr);

        Assert.assertEquals(1, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testIsHidden_happyPath_nullCompoundNamePart() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(isHidden(null, true), pr);

        Assert.assertEquals(1, actual.getNumberOfElements());
        Assert.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testIsHidden_happyPath_withCompoundNamePart() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(isHidden("xxx", true), pr);

        Assert.assertEquals(0, actual.getNumberOfElements());
    }

    @Test
    public void testStandardSearch_happyPath_nullCompoundNamePart_includeHidden() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(standardSearch(null, true), pr);

        Assert.assertEquals(3, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assert.assertEquals("compoundB", actual.getContent().get(1).getCompoundName());
        Assert.assertEquals("compoundC", actual.getContent().get(2).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_nullCompoundNamePart_excludeHidden() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(standardSearch(null, false), pr);

        Assert.assertEquals(2, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assert.assertEquals("compoundC", actual.getContent().get(1).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_withCompoundNamePart_includeHidden() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(standardSearch("b", true), pr);

        Assert.assertEquals(1, actual.getNumberOfElements());
        Assert.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_withCompoundNamePart_excludeHidden() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = compoundRepository.findAll(standardSearch("b", false), pr);

        Assert.assertEquals(0, actual.getNumberOfElements());
    }
}
