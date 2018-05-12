package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Test;

public class ActivityProfileTest {

    @Test
    public void testEquals_sameObject() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        Assert.assertTrue(profile.equals(profile));
    }

    @Test
    public void testEquals_equalButDifferentObjects() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        ActivityProfile profile2 = new ActivityProfile();
        profile2.setId(42L);
        Assert.assertTrue(profile.equals(profile2));
    }

    @Test
    public void testEquals_null() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        Assert.assertFalse(profile.equals(null));
    }

    @Test
    public void testEquals_differentObjects() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        ActivityProfile profile2 = new ActivityProfile();
        profile2.setId(43L);
        Assert.assertFalse(profile.equals(profile2));
    }

    @Test
    public void testHashCode() {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(42L);
        Assert.assertNotEquals(0, profile.hashCode());
    }

    @Test
    public void testGetSetId() {
        ActivityProfile profile = new ActivityProfile();
        Assert.assertNull(profile.getId());
        profile.setId(42L);
        Assert.assertEquals(42, profile.getId().longValue());
    }

    @Test
    public void testGetSetCompoundName() {
        ActivityProfile profile = new ActivityProfile();
        Assert.assertNull(profile.getCompoundName());
        profile.setCompoundName("foo");
        Assert.assertEquals("foo", profile.getCompoundName());
    }

    @Test
    public void testGetSetKinase() {
        ActivityProfile profile = new ActivityProfile();
        Assert.assertNull(profile.getKinase());
        profile.setKinase(new Kinase());
        Assert.assertNotNull(profile.getKinase());
    }

    @Test
    public void testGetSetPercentControl() {
        ActivityProfile profile = new ActivityProfile();
        Assert.assertNull(profile.getPercentControl());
        profile.setPercentControl(.5);
        Assert.assertEquals(.5, profile.getPercentControl(), 0.001);
    }

    @Test
    public void testGetSetCompoundConcentration() {
        ActivityProfile profile = new ActivityProfile();
        Assert.assertNull(profile.getCompoundConcentration());
        profile.setCompoundConcentration(42);
        Assert.assertEquals(42, profile.getCompoundConcentration().longValue());
    }

    @Test
    public void testGetSetKd() {
        ActivityProfile profile = new ActivityProfile();
        Assert.assertNull(profile.getKd());
        profile.setKd(.5);
        Assert.assertEquals(.5, profile.getKd(), 0.001);
    }

    @Test
    public void testToString() {
        String expected = "ActivityProfile[compoundName=<null>,kinase=<null>," +
            "percentControl=<null>,compoundConcentration=<null>,kd=<null>]";
        Assert.assertEquals(expected, new ActivityProfile().toString());
    }
}
