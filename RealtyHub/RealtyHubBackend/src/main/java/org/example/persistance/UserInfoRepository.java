package org.example.persistance;

import org.example.persistance.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
    UserInfoEntity findByUsername(String username);

    boolean existsByUsername(String username);

}
