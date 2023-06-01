package org.example.business.impl.converters;

import org.example.domain.classes.Accommodation;
import org.example.persistance.entity.AccommodationEntity;

public final class AccommodationConverter {
    private AccommodationConverter(){
    }

    public static Accommodation convert(AccommodationEntity accommodation)
    {
        return Accommodation.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .interior(accommodation.getInterior())
                .price(accommodation.getPrice())
                .startingDate(accommodation.getStartingDate())
                .address(AddressConverter.convert(accommodation.getAddress()))
                .area(accommodation.getArea())
                .image(accommodation.getImage())
                .rooms(accommodation.getRooms())
                .floors(accommodation.getFloors())
                .build();
    }
}
