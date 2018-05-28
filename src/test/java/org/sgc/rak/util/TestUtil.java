package org.sgc.rak.util;

import org.junit.Assert;
import org.sgc.rak.model.*;
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.KdCsvRecord;

/**
 * Utility methods for unit tests.
 */
public final class TestUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private TestUtil() {
    }

    public static void assertActivityProfilesEqual(ActivityProfile expected, ActivityProfile actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getCompoundConcentration(), actual.getCompoundConcentration());
        Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
        Assert.assertEquals(expected.getKd(), actual.getKd());
        //Assert.assertEquals(expected.getKinase(), actual.getKinase());
        Assert.assertEquals(expected.getPercentControl(), actual.getPercentControl());
    }

    public static void assertBlogPostsEqual(BlogPost expected, BlogPost actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getBody(), actual.getBody());
        Assert.assertEquals(expected.getCreateDate(), actual.getCreateDate());
    }

    public static void assertCompoundsEqual(Compound expected, Compound actual) {
        Assert.assertEquals(expected.getChemotype(), actual.getChemotype());
        Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
        Assert.assertEquals(expected.getPrimaryReference(), actual.getPrimaryReference());
        Assert.assertEquals(expected.getPrimaryReferenceUrl(), actual.getPrimaryReferenceUrl());
        Assert.assertEquals(expected.getS10(), actual.getS10());
        Assert.assertEquals(expected.getSmiles(), actual.getSmiles());
        Assert.assertEquals(expected.getSource(), actual.getSource());
    }

    public static void assertFeedbacksEqual(Feedback expected, Feedback actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getBody(), actual.getBody());
        Assert.assertEquals(expected.getCreateDate(), actual.getCreateDate());
    }

    public static void assertKinasesEqual(Kinase expected, Kinase actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getDiscoverxGeneSymbol(), actual.getDiscoverxGeneSymbol());
        Assert.assertEquals(expected.getEntrezGeneSymbol(), actual.getEntrezGeneSymbol());
    }

    public static ActivityProfile createActivityProfile(Long id) {
        return createActivityProfile(id, null, null, null, null, null);
    }

    public static ActivityProfile createActivityProfile(Long id, String compoundName, String discoverx, String entrez,
                    Double percentControl, Integer compoundConcentration) {
        ActivityProfile profile = new ActivityProfile();
        profile.setId(id);
        profile.setCompoundName(compoundName);
        profile.setKinase(createKinase(discoverx, entrez));
        profile.setPercentControl(percentControl);
        profile.setCompoundConcentration(compoundConcentration);
        return profile;
    }

    public static ActivityProfileCsvRecord createActivityProfileCsvRecord(String compoundName, String discoverx,
                        String entrez, double percentControl, int compoundConcentration) {
        ActivityProfileCsvRecord rep = new ActivityProfileCsvRecord();
        rep.setCompoundName(compoundName);
        rep.setDiscoverxGeneSymbol(discoverx);
        rep.setEntrezGeneSymbol(entrez);
        rep.setPercentControl(percentControl);
        rep.setCompoundConcentration(compoundConcentration);
        return rep;
    }

    public static BlogPost createBlogPost(String title, String body) {
        BlogPost post = new BlogPost();
        post.setTitle(title);
        post.setBody(body);
        return post;
    }

    public static Compound createCompound(String name) {
        Compound compound = new Compound();
        compound.setCompoundName(name);
        return compound;
    }

    public static Feedback createFeedback(String title, String body) {
        Feedback feedback = new Feedback();
        feedback.setTitle(title);
        feedback.setBody(body);
        return feedback;
    }

    public static Kinase createKinase(String discoverx, String entrez) {
        return createKinase(0, discoverx, entrez);
    }

    public static Kinase createKinase(long id, String discoverx, String entrez) {
        Kinase kinase = new Kinase();
        kinase.setId(id);
        kinase.setDiscoverxGeneSymbol(discoverx);
        kinase.setEntrezGeneSymbol(entrez);
        return kinase;
    }

    public static KdCsvRecord createKdCsvRecord(String compoundName, String discoverx, String entrez,
                                                      String modifier, Double kd) {
        KdCsvRecord rep = new KdCsvRecord();
        rep.setCompoundName(compoundName);
        rep.setDiscoverxGeneSymbol(discoverx);
        rep.setEntrezGeneSymbol(entrez);
        rep.setModifier(modifier);
        rep.setKd(kd);
        return rep;
    }
}
