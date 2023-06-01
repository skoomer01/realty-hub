package org.example.domain.classes;

import lombok.*;
import org.example.domain.classes.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Accommodation {
    private Long id;
    private String name;
    private Double price;
    private String startingDate;
    private Address address;
    private int area;
    private String interior;
    private String image;
    private int rooms;
    private int floors;
}
