package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.classes.Address;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccommodationRequest {
    private Long id;
    private double price;
    private String startingDate;
    private String streetName;
    private int houseNumber;
    private String postcode;
    private String name;
    private int area;
    private String interior;
    private Integer rooms;
    private Integer floors;
    private String image;
}
