package org.example.business.impl;


import lombok.AllArgsConstructor;
import org.example.business.IAccommodationIdValidator;
import org.example.business.IAccommodationManager;
import org.example.business.exception.InvalidAccommodationException;
import org.example.business.impl.converters.AccommodationConverter;
import org.example.domain.*;
import org.example.domain.classes.AccessToken;
import org.example.domain.classes.Accommodation;
import org.example.persistance.AccommodationRepository;
import org.example.persistance.AddressRepository;
import org.example.persistance.UserAccommodationRepository;
import org.example.persistance.UserRepository;
import org.example.persistance.entity.AccommodationEntity;
import org.example.persistance.entity.AddressEntity;
import org.example.persistance.entity.UserAccommodationEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccommodationManager implements IAccommodationManager {
    private final AccommodationRepository accommodationRepository;
    private final AddressRepository addressRepository;
    private final IAccommodationIdValidator accommodationIdValidator;
    private final UserAccommodationRepository userAccommodationRepository;
    private final UserRepository userRepository;
    private final AccessToken accessToken;

    //test
    @Override
    @Transactional
    public CreateAccommodationResponse createAccommodation(CreateAccommodationRequest request) {

        Optional<AccommodationEntity> savedAccommodation = saveNewAccommodation(request);
            UserAccommodationEntity userAccommodationEntity = UserAccommodationEntity.builder().user(userRepository.getById(accessToken.getUserId())).accommodation(savedAccommodation.orElse(null)).status("Active").build();
            userAccommodationRepository.save(userAccommodationEntity);

        return savedAccommodation.map(accommodationEntity -> CreateAccommodationResponse.builder()
                .accommodationId(accommodationEntity.getId())
                .build()).orElse(null);
    }
    private Optional<AccommodationEntity> saveNewAccommodation(CreateAccommodationRequest request) {
        AddressEntity addressEntity = AddressEntity.builder()
                .streetName(request.getStreetName())
                .number(request.getHouseNumber())
                .postcode(request.getPostcode()).build();
        addressRepository.save(addressEntity);
        AccommodationEntity newAccommodation = AccommodationEntity.builder()
                    .name(request.getName())
                    .address(addressEntity)
                    .price(request.getPrice())
                    .interior(request.getInterior())
                    .area(request.getArea())
                    .startingDate(request.getStartingDate())
                    .image(request.getImage())
                    .rooms(request.getRooms())
                    .floors(request.getFloors())
                    .build();
        return Optional.ofNullable(accommodationRepository.save(newAccommodation));
    }

    @Override
    public Optional<Accommodation> getAccommodation(long accommodationId)
    {
        return accommodationRepository.findById(accommodationId).map(AccommodationConverter::convert);
    }
    @Transactional
    @Override
    public void deleteAccommodation(long accommodationId)
    {
        this.userAccommodationRepository.deleteByUserAndAccommodation(accommodationId);
        this.accommodationRepository.deleteById(accommodationId);

    }
    @Override
    public void updateAccommodation(UpdateAccommodationRequest request)
    {
        Optional<AccommodationEntity> accommodationEntity = accommodationRepository.findById(request.getId());
        if(accommodationEntity.isEmpty())
        {
            throw new InvalidAccommodationException("ACCOMMODATION_ID_INVALID");
        }
        accommodationIdValidator.validateId(request.getId());
        AccommodationEntity accommodation = accommodationEntity.get();
        updateFields(request, accommodation);
    }

    private void updateFields(UpdateAccommodationRequest request, AccommodationEntity accommodation) {
        Optional<AccommodationEntity> accommodationEntity = accommodationRepository.findById(request.getId());
        if(accommodationEntity.isPresent()) {
            AddressEntity addressEntity = accommodationEntity.get().getAddress();
            addressEntity.setPostcode(request.getPostcode());
            addressEntity.setStreetName(request.getStreetName());
            addressEntity.setNumber(request.getHouseNumber());
            accommodation.setName(request.getName());
            accommodation.setInterior(request.getInterior());
            accommodation.setArea(request.getArea());
            accommodation.setPrice(request.getPrice());
            accommodation.setStartingDate(request.getStartingDate());
            accommodation.setFloors(request.getFloors());
            accommodation.setRooms(request.getRooms());
            accommodation.setImage(request.getImage());
            accommodation.setAddress(addressEntity);
            addressRepository.save(addressEntity);
            accommodationRepository.save(accommodation);
        }
    }

    @Override
    public GetAllAccommodationsResponse getAllAccommodations() {
        List<Accommodation> accommodations = accommodationRepository.findAll()
                .stream()
                .map(AccommodationConverter::convert)
                .toList();

        return GetAllAccommodationsResponse.builder()
                .accommodations(accommodations)
                .build();
    }

    @Override
    public GetAllAccommodationsResponse getAllOwnedAccommodations() {
        List<Accommodation> accommodations = accommodationRepository.getAllOwnedAccommodations(accessToken.getUserId())
                .stream()
                .map(AccommodationConverter::convert)
                .toList();

        return GetAllAccommodationsResponse.builder()
                .accommodations(accommodations)
                .build();
    }
    @Override
    public GetAllAccommodationsResponse getAllActiveAccommodations() {
        List<Accommodation> accommodations = accommodationRepository.getAllAvailableAccommodations()
                .stream()
                .map(AccommodationConverter::convert)
                .toList();

        return GetAllAccommodationsResponse.builder()
                .accommodations(accommodations)
                .build();
    }


    @Override
    @Transactional
    public void purchaseAccommodation(Long id)
    {
        AccommodationEntity accommodationEntity = accommodationRepository.getById(id);
        UserAccommodationEntity userAccommodationEntity = UserAccommodationEntity.builder().user(userRepository.getById(accessToken.getUserId())).accommodation(accommodationEntity).status("Purchased").build();
        userAccommodationRepository.save(userAccommodationEntity);
        userAccommodationRepository.updateStatus(id);
    }

    public int countAccommodations() {
        return accommodationRepository.getAllAccommodationsCount();
    }
    public int countActiveAccommodations() {
        return accommodationRepository.getAllActiveAccommodationsCount();
    }

}
