package org.sgc.rak.exceptions;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class ForbiddenExceptionTest {

    @Test
    public void testConstructor() {
        ForbiddenException e = new ForbiddenException("forbidden");
        Assert.assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
        Assert.assertEquals("forbidden", e.getMessage());
    }
}
