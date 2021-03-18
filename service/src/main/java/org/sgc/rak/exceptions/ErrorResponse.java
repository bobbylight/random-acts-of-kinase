package org.sgc.rak.exceptions;

/**
 * Returned to the user when an error occurs (any 4xx or 5xx response).
 */
public class ErrorResponse {

    private final int statusCode;

    private final String message;

    ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
