package org.sgc.rak.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles uncaught exceptions in the application.  Creates a response in the
 * format we want to return.
 */
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Overridden to return our custom error JSON.
     *
     * @param e The exception thrown.
     * @param body The body of the response.  This parameter is ignored.
     * @param headers Any headers for the response.
     * @param status The status for the response.
     * @param request The request.
     * @return The response entity to return.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body,
                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = new ErrorResponse(status.value(), e.getMessage());
        return new ResponseEntity<>(response, headers, status);
    }

    /**
     * Handler for when a custom REST exception is thrown.
     *
     * @param request The HTTP request.
     * @param e The exception.
     * @return The response to return.
     */
    @ExceptionHandler(AbstractRestException.class)
    public ResponseEntity<Object> restException(WebRequest request, AbstractRestException e) {
        return handleExceptionInternal(e, null, null, e.getStatus(), request);
    }
}
