package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Test;

public class CompoundCountPairTest {

    @Test
    public void testGetCompoundName() {
        CompoundCountPair pair = new CompoundCountPair("compoundA", 3);
        Assert.assertEquals("compoundA", pair.getCompoundName());
    }

    @Test
    public void testGetCount() {
        CompoundCountPair pair = new CompoundCountPair("compoundA", 3);
        Assert.assertEquals(3, pair.getCount());
    }
}
