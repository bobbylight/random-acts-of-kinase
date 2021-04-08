package org.sgc.rak.model.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SScoreCsvRecordRepTest {

    private SScoreCsvRecord rep;

    @BeforeEach
    public void setUp() {
        rep = new SScoreCsvRecord();
    }

    @Test
    public void testGetSetCompoundName() {
        Assertions.assertNull(rep.getCompoundName());
        rep.setCompoundName("foo");
        Assertions.assertEquals("foo", rep.getCompoundName());
    }

    @Test
    public void testGetSetSelectivityScoreType() {
        Assertions.assertNull(rep.getSelectivityScoreType());
        rep.setSelectivityScoreType("foo");
        Assertions.assertEquals("foo", rep.getSelectivityScoreType());
    }

    @Test
    public void testGetSetNumberOfHits() {
        Assertions.assertNull(rep.getNumberOfHits());
        rep.setNumberOfHits(7);
        Assertions.assertEquals(7, rep.getNumberOfHits().intValue());
    }

    @Test
    public void testGetSetNumberOfNonMutantHits() {
        Assertions.assertNull(rep.getNumberOfNonMutantHits());
        rep.setNumberOfNonMutantHits(7);
        Assertions.assertEquals(7, rep.getNumberOfNonMutantHits().intValue());
    }

    @Test
    public void testGetSetScreeningConcentration() {
        Assertions.assertNull(rep.getScreeningConcentration());
        rep.setScreeningConcentration(7d);
        Assertions.assertEquals(7, rep.getScreeningConcentration(), 0.001);
    }

    @Test
    public void testGetSetSelectivityScore() {
        Assertions.assertNull(rep.getSelectivityScore());
        rep.setSelectivityScore(7d);
        Assertions.assertEquals(7, rep.getSelectivityScore(), 0.001);
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("SScoreCsvRecord[compoundName=<null>," +
            "selectivityScoreType=<null>," +
            "numberOfHits=<null>," +
            "numberOfNonMutantHits=<null>," +
            "screeningConcentration=<null>," +
            "selectivityScore=<null>" +
            "]", rep.toString());
    }
}
