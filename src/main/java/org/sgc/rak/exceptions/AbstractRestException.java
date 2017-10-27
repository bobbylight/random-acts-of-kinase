package org.sgc.rak.exceptions;

import org.springframework.http.HttpStatus;

/**
 * A base class for REST exceptions.
 */
abstract class AbstractRestException extends RuntimeException {

    private HttpStatus status;

    AbstractRestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    HttpStatus getStatus() {
        return status;
    }
}
