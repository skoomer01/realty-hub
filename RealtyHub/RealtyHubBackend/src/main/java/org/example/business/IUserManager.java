package org.example.business;


import org.example.domain.CreateUserRequest;
import org.example.domain.CreateUserResponse;
import org.example.domain.GetAllUsersResponse;
import org.example.domain.UpdateUserRequest;
import org.example.domain.classes.UserInfo;

import java.util.Optional;

public interface IUserManager {
    CreateUserResponse createCustomer(CreateUserRequest request);
    CreateUserResponse createRealtor(CreateUserRequest request);
    void deleteUser(long userId);
    GetAllUsersResponse getAllUsers();
    Optional<UserInfo> getUser(long userId);
    void updateUser(UpdateUserRequest request);
}
