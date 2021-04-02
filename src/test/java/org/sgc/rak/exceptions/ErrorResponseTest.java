package org.sgc.rak.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ErrorResponseTest {

    @Test
    public void testGetStatusCode() {
        ErrorResponse er = new ErrorResponse(400, "bad request");
        Assertions.assertEquals(400, er.getStatusCode());
    }

    @Test
    public void testGetMessage() {
        ErrorResponse er = new ErrorResponse(400, "bad request");
        Assertions.assertEquals("bad request", er.getMessage());
    }
}
