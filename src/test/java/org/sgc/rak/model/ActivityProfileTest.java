package org.sgc.rak.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ActivityProfileTest {

    @Test
    public void testEquals_sameObject() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        Assertions.assertEquals(profile, profile);
    }

    @Test
    public void testEquals_equalButDifferentObjects() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        ActivityProfile profile2 = new ActivityProfile();
        profile2.setId(42L);
        Assertions.assertEquals(profile, profile2);
    }

    @Test
    public void testEquals_null() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        Assertions.assertNotEquals(null, profile);
    }

    @Test
    public void testEquals_differentObjects() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        ActivityProfile profile2 = new ActivityProfile();
        profile2.setId(43L);
        Assertions.assertNotEquals(profile, profile2);
    }

    @Test
    public void testHashCode() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        Assertions.assertNotEquals(0, profile.hashCode());
    }

    @Test
    public void testGetSetId() {
        ActivityProfile profile = new ActivityProfile();
        Assertions.assertNull(profile.getId());
        profile.setId(42L);
        Assertions.assertEquals(42, profile.getId().longValue());
    }

    @Test
    public void testGetSetCompoundName() {
        ActivityProfile profile = new ActivityProfile();
        Assertions.assertNull(profile.getCompoundName());
        profile.setCompoundName("foo");
        Assertions.assertEquals("foo", profile.getCompoundName());
    }

    @Test
    public void testGetSetKinase() {
        ActivityProfile profile = new ActivityProfile();
        Assertions.assertNull(profile.getKinase());
        profile.setKinase(new Kinase());
        Assertions.assertNotNull(profile.getKinase());
    }

    @Test
    public void testGetSetPercentControl() {
        ActivityProfile profile = new ActivityProfile();
        Assertions.assertNull(profile.getPercentControl());
        profile.setPercentControl(.5);
        Assertions.assertEquals(.5, profile.getPercentControl(), 0.001);
    }

    @Test
    public void testGetSetCompoundConcentration() {
        ActivityProfile profile = new ActivityProfile();
        Assertions.assertNull(profile.getCompoundConcentration());
        profile.setCompoundConcentration(42);
        Assertions.assertEquals(42, profile.getCompoundConcentration().longValue());
    }

    @Test
    public void testGetSetKd() {
        ActivityProfile profile = new ActivityProfile();
        Assertions.assertNull(profile.getKd());
        profile.setKd(.5);
        Assertions.assertEquals(.5, profile.getKd(), 0.001);
    }

    @Test
    public void testToString() {
        String expected = "ActivityProfile[compoundName=<null>,kinase=<null>," +
            "percentControl=<null>,compoundConcentration=<null>,kd=<null>]";
        Assertions.assertEquals(expected, new ActivityProfile().toString());
    }
}
