package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KinaseTest {

    @Test
    public void testGetSetId() {
        Kinase kinase = new Kinase();
        Assertions.assertEquals(0, kinase.getId());
        kinase.setId(42);
        Assertions.assertEquals(42, kinase.getId());
    }

    @Test
    public void testGetSetDiscoverxGeneSymbol() {
        Kinase kinase = new Kinase();
        Assertions.assertNull(kinase.getDiscoverxGeneSymbol());
        kinase.setDiscoverxGeneSymbol("foo");
        Assertions.assertEquals("foo", kinase.getDiscoverxGeneSymbol());
    }

    @Test
    public void testGetSetEntrezGeneSymbol() {
        Kinase kinase = new Kinase();
        Assertions.assertNull(kinase.getEntrezGeneSymbol());
        kinase.setEntrezGeneSymbol("foo");
        Assertions.assertEquals("foo", kinase.getEntrezGeneSymbol());
    }

    @Test
    public void testGetSetNanosynGeneSymbol() {
        Kinase kinase = new Kinase();
        Assertions.assertNull(kinase.getNanosynGeneSymbol());
        kinase.setNanosynGeneSymbol("foo");
        Assertions.assertEquals("foo", kinase.getNanosynGeneSymbol());
    }

    @Test
    public void testGetSetDiscoverxUrl() {
        Kinase kinase = new Kinase();
        Assertions.assertNull(kinase.getDiscoverxUrl());
        kinase.setDiscoverxUrl("foo");
        Assertions.assertEquals("foo", kinase.getDiscoverxUrl());
    }

    @Test
    public void testToString() {
        String expected = "Kinase[id=0,discoverxGeneSymbol=<null>,entrezGeneSymbol=<null>," +
            "nanosynGeneSymbol=<null>,discoverxUrl=<null>]";
        Assertions.assertEquals(expected, new Kinase().toString());
    }
}
