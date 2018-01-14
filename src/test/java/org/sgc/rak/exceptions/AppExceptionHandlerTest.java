package org.sgc.rak.exceptions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class AppExceptionHandlerTest {

    private AppExceptionHandler exceptionHandler;

    @Before
    public void setUp() {
        exceptionHandler = new AppExceptionHandler();
    }

    @Test
    public void testRestException() {

        WebRequest mockRequest = Mockito.mock(WebRequest.class);
        BadRequestException e = new BadRequestException("bad request");

        ResponseEntity<Object> response = exceptionHandler.restException(mockRequest, e);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
