package org.example.persistance.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "accommodation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Double price;

    @Column(name = "starting_date")
    private String startingDate;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Column(name = "name")
    private String name;

    @Column(name = "area")
    private int area;

    @Column(name = "interior")
    private String interior;
    @NotBlank
    @Column(name = "image")
    private String image;

    @Column(name = "rooms")
    private Integer rooms;

    @Column(name = "floors")
    private Integer floors;
}

