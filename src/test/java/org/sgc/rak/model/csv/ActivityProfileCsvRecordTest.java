package org.sgc.rak.model.csv;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityProfileCsvRecordTest {

    private ActivityProfileCsvRecord rep;

    @Before
    public void setUp() {
        rep = new ActivityProfileCsvRecord();
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
        Assert.assertNull(rep.getPercentControl());
        rep.setPercentControl(4.2);
        Assert.assertEquals(4.2, rep.getPercentControl(), 0.01);
    }

    @Test
    public void testGetSetCompuondConcentration() {
        Assert.assertNull(rep.getCompoundConcentration());
        rep.setCompoundConcentration(42);
        Assert.assertEquals(42, rep.getCompoundConcentration().intValue());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("ActivityProfileCsvRecord[" +
            "compoundName=<null>," +
            "discoverxGeneSymbol=<null>," +
            "entrezGeneSymbol=<null>," +
            "percentControl=<null>," +
            "compoundConcentration=<null>" +
            "]", rep.toString());
    }
}
