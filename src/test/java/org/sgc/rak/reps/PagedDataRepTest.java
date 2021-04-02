package org.sgc.rak.reps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PagedDataRepTest {

    private PagedDataRep<String> rep;

    @BeforeEach
    public void setUp() {
        List<String> data = Arrays.asList(
            "one",
            "two"
        );
        rep = new PagedDataRep<>(data, 10, 100);
    }

    @Test
    public void testGetCount() {
        Assertions.assertEquals(2, rep.getCount());
    }

    @Test
    public void testGetSetData() {

        List<String> data = rep.getData();
        Assertions.assertEquals(2, rep.getCount());
        Assertions.assertEquals(2, data.size());
        Assertions.assertEquals("one", data.get(0));
        Assertions.assertEquals("two", data.get(1));

        rep.setData(Arrays.asList("three", "four", "five"));
        Assertions.assertEquals(3, rep.getCount());
        Assertions.assertEquals(3, rep.getData().size());
        Assertions.assertEquals("three", rep.getData().get(0));
        Assertions.assertEquals("four", rep.getData().get(1));
        Assertions.assertEquals("five", rep.getData().get(2));
    }

    @Test
    public void testGetSetStart() {
        Assertions.assertEquals(10, rep.getStart());
        rep.setStart(2);
        Assertions.assertEquals(2, rep.getStart());
    }

    @Test
    public void testGetSetTotal() {
        Assertions.assertEquals(100, rep.getTotal());
        rep.setTotal(200);
        Assertions.assertEquals(200, rep.getTotal());
    }
}
