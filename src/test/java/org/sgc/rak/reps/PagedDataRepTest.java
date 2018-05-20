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
    public void testGetSetData() {

        List<String> data = rep.getData();
        Assert.assertEquals(2, rep.getCount());
        Assert.assertEquals(2, data.size());
        Assert.assertEquals("one", data.get(0));
        Assert.assertEquals("two", data.get(1));

        rep.setData(Arrays.asList("three", "four", "five"));
        Assert.assertEquals(3, rep.getCount());
        Assert.assertEquals(3, rep.getData().size());
        Assert.assertEquals("three", rep.getData().get(0));
        Assert.assertEquals("four", rep.getData().get(1));
        Assert.assertEquals("five", rep.getData().get(2));
    }

    @Test
    public void testGetSetStart() {
        Assert.assertEquals(10, rep.getStart());
        rep.setStart(2);
        Assert.assertEquals(2, rep.getStart());
    }

    @Test
    public void testGetSetTotal() {
        Assert.assertEquals(100, rep.getTotal());
        rep.setTotal(200);
        Assert.assertEquals(200, rep.getTotal());
    }
}
