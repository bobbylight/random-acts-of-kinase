package org.sgc.rak.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ForbiddenExceptionTest {

    @Test
    public void testConstructor() {
        ForbiddenException e = new ForbiddenException("forbidden");
        Assertions.assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
        Assertions.assertEquals("forbidden", e.getMessage());
    }
}
