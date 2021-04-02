package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PartnerTest {

    private Partner partner;

    @BeforeEach
    public void setUp() {
        partner = new Partner();
    }

    @Test
    public void testGetSetId() {
        Assertions.assertNull(partner.getId());
        partner.setId(5L);
        Assertions.assertEquals(Long.valueOf(5), partner.getId());
    }

    @Test
    public void testGetSetName() {
        Assertions.assertNull(partner.getName());
        partner.setName("foo");
        Assertions.assertEquals("foo", partner.getName());
    }

    @Test
    public void testGetSetUrl() {
        Assertions.assertNull(partner.getUrl());
        partner.setUrl("foo");
        Assertions.assertEquals("foo", partner.getUrl());
    }

    @Test
    public void testGetSetImage() {
        Assertions.assertNull(partner.getImage());
        partner.setImage("foo");
        Assertions.assertEquals("foo", partner.getImage());
    }

    @Test
    public void testToString() {
        String expected = "Partner[id=<null>,name=<null>,url=<null>,image=<null>]";
        Assertions.assertEquals(expected, partner.toString());
    }
}
