package org.sgc.rak.exceptions;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class NotFoundExceptionTest {

    @Test
    public void testConstructor() {
        NotFoundException e = new NotFoundException("not found");
        Assert.assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        Assert.assertEquals("not found", e.getMessage());
    }
}
