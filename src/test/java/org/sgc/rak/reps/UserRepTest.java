package org.sgc.rak.reps;

import org.junit.Assert;
import org.junit.Test;

public class UserRepTest {

    private static final String USERID = "gclooney";
    private static final String USERID2 = "capplegate";

    @Test
    public void testConstructor() {
        UserRep rep = new UserRep(USERID);
        Assert.assertEquals(USERID, rep.getUserName());
    }

    @Test
    public void testGetSetUserName() {
        UserRep rep = new UserRep(USERID);
        Assert.assertEquals(USERID, rep.getUserName());
        rep.setUserName(USERID2);
        Assert.assertEquals(USERID2, rep.getUserName());
    }
}
