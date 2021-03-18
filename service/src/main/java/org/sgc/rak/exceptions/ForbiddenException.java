package org.sgc.rak.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a 403 Forbidden should occur.
 */
public class ForbiddenException extends AbstractRestException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
