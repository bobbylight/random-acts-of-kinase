package org.sgc.rak.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sgc.rak.core.Application;
import org.sgc.rak.model.Compound;
import org.sgc.rak.repositories.CompoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.sgc.rak.util.QuerySpecifications.hasNullFields;
import static org.sgc.rak.util.QuerySpecifications.isHidden;
import static org.sgc.rak.util.QuerySpecifications.standardSearch;

/**
 * Unit tests for our query specifications.  These are hard to unit test, so we instead
 * use an in-memory database and essentially integration-test them.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = Application.class)
@Sql("query-specifications-test-data.sql")
public class QuerySpecificationsTest {

    @Autowired
    private CompoundRepository repository;

    private static PageRequest createPageReuest(int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        return  PageRequest.of(page, size, sort);
    }

    @Test
    public void testHasNullFields_happyPath_nullCompoundNamePart() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = repository.findAll(hasNullFields(null, "smiles", "s10"), pr);

        Assert.assertEquals(2, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assert.assertEquals("compoundB", actual.getContent().get(1).getCompoundName());
    }

    @Test
    public void testHasNullFields_happyPath_withCompoundNamePart() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = repository.findAll(hasNullFields("a", "smiles", "s10"), pr);

        Assert.assertEquals(1, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testIsHidden_happyPath_nullCompoundNamePart() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = repository.findAll(isHidden(null, true), pr);

        Assert.assertEquals(1, actual.getNumberOfElements());
        Assert.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testIsHidden_happyPath_withCompoundNamePart() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = repository.findAll(isHidden("xxx", true), pr);

        Assert.assertEquals(0, actual.getNumberOfElements());
    }

    @Test
    public void testStandardSearch_happyPath_nullCompoundNamePart_includeHidden() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = repository.findAll(standardSearch(null, true), pr);

        Assert.assertEquals(3, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assert.assertEquals("compoundB", actual.getContent().get(1).getCompoundName());
        Assert.assertEquals("compoundC", actual.getContent().get(2).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_nullCompoundNamePart_excludeHidden() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = repository.findAll(standardSearch(null, false), pr);

        Assert.assertEquals(2, actual.getNumberOfElements());
        Assert.assertEquals("compoundA", actual.getContent().get(0).getCompoundName());
        Assert.assertEquals("compoundC", actual.getContent().get(1).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_withCompoundNamePart_includeHidden() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = repository.findAll(standardSearch("b", true), pr);

        Assert.assertEquals(1, actual.getNumberOfElements());
        Assert.assertEquals("compoundB", actual.getContent().get(0).getCompoundName());
    }

    @Test
    public void testStandardSearch_happyPath_withCompoundNamePart_excludeHidden() {

        PageRequest pr = createPageReuest(0, 20, "compoundName");
        Page<Compound> actual = repository.findAll(standardSearch("b", false), pr);

        Assert.assertEquals(0, actual.getNumberOfElements());
    }
}
