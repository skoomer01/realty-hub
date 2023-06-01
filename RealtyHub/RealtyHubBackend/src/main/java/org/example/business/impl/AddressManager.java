package org.example.business.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.business.IAddressManager;
import org.example.business.exception.DuplicateAddressException;
import org.example.domain.CreateAddressRequest;
import org.example.domain.CreateAddressResponse;
import org.example.persistance.AddressRepository;
import org.example.persistance.entity.AddressEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressManager implements IAddressManager {
    private final AddressRepository addressRepository;
    @Override
    public CreateAddressResponse createAddress (CreateAddressRequest request)
    {
        if (addressRepository.existsByNumber(request.getNumber())&&addressRepository.existsByStreetName(request.getStreetName())) {
            throw new DuplicateAddressException();
        }

        AddressEntity newAddress = AddressEntity.builder()
                .streetName(request.getStreetName())
                .number(request.getNumber())
                .postcode(request.getPostcode())
                .build();

        AddressEntity savedAddress =addressRepository.save(newAddress);

        return CreateAddressResponse.builder()
                .addressId(savedAddress.getId())
                .build();
    }
}
