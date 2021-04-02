package org.sgc.rak.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class BadRequestExceptionTest {

    @Test
    public void testConstructor() {
        BadRequestException e = new BadRequestException("bad request");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        Assertions.assertEquals("bad request", e.getMessage());
    }
}
