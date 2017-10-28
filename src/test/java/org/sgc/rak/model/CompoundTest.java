package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Test;

public class CompoundTest {

    @Test
    public void testGetSetChemotype() {
        Compound compound = new Compound();
        Assert.assertNull(compound.getChemotype());
        compound.setChemotype("foo");
        Assert.assertEquals("foo", compound.getChemotype());
    }

    @Test
    public void testGetSetCompoundName() {
        Compound compound = new Compound();
        Assert.assertNull(compound.getCompoundName());
        compound.setCompoundName("foo");
        Assert.assertEquals("foo", compound.getCompoundName());
    }

    @Test
    public void testGetSetS10() {
        Compound compound = new Compound();
        Assert.assertNull(compound.getS10());
        compound.setS10(42d);
        Assert.assertEquals(Double.valueOf(42), compound.getS10());
    }

    @Test
    public void testGetSetSource() {
        Compound compound = new Compound();
        Assert.assertNull(compound.getSource());
        compound.setSource("foo");
        Assert.assertEquals("foo", compound.getSource());
    }

    @Test
    public void testToString() {
        String expected = "Compound[compoundName=<null>,chemotype=<null>," +
            "s10=<null>,source=<null>]";
        Assert.assertEquals(expected, new Compound().toString());
    }
}
