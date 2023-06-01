package org.example.persistance;

import org.example.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    UserEntity findByUsername(String username);
}
