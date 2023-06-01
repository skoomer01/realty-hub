package org.example.business;

import org.example.business.exception.InvalidAccommodationException;

public interface IAccommodationIdValidator {
    void validateId(Long accommodationId) throws InvalidAccommodationException;
}
