package org.sgc.rak.exceptions;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class BadRequestExceptionTest {

    @Test
    public void testConstructor() {
        BadRequestException e = new BadRequestException("bad request");
        Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        Assert.assertEquals("bad request", e.getMessage());
    }
}
