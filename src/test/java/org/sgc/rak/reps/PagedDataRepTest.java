package org.sgc.rak.reps;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PagedDataRepTest {

    private PagedDataRep<String> rep;

    @Before
    public void setUp() {
        List<String> data = Arrays.asList(
            "one",
            "two"
        );
        rep = new PagedDataRep<>(data, 10, 100);
    }

    @Test
    public void testGetCount() {
        Assert.assertEquals(2, rep.getCount());
    }

    @Test
    public void testGetData() {
        List<String> data = rep.getData();
        Assert.assertEquals(2, data.size());
        Assert.assertEquals("one", data.get(0));
        Assert.assertEquals("two", data.get(1));
    }

    @Test
    public void testGetStart() {
        Assert.assertEquals(10, rep.getStart());
    }

    @Test
    public void testGetTotal() {
        Assert.assertEquals(100, rep.getTotal());
    }
}
