package org.sgc.rak.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class InternalServerErrorExceptionTest {

    @Test
    public void testConstructor() {
        InternalServerErrorException e = new InternalServerErrorException("internal server error");
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
        Assertions.assertEquals("internal server error", e.getMessage());
    }
}
