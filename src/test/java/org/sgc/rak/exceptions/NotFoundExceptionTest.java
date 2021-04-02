package org.sgc.rak.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class NotFoundExceptionTest {

    @Test
    public void testConstructor() {
        NotFoundException e = new NotFoundException("not found");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        Assertions.assertEquals("not found", e.getMessage());
    }
}
