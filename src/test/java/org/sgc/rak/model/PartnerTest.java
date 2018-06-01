package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PartnerTest {

    private Partner partner;

    @Before
    public void setUp() {
        partner = new Partner();
    }

    @Test
    public void testGetSetId() {
        Assert.assertNull(partner.getId());
        partner.setId(5L);
        Assert.assertEquals(Long.valueOf(5), partner.getId());
    }

    @Test
    public void testGetSetName() {
        Assert.assertNull(partner.getName());
        partner.setName("foo");
        Assert.assertEquals("foo", partner.getName());
    }

    @Test
    public void testGetSetUrl() {
        Assert.assertNull(partner.getUrl());
        partner.setUrl("foo");
        Assert.assertEquals("foo", partner.getUrl());
    }

    @Test
    public void testToString() {
        String expected = "Partner[id=<null>,name=<null>,url=<null>]";
        Assert.assertEquals(expected, partner.toString());
    }
}
