package org.sgc.rak.model.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActivityProfileCsvRecordTest {

    private ActivityProfileCsvRecord rep;

    @BeforeEach
    public void setUp() {
        rep = new ActivityProfileCsvRecord();
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
    public void testGetSetPercentControl() {
        Assertions.assertNull(rep.getPercentControl());
        rep.setPercentControl(4.2);
        Assertions.assertEquals(4.2, rep.getPercentControl(), 0.01);
    }

    @Test
    public void testGetSetCompuondConcentration() {
        Assertions.assertNull(rep.getCompoundConcentration());
        rep.setCompoundConcentration(42);
        Assertions.assertEquals(42, rep.getCompoundConcentration().intValue());
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("ActivityProfileCsvRecord[" +
            "compoundName=<null>," +
            "discoverxGeneSymbol=<null>," +
            "entrezGeneSymbol=<null>," +
            "percentControl=<null>," +
            "compoundConcentration=<null>" +
            "]", rep.toString());
    }
}
