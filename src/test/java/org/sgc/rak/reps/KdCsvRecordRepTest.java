package org.sgc.rak.reps;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KdCsvRecordRepTest {

    private KdCsvRecordRep rep;

    @Before
    public void setUp() {
        rep = new KdCsvRecordRep();
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
        rep.setKd(42);
        Assert.assertEquals(42, rep.getKd().intValue());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("KdCsvRecordRep[" +
            "compoundName=<null>," +
            "discoverxGeneSymbol=<null>," +
            "entrezGeneSymbol=<null>," +
            "modifier=<null>," +
            "kd=<null>" +
            "]", rep.toString());
    }
}
