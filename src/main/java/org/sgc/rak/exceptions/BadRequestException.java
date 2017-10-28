package org.sgc.rak.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a 400 should occur.
 */
public class BadRequestException extends AbstractRestException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
