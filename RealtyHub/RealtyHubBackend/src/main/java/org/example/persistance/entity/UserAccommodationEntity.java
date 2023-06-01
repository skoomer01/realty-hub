package org.example.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import java.util.*;


import javax.persistence.*;

@Entity
@Data
@Table(name = "user_accommodation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccommodationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private AccommodationEntity accommodation;

    @Column(name = "status")
    private String status;
}
