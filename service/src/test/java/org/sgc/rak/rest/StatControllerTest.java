package org.sgc.rak.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.CompoundCountPair;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.CompoundService;
import org.sgc.rak.util.TestUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class StatControllerTest {

    @Mock
    private CompoundService mockCompoundService;

    @InjectMocks
    private StatController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCompoundsMissingActivityProfiles_happyPath() {

        CompoundCountPair pairA = new CompoundCountPair("compoundA", 3);
        CompoundCountPair pairB = new CompoundCountPair("compoundB", 4);
        List<CompoundCountPair> pairs = Arrays.asList(pairA, pairB);

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<CompoundCountPair> page = new PageImpl<>(pairs, pageable, 100);
        doReturn(page).when(mockCompoundService).getCompoundsMissingActivityProfiles(any(), any(Pageable.class));

        PagedDataRep<CompoundCountPair> actualResponse = controller.getCompoundsMissingActivityProfiles(null, pageable);
        Assertions.assertEquals(6, actualResponse.getStart());
        Assertions.assertEquals(2, actualResponse.getCount());
        Assertions.assertEquals(100, actualResponse.getTotal());
        Assertions.assertEquals(pairs.size(), actualResponse.getData().size());
        for (int i = 0; i < pairs.size(); i++) {
            CompoundCountPair expected = pairs.get(i);
            CompoundCountPair actual = actualResponse.getData().get(i);
            Assertions.assertEquals(expected.getCompoundName(), actual.getCompoundName());
            Assertions.assertEquals(expected.getCount(), actual.getCount());
        }
    }

    @Test
    public void testGetCompoundsMissingPublicationInfo_happyPath() {

        List<Compound> compounds = Arrays.asList(
            TestUtil.createCompound("compoundA"),
            TestUtil.createCompound("compoundB")
        );

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<Compound> page = new PageImpl<>(compounds, pageable, 100);
        doReturn(page).when(mockCompoundService).getCompoundsMissingPublicationInfo(any(), any(Pageable.class));

        PagedDataRep<Compound> actualResponse = controller.getCompoundsMissingPublicationInfo(null, pageable);
        Assertions.assertEquals(6, actualResponse.getStart());
        Assertions.assertEquals(2, actualResponse.getCount());
        Assertions.assertEquals(100, actualResponse.getTotal());
        Assertions.assertEquals(compounds.size(), actualResponse.getData().size());
        for (int i = 0; i < compounds.size(); i++) {
            TestUtil.assertCompoundsEqual(compounds.get(i), actualResponse.getData().get(i));
        }
    }

    @Test
    public void testGetHiddenCompounds_happyPath() {

        List<Compound> compounds = Arrays.asList(
            TestUtil.createCompound("compoundA"),
            TestUtil.createCompound("compoundB")
        );

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<Compound> page = new PageImpl<>(compounds, pageable, 100);
        doReturn(page).when(mockCompoundService).getHiddenCompounds(any(), any(Pageable.class));

        PagedDataRep<Compound> actualResponse = controller.getHiddenCompounds(null, pageable);
        Assertions.assertEquals(6, actualResponse.getStart());
        Assertions.assertEquals(2, actualResponse.getCount());
        Assertions.assertEquals(100, actualResponse.getTotal());
        Assertions.assertEquals(compounds.size(), actualResponse.getData().size());
        for (int i = 0; i < compounds.size(); i++) {
            TestUtil.assertCompoundsEqual(compounds.get(i), actualResponse.getData().get(i));
        }
    }

    @Test
    public void testGetIncompleteCompounds_happyPath() {

        List<Compound> compounds = Arrays.asList(
            TestUtil.createCompound("compoundA"),
            TestUtil.createCompound("compoundB")
        );

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<Compound> page = new PageImpl<>(compounds, pageable, 100);
        doReturn(page).when(mockCompoundService).getIncompleteCompounds(any(), any(Pageable.class));

        PagedDataRep<Compound> actualResponse = controller.getIncompleteCompounds(null, pageable);
        Assertions.assertEquals(6, actualResponse.getStart());
        Assertions.assertEquals(2, actualResponse.getCount());
        Assertions.assertEquals(100, actualResponse.getTotal());
        Assertions.assertEquals(compounds.size(), actualResponse.getData().size());
        for (int i = 0; i < compounds.size(); i++) {
            TestUtil.assertCompoundsEqual(compounds.get(i), actualResponse.getData().get(i));
        }
    }
}
