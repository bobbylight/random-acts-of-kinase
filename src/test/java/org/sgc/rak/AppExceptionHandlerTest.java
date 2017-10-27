package org.sgc.rak;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sgc.rak.exceptions.AppExceptionHandler;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.exceptions.ErrorResponse;
import org.sgc.rak.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class AppExceptionHandlerTest {

    private AppExceptionHandler handler;

    @Before
    public void setUp() {
        handler = new AppExceptionHandler();
    }

    @Test
    public void testHandleBadRequestException() {

        BadRequestException e = new BadRequestException("Invalid parameter");

        WebRequest request = Mockito.mock(WebRequest.class);
        ResponseEntity<Object> responseEntity = handler.restException(request, e);

        ErrorResponse response = (ErrorResponse)responseEntity.getBody();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assert.assertEquals("Invalid parameter", response.getMessage());
    }

    @Test
    public void testHandleNotFoundException() {

        NotFoundException e = new NotFoundException("No such widget");

        WebRequest request = Mockito.mock(WebRequest.class);
        ResponseEntity<Object> responseEntity = handler.restException(request, e);

        ErrorResponse response = (ErrorResponse)responseEntity.getBody();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        Assert.assertEquals("No such widget", response.getMessage());
    }
}
