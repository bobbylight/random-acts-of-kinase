package org.sgc.rak.exceptions;

import org.junit.Assert;
import org.junit.Test;

public class ErrorResponseTest {

    @Test
    public void testGetStatusCode() {
        ErrorResponse er = new ErrorResponse(400, "bad request");
        Assert.assertEquals(400, er.getStatusCode());
    }

    @Test
    public void testGetMessage() {
        ErrorResponse er = new ErrorResponse(400, "bad request");
        Assert.assertEquals("bad request", er.getMessage());
    }
}
