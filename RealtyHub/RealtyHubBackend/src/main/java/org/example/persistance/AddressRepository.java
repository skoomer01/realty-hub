package org.example.persistance;

import org.example.persistance.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    boolean existsByStreetName(String name);
    boolean existsByNumber(int number);
    @Query("select address from AddressEntity address where address.streetName = ?1 and address.postcode = ?2 and address.number = ?3")
    AddressEntity getAddressByParameters(String streetName, String postcode, int houseNumber);
}
