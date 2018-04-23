package org.sgc.rak.reps;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class CompoundImportRepTest {

    private CompoundImportRep importRep;

    @Before
    public void setUp() {
        importRep = new CompoundImportRep();
    }

    @Test
    public void testGetSetCompoundStatuses() {

        Assert.assertEquals(0, importRep.getCompoundStatuses().size());

        CompoundImportRep.CompoundStatusPair csp =
            new CompoundImportRep.CompoundStatusPair("fake", null);
        csp.setCompoundName("compoundA");
        csp.setStatus(CompoundImportRep.Status.NEW_COMPOUND);
        List<CompoundImportRep.CompoundStatusPair> csps = Collections.singletonList(csp);
        importRep.setCompoundStatuses(csps);

        Assert.assertEquals(1, importRep.getCompoundStatuses().size());
        CompoundImportRep.CompoundStatusPair actualCsp = importRep.getCompoundStatuses().get(0);
        Assert.assertEquals(csp.getCompoundName(), actualCsp.getCompoundName());
        Assert.assertEquals(csp.getStatus(), actualCsp.getStatus());
    }
}
