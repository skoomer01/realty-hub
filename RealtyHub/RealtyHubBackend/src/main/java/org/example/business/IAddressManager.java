package org.example.business;

import org.example.domain.CreateAddressRequest;
import org.example.domain.CreateAddressResponse;

public interface IAddressManager {
    CreateAddressResponse createAddress (CreateAddressRequest request);
}
