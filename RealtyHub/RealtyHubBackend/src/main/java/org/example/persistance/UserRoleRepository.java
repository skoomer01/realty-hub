package org.example.persistance;

import org.example.persistance.entity.RoleEnum;
import org.example.persistance.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    @Modifying
    @Query(value = "UPDATE user_role\n" +
            "SET role_name = :roleName\n" +
            "WHERE user_id = :userId",
            nativeQuery = true)
    void deactivateUser(@Param("roleName") String INACTIVE, @Param("userId") Long userId);

}
