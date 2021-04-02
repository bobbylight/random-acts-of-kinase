package org.sgc.rak.util;

import org.junit.jupiter.api.Assertions;
import org.sgc.rak.model.*;
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.KdCsvRecord;
import org.sgc.rak.model.csv.NanoBretActivityProfileCsvRecord;

import java.util.Date;
import java.util.List;

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
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCompoundConcentration(), actual.getCompoundConcentration());
        Assertions.assertEquals(expected.getCompoundName(), actual.getCompoundName());
        Assertions.assertEquals(expected.getKd(), actual.getKd());
        //Assertions.assertEquals(expected.getKinase(), actual.getKinase());
        Assertions.assertEquals(expected.getPercentControl(), actual.getPercentControl());
    }

    public static void assertAuditsEqual(Audit expected, Audit actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUserName(), actual.getUserName());
        Assertions.assertEquals(expected.getAction(), actual.getAction());
        Assertions.assertEquals(expected.getIpAddress(), actual.getIpAddress());
        Assertions.assertEquals(expected.getCreateDate(), actual.getCreateDate());
        Assertions.assertEquals(expected.getSuccess(), actual.getSuccess());
    }

    public static void assertBlogPostsEqual(BlogPost expected, BlogPost actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getBody(), actual.getBody());
        Assertions.assertEquals(expected.getCreateDate(), actual.getCreateDate());
        Assertions.assertEquals(expected.getViewCount(), actual.getViewCount());
    }

    public static void assertCompoundsEqual(Compound expected, Compound actual) {
        Assertions.assertEquals(expected.getChemotype(), actual.getChemotype());
        Assertions.assertEquals(expected.getCompoundName(), actual.getCompoundName());
        Assertions.assertEquals(expected.getPrimaryReference(), actual.getPrimaryReference());
        Assertions.assertEquals(expected.getPrimaryReferenceUrl(), actual.getPrimaryReferenceUrl());
        Assertions.assertEquals(expected.getS10(), actual.getS10());
        Assertions.assertEquals(expected.getSmiles(), actual.getSmiles());
        Assertions.assertEquals(expected.getSource(), actual.getSource());
    }

    public static void assertFeedbacksEqual(Feedback expected, Feedback actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getBody(), actual.getBody());
        Assertions.assertEquals(expected.getCreateDate(), actual.getCreateDate());
    }

    public static void assertKinasesEqual(Kinase expected, Kinase actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getDiscoverxGeneSymbol(), actual.getDiscoverxGeneSymbol());
        Assertions.assertEquals(expected.getEntrezGeneSymbol(), actual.getEntrezGeneSymbol());
    }

    public static void assertKinasesEqual(List<Kinase> expected, List<Kinase> actual) {

        Assertions.assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertKinasesEqual(expected.get(i), actual.get(i));
        }
    }

    public static void assertPartnersEqual(Partner expected, Partner actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getUrl(), actual.getUrl());
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

    public static Audit createAudit(String user, AuditAction action, Boolean success) {
        Audit audit = new Audit();
        audit.setUserName(user);
        audit.setAction(action);
        audit.setSuccess(success);
        return audit;
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
        compound.setHidden(false);
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

    @SuppressWarnings("checkstyle:ParameterNumber")
    public static NanoBretActivityProfile createNanoBretActivityProfile(String compoundName, Date date,
                                String comment, String nluc, Double ic50, int compoundConcentration,
                                String discoverx, String entrez, NanoBretActivityProfileModifier modifier,
                                double percentInhibition, int points) {
        NanoBretActivityProfile rep = new NanoBretActivityProfile();
        rep.setDate(date);
        rep.setComment(comment);
        rep.setNlucOrientation(nluc);
        rep.setIc50(ic50);
        rep.setConcentration(compoundConcentration);
        rep.setCompoundName(compoundName);
        rep.setKinase(createKinase(discoverx, entrez));
        rep.setModifier(modifier);
        rep.setPercentInhibition(percentInhibition);
        rep.setPoints(points);
        return rep;
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public static NanoBretActivityProfileCsvRecord createNanoBretActivityProfileCsvRecord(String compoundName,
                                  String date, String comment, String nluc, Double ic50, int compoundConcentration,
                                  String discoverx, NanoBretActivityProfileModifier modifier, double percentInhibition,
                                  int points) {
        NanoBretActivityProfileCsvRecord rep = new NanoBretActivityProfileCsvRecord();
        rep.setDate(date);
        rep.setComment(comment);
        rep.setNlucOrientation(nluc);
        rep.setIc50(ic50);
        rep.setCompoundConcentration(compoundConcentration);
        rep.setCompoundName(compoundName);
        rep.setDiscoverxGeneSymbol(discoverx);
        rep.setModifier(modifier);
        rep.setPercentInhibition(percentInhibition);
        rep.setPoints(points);
        return rep;
    }

    public static Partner createPartner(long id, String name, String url) {
        Partner partner = new Partner();
        partner.setId(id);
        partner.setName(name);
        partner.setUrl(url);
        return partner;
    }
}
