package org.sgc.rak.exceptions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doReturn;

public class AppExceptionHandlerTest {

    private AppExceptionHandler exceptionHandler;

    @Before
    public void setUp() {
        exceptionHandler = new AppExceptionHandler();
    }

    @Test
    public void testHandleBindException() {

        List<ObjectError> errors = Collections.singletonList(
            new ObjectError("objectName", "Default message")
        );

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        doReturn(errors).when(bindingResult).getAllErrors();
        BindException e = new BindException(bindingResult);

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest request = Mockito.mock(WebRequest.class);

        ResponseEntity<Object> response = exceptionHandler.handleBindException(e, headers, status, request);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testHandleMethodArgumentNotValid_objectErrors() {

        List<ObjectError> errors = Collections.singletonList(
            new ObjectError("objectName", "Default message")
        );

        MethodParameter methodParam = Mockito.mock(MethodParameter.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        doReturn(errors).when(bindingResult).getAllErrors();
        MethodArgumentNotValidException e = new MethodArgumentNotValidException(methodParam, bindingResult);

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest request = Mockito.mock(WebRequest.class);

        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(e, headers, status, request);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRestException() {

        WebRequest mockRequest = Mockito.mock(WebRequest.class);
        BadRequestException e = new BadRequestException("bad request");
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Object> response = exceptionHandler.restException(mockRequest, headers, e);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
