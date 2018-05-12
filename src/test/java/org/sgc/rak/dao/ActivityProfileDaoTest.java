package org.sgc.rak.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.ActivityProfile;
import org.sgc.rak.repositories.ActivityProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@code ActivityProfileDao} class.  This is really just testing
 * the pass-through of queries to the repository.
 */
public class ActivityProfileDaoTest {

    @Mock
    private ActivityProfileRepository mockRepository;

    @InjectMocks
    private ActivityProfileDao activityProfileDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetActivityProfiles_compoundNamesAndDiscoverxes_badArgs() {

        List<String> compoundNames = Collections.singletonList("compoundA");
        List<String> discoverxes = Arrays.asList("discoverxA", "discoverxB");

        activityProfileDao.getActivityProfiles(compoundNames, discoverxes);
    }

    @Test
    public void testGetActivityProfiles_compoundNamesAndDiscoverxes_happyPath() {

        List<String> compoundNames = Arrays.asList("compoundA", "compoundB");
        List<String> discoverxes = Arrays.asList("discoverxA", "discoverxB");

        when(mockRepository.findByCompoundNameAndKinaseDiscoverxGeneSymbol(anyString(), anyString()))
            .thenAnswer(answer -> {
                // This isn't fully fleshed out, but is sufficient for the test to complete.
                ActivityProfile profile = new ActivityProfile();
                profile.setId((long)answer.getArgument(0).hashCode());
                profile.setCompoundName(answer.getArgument(0));
                return Optional.of(profile);
            });

        Set<ActivityProfile> actualProfiles = activityProfileDao.getActivityProfiles(compoundNames, discoverxes);
        Assert.assertEquals(compoundNames.size(), actualProfiles.size());
    }

    @Test
    public void testGetActivityProfiles_pageable() {

        ActivityProfile profile = new ActivityProfile();
        profile.setCompoundName("compoundA");
        Page<ActivityProfile> expectedPage = new PageImpl<>(Collections.singletonList(profile));
        doReturn(expectedPage).when(mockRepository).findAll(any(Pageable.class));

        Pageable pageInfo = PageRequest.of(0, 20);
        Page<ActivityProfile> actualPage = activityProfileDao.getActivityProfiles(pageInfo);

        comparePages(expectedPage, actualPage);
    }

    @Test
    public void testSave() {
        activityProfileDao.save(null);
        verify(mockRepository, times(1)).saveAll(any());
    }

    private static void comparePages(Page<ActivityProfile> expectedPage, Page<ActivityProfile> actualPage) {
        Assert.assertEquals(expectedPage.getNumberOfElements(), actualPage.getNumberOfElements());
        for (int i = 0; i < expectedPage.getNumberOfElements(); i++) {
            Assert.assertEquals(expectedPage.getContent().get(i), actualPage.getContent().get(i));
        }
    }
}
