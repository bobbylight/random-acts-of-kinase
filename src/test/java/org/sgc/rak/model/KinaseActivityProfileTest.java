package org.sgc.rak.model;

import org.junit.Assert;
import org.junit.Test;

public class KinaseActivityProfileTest {

    @Test
    public void testEquals_sameObject() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        profile.setId(42);
        Assert.assertTrue(profile.equals(profile));
    }

    @Test
    public void testEquals_equalButDifferentObjects() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        profile.setId(42);
        KinaseActivityProfile profile2 = new KinaseActivityProfile();
        profile2.setId(42);
        Assert.assertTrue(profile.equals(profile2));
    }

    @Test
    public void testEquals_null() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        profile.setId(42);
        Assert.assertFalse(profile.equals(null));
    }

    @Test
    public void testEquals_differentObjects() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        profile.setId(42);
        KinaseActivityProfile profile2 = new KinaseActivityProfile();
        profile2.setId(43);
        Assert.assertFalse(profile.equals(profile2));
    }

    @Test
    public void testHashCode() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        profile.setId(42);
        Assert.assertNotEquals(0, profile.hashCode());
    }

    @Test
    public void testGetSetId() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        Assert.assertEquals(0, profile.getId());
        profile.setId(42);
        Assert.assertEquals(42, profile.getId());
    }

    @Test
    public void testGetSetCompoundName() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        Assert.assertNull(profile.getCompoundName());
        profile.setCompoundName("foo");
        Assert.assertEquals("foo", profile.getCompoundName());
    }

    @Test
    public void testGetSetKinase() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        Assert.assertNull(profile.getKinase());
        profile.setKinase(new Kinase());
        Assert.assertNotNull(profile.getKinase());
    }

    @Test
    public void testGetSetPercentControl() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        Assert.assertEquals(0, profile.getPercentControl(), 0.001);
        profile.setPercentControl(.5);
        Assert.assertEquals(.5, profile.getPercentControl(), 0.001);
    }

    @Test
    public void testGetSetCompoundConcentration() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        Assert.assertEquals(0, profile.getCompoundConcentration());
        profile.setCompoundConcentration(42);
        Assert.assertEquals(42, profile.getCompoundConcentration());
    }

    @Test
    public void testGetSetKd() {
        KinaseActivityProfile profile = new KinaseActivityProfile();
        Assert.assertNull(profile.getKd());
        profile.setKd(.5);
        Assert.assertEquals(.5, profile.getKd(), 0.001);
    }

    @Test
    public void testToString() {
        String expected = "KinaseActivityProfile[compoundName=<null>,kinase=<null>," +
            "percentControl=0.0,compoundConcentration=0,kd=<null>]";
        Assert.assertEquals(expected, new KinaseActivityProfile().toString());
    }
}
