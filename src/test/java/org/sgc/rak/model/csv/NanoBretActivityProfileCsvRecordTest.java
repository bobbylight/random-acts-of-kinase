package org.sgc.rak.model.csv;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sgc.rak.model.NanoBretActivityProfileModifier;

public class NanoBretActivityProfileCsvRecordTest {

    private NanoBretActivityProfileCsvRecord rep;

    @Before
    public void setUp() {
        rep = new NanoBretActivityProfileCsvRecord();
    }

    @Test
    public void testGetSetCompoundName() {
        Assert.assertNull(rep.getCompoundName());
        rep.setCompoundName("compoundA");
        Assert.assertEquals("compoundA", rep.getCompoundName());
    }

    @Test
    public void testGetSetEntrezGeneSymbol() {
        Assert.assertNull(rep.getDiscoverxGeneSymbol());
        rep.setDiscoverxGeneSymbol("entrez");
        Assert.assertEquals("entrez", rep.getDiscoverxGeneSymbol());
    }

    @Test
    public void testGetSetNlucOrientation() {
        Assert.assertNull(rep.getNlucOrientation());
        rep.setNlucOrientation("foo");
        Assert.assertEquals("foo", rep.getNlucOrientation());
    }

    @Test
    public void testGetSetModifier() {
        Assert.assertNull(rep.getModifier());
        rep.setModifier(NanoBretActivityProfileModifier.EQUAL_TO);
        Assert.assertEquals(NanoBretActivityProfileModifier.EQUAL_TO, rep.getModifier());
    }

    @Test
    public void testGetSetIc50() {
        Assert.assertNull(rep.getIc50());
        rep.setIc50(42.1);
        Assert.assertEquals(42.1, rep.getIc50(), 0.001);
    }

    @Test
    public void testGetSetPercentInhibition() {
        Assert.assertNull(rep.getPercentInhibition());
        rep.setPercentInhibition(42.1);
        Assert.assertEquals(42.1, rep.getPercentInhibition(), 0.001);
    }

    @Test
    public void testGetSetCompoundConcentration() {
        Assert.assertNull(rep.getCompoundConcentration());
        rep.setCompoundConcentration(42);
        Assert.assertEquals(42, rep.getCompoundConcentration().intValue());
    }

    @Test
    public void testGetSetPoints() {
        Assert.assertNull(rep.getPoints());
        rep.setPoints(42);
        Assert.assertEquals(42, rep.getPoints().intValue());
    }

    @Test
    public void testGetSetComment() {
        Assert.assertNull(rep.getComment());
        rep.setComment("foo");
        Assert.assertEquals("foo", rep.getComment());
    }

    @Test
    public void testGetSetDate() {
        Assert.assertNull(rep.getDate());
        rep.setDate("foo");
        Assert.assertEquals("foo", rep.getDate());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("NanoBretActivityProfileCsvRecord[" +
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
