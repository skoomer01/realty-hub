package org.example.persistance;

import org.example.persistance.entity.AccommodationEntity;
import org.example.persistance.entity.UserAccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAccommodationRepository extends JpaRepository<UserAccommodationEntity, Long> {
    @Modifying
    @Query(value = "Delete from user_accommodation where user_id = :user_id and accommodation_id = :accommodation_id",
            nativeQuery = true)
    void deleteByUserAndAccommodation(@Param("user_id") Long user_id, @Param("accommodation_id") Long accommodation_id);

    @Modifying
    @Query(value = "UPDATE user_accommodation set status = 'Purchased' where accommodation_id = :accommodation_id",
            nativeQuery = true)
    void updateStatus(@Param("accommodation_id")Long accommodation_id);

}
