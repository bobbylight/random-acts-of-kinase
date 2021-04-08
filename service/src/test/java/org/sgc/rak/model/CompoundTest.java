package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompoundTest {

    @Test
    public void testGetSetChemotype() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.getChemotype());
        compound.setChemotype("foo");
        Assertions.assertEquals("foo", compound.getChemotype());
    }

    @Test
    public void testGetSetCompoundName() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.getCompoundName());
        compound.setCompoundName("foo");
        Assertions.assertEquals("foo", compound.getCompoundName());
    }

    @Test
    public void testGetSetS10() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.getS10());
        compound.setS10(42d);
        Assertions.assertEquals(Double.valueOf(42), compound.getS10());
    }

    @Test
    public void testGetSetSolubility() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.getSolubility());
        compound.setSolubility(42d);
        Assertions.assertEquals(Double.valueOf(42), compound.getSolubility());
    }

    @Test
    public void testGetSetSmiles() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.getSmiles());
        compound.setSmiles("foo");
        Assertions.assertEquals("foo", compound.getSmiles());
    }

    @Test
    public void testGetSetSource() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.getSource());
        compound.setSource("foo");
        Assertions.assertEquals("foo", compound.getSource());
    }

    @Test
    public void testGetSetPrimaryReference() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.getPrimaryReference());
        compound.setPrimaryReference("foo");
        Assertions.assertEquals("foo", compound.getPrimaryReference());
    }

    @Test
    public void testGetSetPrimaryReferenceUrl() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.getPrimaryReferenceUrl());
        compound.setPrimaryReferenceUrl("foo");
        Assertions.assertEquals("foo", compound.getPrimaryReferenceUrl());
    }

    @Test
    public void testGetSetHidden() {
        Compound compound = new Compound();
        Assertions.assertNull(compound.isHidden());
        compound.setHidden(true);
        Assertions.assertTrue(compound.isHidden());
    }

    @Test
    public void testToString() {
        String expected = "Compound[compoundName=<null>,chemotype=<null>," +
            "s10=<null>,solubility=<null>,smiles=<null>,source=<null>,hidden=<null>]";
        Assertions.assertEquals(expected, new Compound().toString());
    }
}
