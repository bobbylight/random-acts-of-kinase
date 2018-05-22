package org.sgc.rak.model.csv;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KdCsvRecordTest {

    private KdCsvRecord rep;

    @Before
    public void setUp() {
        rep = new KdCsvRecord();
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
    public void testGetSetModifier() {
        Assert.assertNull(rep.getModifier());
        rep.setModifier("modifier");
        Assert.assertEquals("modifier", rep.getModifier());
    }

    @Test
    public void testGetSetKd() {
        Assert.assertNull(rep.getKd());
        rep.setKd(42d);
        Assert.assertEquals(42d, rep.getKd(), 0.01);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("KdCsvRecord[" +
            "compoundName=<null>," +
            "discoverxGeneSymbol=<null>," +
            "entrezGeneSymbol=<null>," +
            "modifier=<null>," +
            "kd=<null>" +
            "]", rep.toString());
    }
}
