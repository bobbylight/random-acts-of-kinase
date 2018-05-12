package org.sgc.rak.reps;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityProfileCsvRecordRepTest {

    private ActivityProfileCsvRecordRep rep;

    @Before
    public void setUp() {
        rep = new ActivityProfileCsvRecordRep();
    }

    @Test
    public void testGetSetCompoundName() {
        Assert.assertNull(rep.getCompoundName());
        rep.setCompoundName("compoundA");
        Assert.assertEquals("compoundA", rep.getCompoundName());
    }

    @Test
    public void testGetSetDiscoverxGeneSymbol() {
        Assert.assertNull(rep.getDiscoverxGeneSymbol());
        rep.setDiscoverxGeneSymbol("discoverx");
        Assert.assertEquals("discoverx", rep.getDiscoverxGeneSymbol());
    }

    @Test
    public void testGetSetEntrezGeneSymbol() {
        Assert.assertNull(rep.getEntrezGeneSymbol());
        rep.setEntrezGeneSymbol("entrez");
        Assert.assertEquals("entrez", rep.getEntrezGeneSymbol());
    }

    @Test
    public void testGetSetPercentControl() {
        Assert.assertEquals(0, rep.getPercentControl(), 0.01);
        rep.setPercentControl(4.2);
        Assert.assertEquals(4.2, rep.getPercentControl(), 0.01);
    }

    @Test
    public void testGetSetCompuondConcentration() {
        Assert.assertEquals(0, rep.getCompoundConcentration());
        rep.setCompoundConcentration(42);
        Assert.assertEquals(42, rep.getCompoundConcentration());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("ActivityProfileCsvRecordRep[" +
            "compoundName=<null>," +
            "discoverxGeneSymbol=<null>," +
            "entrezGeneSymbol=<null>," +
            "percentControl=0.0," +
            "compoundConcentration=0" +
            "]", rep.toString());
    }
}
