package org.sgc.rak.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCompoundsMissingActivityProfiles_happyPath() {

        CompoundCountPair pairA = new CompoundCountPair("compoundA", 3);
        CompoundCountPair pairB = new CompoundCountPair("compoundB", 4);
        List<CompoundCountPair> pairs = Arrays.asList(pairA, pairB);

        Pageable pageable = PageRequest.of(3, 2);

        PageImpl<CompoundCountPair> page = new PageImpl<>(pairs, pageable, 100);
        doReturn(page).when(mockCompoundService).getCompoundsMissingActivityProfiles(any(Pageable.class));

        PagedDataRep<CompoundCountPair> actualResponse = controller.getCompoundsMissingActivityProfiles(pageable);
        Assert.assertEquals(6, actualResponse.getStart());
        Assert.assertEquals(2, actualResponse.getCount());
        Assert.assertEquals(100, actualResponse.getTotal());
        Assert.assertEquals(pairs.size(), actualResponse.getData().size());
        for (int i = 0; i < pairs.size(); i++) {
            CompoundCountPair expected = pairs.get(i);
            CompoundCountPair actual = actualResponse.getData().get(i);
            Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
            Assert.assertEquals(expected.getCount(), actual.getCount());
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
        doReturn(page).when(mockCompoundService).getCompoundsMissingPublicationInfo(any(Pageable.class));

        PagedDataRep<Compound> actualResponse = controller.getCompoundsMissingPublicationInfo(pageable);
        Assert.assertEquals(6, actualResponse.getStart());
        Assert.assertEquals(2, actualResponse.getCount());
        Assert.assertEquals(100, actualResponse.getTotal());
        Assert.assertEquals(compounds.size(), actualResponse.getData().size());
        for (int i = 0; i < compounds.size(); i++) {
            Compound expected = compounds.get(i);
            Compound actual = actualResponse.getData().get(i);
            Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
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
        doReturn(page).when(mockCompoundService).getIncompleteCompounds(any(Pageable.class));

        PagedDataRep<Compound> actualResponse = controller.getIncompleteCompounds(pageable);
        Assert.assertEquals(6, actualResponse.getStart());
        Assert.assertEquals(2, actualResponse.getCount());
        Assert.assertEquals(100, actualResponse.getTotal());
        Assert.assertEquals(compounds.size(), actualResponse.getData().size());
        for (int i = 0; i < compounds.size(); i++) {
            Compound expected = compounds.get(i);
            Compound actual = actualResponse.getData().get(i);
            Assert.assertEquals(expected.getCompoundName(), actual.getCompoundName());
        }
    }
}
