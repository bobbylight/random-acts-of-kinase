package org.sgc.rak.model.csv;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SScoreCsvRecordRepTest {

    private SScoreCsvRecord rep;

    @Before
    public void setUp() {
        rep = new SScoreCsvRecord();
    }

    @Test
    public void testGetSetCompoundName() {
        Assert.assertNull(rep.getCompoundName());
        rep.setCompoundName("foo");
        Assert.assertEquals("foo", rep.getCompoundName());
    }

    @Test
    public void testGetSetSelectivityScoreType() {
        Assert.assertNull(rep.getSelectivityScoreType());
        rep.setSelectivityScoreType("foo");
        Assert.assertEquals("foo", rep.getSelectivityScoreType());
    }

    @Test
    public void testGetSetNumberOfHits() {
        Assert.assertNull(rep.getNumberOfHits());
        rep.setNumberOfHits(7);
        Assert.assertEquals(7, rep.getNumberOfHits().intValue());
    }

    @Test
    public void testGetSetNumberOfNonMutantHits() {
        Assert.assertNull(rep.getNumberOfNonMutantHits());
        rep.setNumberOfNonMutantHits(7);
        Assert.assertEquals(7, rep.getNumberOfNonMutantHits().intValue());
    }

    @Test
    public void testGetSetScreeningConcentration() {
        Assert.assertNull(rep.getScreeningConcentration());
        rep.setScreeningConcentration(7d);
        Assert.assertEquals(7, rep.getScreeningConcentration(), 0.001);
    }

    @Test
    public void testGetSetSelectivityScore() {
        Assert.assertNull(rep.getSelectivityScore());
        rep.setSelectivityScore(7d);
        Assert.assertEquals(7, rep.getSelectivityScore(), 0.001);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("SScoreCsvRecord[compoundName=<null>," +
            "selectivityScoreType=<null>," +
            "numberOfHits=<null>," +
            "numberOfNonMutantHits=<null>," +
            "screeningConcentration=<null>," +
            "selectivityScore=<null>" +
            "]", rep.toString());
    }
}
