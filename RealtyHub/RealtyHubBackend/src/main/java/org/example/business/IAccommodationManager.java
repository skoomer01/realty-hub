package org.example.business;

import org.example.domain.*;
import org.example.domain.classes.Accommodation;

import java.util.Optional;

public interface IAccommodationManager {

    CreateAccommodationResponse createAccommodation(CreateAccommodationRequest request);
    Optional<Accommodation> getAccommodation(long accommodationId);
    void deleteAccommodation(long accommodationId);
    public void updateAccommodation(UpdateAccommodationRequest request);
    GetAllAccommodationsResponse getAllAccommodations();
    GetAllAccommodationsResponse getAllOwnedAccommodations();
    public void purchaseAccommodation(Long id);
    public GetAllAccommodationsResponse getAllActiveAccommodations();


    }
