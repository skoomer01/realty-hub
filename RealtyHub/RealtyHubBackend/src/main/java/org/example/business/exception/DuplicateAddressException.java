package org.example.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateAddressException extends ResponseStatusException {
    public DuplicateAddressException() {
        super(HttpStatus.BAD_REQUEST, "ADDRESS_DUPLICATED");
    }
}
