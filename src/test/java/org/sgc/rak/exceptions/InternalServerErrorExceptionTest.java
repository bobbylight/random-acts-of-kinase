package org.sgc.rak.exceptions;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class InternalServerErrorExceptionTest {

    @Test
    public void testConstructor() {
        InternalServerErrorException e = new InternalServerErrorException("internal server error");
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
        Assert.assertEquals("internal server error", e.getMessage());
    }
}
