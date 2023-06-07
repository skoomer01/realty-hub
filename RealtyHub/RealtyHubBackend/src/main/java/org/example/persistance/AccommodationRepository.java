package org.example.persistance;

import org.example.persistance.entity.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccommodationRepository  extends JpaRepository<AccommodationEntity, Long> {
    @Query(value = "SELECT a.id, a.name, a.starting_date, a.price, a.address_id, a.area, a.interior, a.image, a.floors, a.rooms FROM accommodation as a\n" +
            "INNER JOIN user_accommodation as ua\n" +
            "on a.id = ua.accommodation_id\n" +
            "WHERE ua.user_id = :user_id",
            nativeQuery = true)
    List<AccommodationEntity> getAllOwnedAccommodations(@Param("user_id") Long user_id);

    @Query(value = "Select a.id, a.name, a.starting_date, a.price, a.address_id, a.area, a.interior, a.image, a.floors, a.rooms FROM accommodation as a\n" +
            "INNER JOIN user_accommodation as ua\n" +
            "on a.id = ua.accommodation_id\n" +
            "WHERE ua.status = 'Active'",
            nativeQuery = true)
    List<AccommodationEntity> getAllAvailableAccommodations();

    @Query(value = "SELECT COUNT(*) AS count\n" +
            "FROM accommodation;",
            nativeQuery = true)
    int getAllAccommodationsCount();

    @Query(value = "SELECT COUNT(*) AS count\n" +
            "FROM user_accommodation WHERE status=\"Active\";",
            nativeQuery = true)
    int getAllActiveAccommodationsCount();
}
