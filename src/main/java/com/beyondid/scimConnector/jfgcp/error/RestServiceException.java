package com.beyondid.scimConnector.jfgcp.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Doesn't Identify the redirect URI")
public class RestServiceException extends RuntimeException {
    public RestServiceException(String error) {
        super(error);
    }
}
