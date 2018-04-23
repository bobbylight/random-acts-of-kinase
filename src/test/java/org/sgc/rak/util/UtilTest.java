package org.sgc.rak.util;

import org.junit.Assert;
import org.junit.Test;
import org.sgc.rak.model.Compound;

public class UtilTest {

    @Test
    public void testConvertEmptyStringsToNulls() {

        Compound compound = new Compound();
        compound.setChemotype("");
        compound.setSmiles(" ");
        compound.setSource("  ");

        Util.convertEmptyStringsToNulls(compound);
        Assert.assertNull(compound.getChemotype());
        Assert.assertNull(compound.getSmiles());
        Assert.assertNull(compound.getSource());
    }

    @Test
    public void testUpdateNotNullValues() {

        Compound existing = new Compound();
        existing.setCompoundName("compoundA");
        existing.setChemotype("a");
        existing.setSmiles("a");
        existing.setSource("a");
        existing.setS10(0.3);

        Compound newCompound = new Compound();
        newCompound.setCompoundName("compoundA");
        newCompound.setChemotype("b");
        newCompound.setSmiles("b");
        newCompound.setSource("b");
        newCompound.setS10(0.4);

        Compound result = Util.updateNotNullValues(existing, newCompound);
        Assert.assertTrue(result == existing);

        Assert.assertEquals("compoundA", existing.getCompoundName());
        Assert.assertEquals("b", existing.getChemotype());
        Assert.assertEquals("b", existing.getSmiles());
        Assert.assertEquals("b", existing.getSource());
        Assert.assertEquals(0.4, existing.getS10(), 0.01);
    }
}
