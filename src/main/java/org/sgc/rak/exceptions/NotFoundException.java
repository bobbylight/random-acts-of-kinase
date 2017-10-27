package org.sgc.rak.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a 404 should occur.
 */
public class NotFoundException extends AbstractRestException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
