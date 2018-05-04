package org.sgc.rak.exceptions;

import org.sgc.rak.util.SuppressFBWarnings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * Handles uncaught exceptions in the application.  Creates a response in the
 * format we want to return.
 */
@SuppressFBWarnings(value = { "NP_NONNULL_PARAM_VIOLATION" },
    justification = "These errors don't actually occur")
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Overridden to return the first validation error for a better error message.
     *
     * @param e The exception thrown.
     * @param headers Any headers for the response.
     * @param status The status for the response.
     * @param request The request.
     * @return The response entity to return.
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException e, HttpHeaders headers,
                                                         HttpStatus status, WebRequest request) {

        List<ObjectError> errors =  e.getAllErrors();
        if (errors.isEmpty()) {
            return handleExceptionInternal(e, null, headers, status, request);
        }

        ObjectError error = errors.get(0);
        String message = error.getDefaultMessage();
        return handleExceptionInternal(message, headers, status, request);
    }

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
        return handleExceptionInternal(e.getMessage(), headers, status, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(String message, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        ErrorResponse response = new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(response, headers, status);
    }

    /**
     * Overridden to return the first validation error for a better error message.
     *
     * @param e The exception thrown.
     * @param headers Any headers for the response.
     * @param status The status for the response.
     * @param request The request.
     * @return The response entity to return.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                   HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<ObjectError> errors =  e.getBindingResult().getAllErrors();
        if (errors.isEmpty()) {
            return handleExceptionInternal(e, null, headers, status, request);
        }

        ObjectError error = errors.get(0);
        String message = error.getDefaultMessage();
        return handleExceptionInternal(message, headers, status, request);
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
