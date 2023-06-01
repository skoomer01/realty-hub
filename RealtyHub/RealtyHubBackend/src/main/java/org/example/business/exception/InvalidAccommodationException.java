package org.example.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidAccommodationException extends ResponseStatusException {
    public InvalidAccommodationException(){super(HttpStatus.BAD_REQUEST, "ACCOMMODATION_INVALID");}
    public InvalidAccommodationException(String errorCode){super(HttpStatus.BAD_REQUEST, errorCode);}
}
