package org.sgc.rak.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.KinaseActivityProfile;
import org.sgc.rak.repositories.KinaseActivityProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Unit tests for the {@code ActivityProfileDao} class.  This is really just testing
 * the pass-through of queries to the repository.
 */
public class ActivityProfileDaoTest {

    @Mock
    private KinaseActivityProfileRepository activityProfileRepository;

    @InjectMocks
    private ActivityProfileDao activityProfileDao = new ActivityProfileDao();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetKinaseActivityProfiles() {

        KinaseActivityProfile profile = new KinaseActivityProfile();
        profile.setCompoundName("compoundA");
        Page<KinaseActivityProfile> expectedPage = new PageImpl<>(Collections.singletonList(profile));
        doReturn(expectedPage).when(activityProfileRepository).findAll(any(Pageable.class));

        Pageable pageInfo = new PageRequest(0, 20);
        Page<KinaseActivityProfile> actualPage = activityProfileDao.getKinaseActivityProfiles(pageInfo);

        comparePages(expectedPage, actualPage);
    }

    private static void comparePages(Page<KinaseActivityProfile> expectedPage, Page<KinaseActivityProfile> actualPage) {
        Assert.assertEquals(expectedPage.getNumberOfElements(), actualPage.getNumberOfElements());
        for (int i = 0; i < expectedPage.getNumberOfElements(); i++) {
            Assert.assertEquals(expectedPage.getContent().get(i), actualPage.getContent().get(i));
        }
    }
}
