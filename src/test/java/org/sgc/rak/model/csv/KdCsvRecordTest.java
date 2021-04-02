package org.sgc.rak.model.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KdCsvRecordTest {

    private KdCsvRecord rep;

    @BeforeEach
    public void setUp() {
        rep = new KdCsvRecord();
    }

    @Test
    public void testGetSetCompoundName() {
        Assertions.assertNull(rep.getCompoundName());
        rep.setCompoundName("compoundA");
        Assertions.assertEquals("compoundA", rep.getCompoundName());
    }

    @Test
    public void testGetSetDiscoverxGeneSymbol() {
        Assertions.assertNull(rep.getDiscoverxGeneSymbol());
        rep.setDiscoverxGeneSymbol("discoverx");
        Assertions.assertEquals("discoverx", rep.getDiscoverxGeneSymbol());
    }

    @Test
    public void testGetSetEntrezGeneSymbol() {
        Assertions.assertNull(rep.getEntrezGeneSymbol());
        rep.setEntrezGeneSymbol("entrez");
        Assertions.assertEquals("entrez", rep.getEntrezGeneSymbol());
    }

    @Test
    public void testGetSetModifier() {
        Assertions.assertNull(rep.getModifier());
        rep.setModifier("modifier");
        Assertions.assertEquals("modifier", rep.getModifier());
    }

    @Test
    public void testGetSetKd() {
        Assertions.assertNull(rep.getKd());
        rep.setKd(42d);
        Assertions.assertEquals(42d, rep.getKd(), 0.01);
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("KdCsvRecord[" +
            "compoundName=<null>," +
            "discoverxGeneSymbol=<null>," +
            "entrezGeneSymbol=<null>," +
            "modifier=<null>," +
            "kd=<null>" +
            "]", rep.toString());
    }
}
