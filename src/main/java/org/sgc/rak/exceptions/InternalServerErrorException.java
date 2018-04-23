package org.sgc.rak.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a 500 should occur.
 */
public class InternalServerErrorException extends AbstractRestException {

    public InternalServerErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
