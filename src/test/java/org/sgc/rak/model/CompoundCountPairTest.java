package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompoundCountPairTest {

    @Test
    public void testGetCompoundName() {
        CompoundCountPair pair = new CompoundCountPair("compoundA", 3);
        Assertions.assertEquals("compoundA", pair.getCompoundName());
    }

    @Test
    public void testGetCount() {
        CompoundCountPair pair = new CompoundCountPair("compoundA", 3);
        Assertions.assertEquals(3, pair.getCount());
    }
}
