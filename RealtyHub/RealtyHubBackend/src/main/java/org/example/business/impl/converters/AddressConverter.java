package org.example.business.impl.converters;

import org.example.domain.classes.Address;
import org.example.persistance.entity.AddressEntity;

public final class AddressConverter {
    private AddressConverter(){
    }

    public static Address convert(AddressEntity address)
    {
        return Address.builder()
                .id(address.getId())
                .streetName(address.getStreetName())
                .number(address.getNumber())
                .postcode(address.getPostcode())
                .build();
    }
}
