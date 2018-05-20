package org.sgc.rak.util;

import org.junit.Assert;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.model.BlogPost;
import org.sgc.rak.model.Kinase;
import org.sgc.rak.reps.ActivityProfileCsvRecordRep;
import org.sgc.rak.reps.KdCsvRecordRep;

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

    public static ActivityProfileCsvRecordRep createActivityProfileCsvRecordRep(String compoundName, String discoverx,
                        String entrez, double percentControl, int compoundConcentration) {
        ActivityProfileCsvRecordRep rep = new ActivityProfileCsvRecordRep();
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

    public static Kinase createKinase(String discoverx, String entrez) {
        Kinase kinase = new Kinase();
        kinase.setDiscoverxGeneSymbol(discoverx);
        kinase.setEntrezGeneSymbol(entrez);
        return kinase;
    }

    public static KdCsvRecordRep createKdCsvRecordRep(String compoundName, String discoverx, String entrez,
                                                      String modifier, Double kd) {
        KdCsvRecordRep rep = new KdCsvRecordRep();
        rep.setCompoundName(compoundName);
        rep.setDiscoverxGeneSymbol(discoverx);
        rep.setEntrezGeneSymbol(entrez);
        rep.setModifier(modifier);
        rep.setKd(kd);
        return rep;
    }
}
