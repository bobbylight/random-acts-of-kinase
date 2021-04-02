package org.sgc.rak.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.NanoBretActivityProfile;
import org.sgc.rak.repositories.NanoBretActivityProfileRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NanoBretActivityProfileDaoTest {

    @Mock
    private NanoBretActivityProfileRepository mockRepository;

    @InjectMocks
    private NanoBretActivityProfileDao dao;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetActivityProfiles_compoundNamesAndDiscoverxes_badDiscoverxListLength() {

        List<String> compoundNames = Collections.singletonList("compoundA");
        List<String> discoverxes = Arrays.asList("discoverxA", "discoverxB");
        List<Date> dates = Collections.singletonList(new Date(0));

        Assertions.assertThrows(IllegalStateException.class, () -> {
            dao.getNanoBretActivityProfiles(compoundNames, discoverxes, dates);
        });
    }

    @Test
    public void testGetActivityProfiles_compoundNamesAndDiscoverxes_badDateListLength() {

        List<String> compoundNames = Collections.singletonList("compoundA");
        List<String> discoverxes = Collections.singletonList("discoverxA");
        List<Date> dates = Arrays.asList(new Date(0), new Date(1000));

        Assertions.assertThrows(IllegalStateException.class, () -> {
            dao.getNanoBretActivityProfiles(compoundNames, discoverxes, dates);
        });
    }

    @Test
    public void testGetNanoBretActivityProfiles_compoundNamesAndDiscoverxesAndDates_happyPath() {

        List<String> compoundNames = Arrays.asList("compoundA", "compoundB");
        List<String> discoverxes = Arrays.asList("discoverxA", "discoverxB");
        List<Date> dates = Arrays.asList(new Date(0), new Date(1000));

        when(mockRepository.findByCompoundNameAndKinaseDiscoverxGeneSymbolAndDate(anyString(), anyString(), any()))
            .thenAnswer(answer -> {
                // This isn't fully fleshed out, but is sufficient for the test to complete.
                NanoBretActivityProfile profile = new NanoBretActivityProfile();
                profile.setId((long)answer.getArgument(0).hashCode());
                profile.setCompoundName(answer.getArgument(0));
                return Optional.of(profile);
            });

        Set<NanoBretActivityProfile> actualProfiles = dao.getNanoBretActivityProfiles(compoundNames, discoverxes,
            dates);
        Assertions.assertEquals(compoundNames.size(), actualProfiles.size());
    }

    @Test
    public void testSave() {
        dao.save(null);
        verify(mockRepository, times(1)).saveAll(any());
    }
}
