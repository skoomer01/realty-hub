package org.example.business.impl;

import lombok.AllArgsConstructor;
import org.example.business.IAccommodationIdValidator;
import org.example.business.exception.InvalidAccommodationException;
import org.example.persistance.AccommodationRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccommodationIdValidator implements IAccommodationIdValidator {
    private final AccommodationRepository accommodationRepository;
    @Override
    public void validateId(Long accommodationId) {
        if (accommodationId == null || !accommodationRepository.existsById(accommodationId)) {
            throw new InvalidAccommodationException();
        }
    }
}
