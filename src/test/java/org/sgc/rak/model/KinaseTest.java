package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Test;

public class KinaseTest {

    @Test
    public void testGetSetId() {
        Kinase kinase = new Kinase();
        Assert.assertEquals(0, kinase.getId());
        kinase.setId(42);
        Assert.assertEquals(42, kinase.getId());
    }

    @Test
    public void testGetSetDiscoverxGeneSymbol() {
        Kinase kinase = new Kinase();
        Assert.assertNull(kinase.getDiscoverxGeneSymbol());
        kinase.setDiscoverxGeneSymbol("foo");
        Assert.assertEquals("foo", kinase.getDiscoverxGeneSymbol());
    }

    @Test
    public void testGetSetEntrezGeneSymbol() {
        Kinase kinase = new Kinase();
        Assert.assertNull(kinase.getEntrezGeneSymbol());
        kinase.setEntrezGeneSymbol("foo");
        Assert.assertEquals("foo", kinase.getEntrezGeneSymbol());
    }

    @Test
    public void testGetSetDiscoverxUrl() {
        Kinase kinase = new Kinase();
        Assert.assertNull(kinase.getDiscoverxUrl());
        kinase.setDiscoverxUrl("foo");
        Assert.assertEquals("foo", kinase.getDiscoverxUrl());
    }

    @Test
    public void testToString() {
        String expected = "Kinase[id=0,discoverxGeneSymbol=<null>,entrezGeneSymbol=<null>,discoverxUrl=<null>]";
        Assert.assertEquals(expected, new Kinase().toString());
    }
}
