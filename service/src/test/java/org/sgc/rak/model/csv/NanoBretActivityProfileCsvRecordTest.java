package org.sgc.rak.model.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sgc.rak.model.NanoBretActivityProfileModifier;

public class NanoBretActivityProfileCsvRecordTest {

    private NanoBretActivityProfileCsvRecord rep;

    @BeforeEach
    public void setUp() {
        rep = new NanoBretActivityProfileCsvRecord();
    }

    @Test
    public void testGetSetCompoundName() {
        Assertions.assertNull(rep.getCompoundName());
        rep.setCompoundName("compoundA");
        Assertions.assertEquals("compoundA", rep.getCompoundName());
    }

    @Test
    public void testGetSetEntrezGeneSymbol() {
        Assertions.assertNull(rep.getDiscoverxGeneSymbol());
        rep.setDiscoverxGeneSymbol("entrez");
        Assertions.assertEquals("entrez", rep.getDiscoverxGeneSymbol());
    }

    @Test
    public void testGetSetNlucOrientation() {
        Assertions.assertNull(rep.getNlucOrientation());
        rep.setNlucOrientation("foo");
        Assertions.assertEquals("foo", rep.getNlucOrientation());
    }

    @Test
    public void testGetSetModifier() {
        Assertions.assertNull(rep.getModifier());
        rep.setModifier(NanoBretActivityProfileModifier.EQUAL_TO);
        Assertions.assertEquals(NanoBretActivityProfileModifier.EQUAL_TO, rep.getModifier());
    }

    @Test
    public void testGetSetIc50() {
        Assertions.assertNull(rep.getIc50());
        rep.setIc50(42.1);
        Assertions.assertEquals(42.1, rep.getIc50(), 0.001);
    }

    @Test
    public void testGetSetPercentInhibition() {
        Assertions.assertNull(rep.getPercentInhibition());
        rep.setPercentInhibition(42.1);
        Assertions.assertEquals(42.1, rep.getPercentInhibition(), 0.001);
    }

    @Test
    public void testGetSetCompoundConcentration() {
        Assertions.assertNull(rep.getCompoundConcentration());
        rep.setCompoundConcentration(42);
        Assertions.assertEquals(42, rep.getCompoundConcentration().intValue());
    }

    @Test
    public void testGetSetPoints() {
        Assertions.assertNull(rep.getPoints());
        rep.setPoints(42);
        Assertions.assertEquals(42, rep.getPoints().intValue());
    }

    @Test
    public void testGetSetComment() {
        Assertions.assertNull(rep.getComment());
        rep.setComment("foo");
        Assertions.assertEquals("foo", rep.getComment());
    }

    @Test
    public void testGetSetDate() {
        Assertions.assertNull(rep.getDate());
        rep.setDate("foo");
        Assertions.assertEquals("foo", rep.getDate());
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("NanoBretActivityProfileCsvRecord[" +
            "compoundName=<null>," +
            "discoverxGeneSymbol=<null>," +
            "nlucOrientation=<null>," +
            "modifier=<null>," +
            "ic50=<null>," +
            "percentInhibition=<null>," +
            "compoundConcentration=<null>," +
            "points=<null>," +
            "comment=<null>," +
            "date=<null>" +
            "]", rep.toString());
    }
}
