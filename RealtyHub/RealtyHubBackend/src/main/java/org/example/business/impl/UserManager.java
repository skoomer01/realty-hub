package org.example.business.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.business.IUserManager;
import org.example.business.exception.InvalidUserException;
import org.example.business.exception.UnauthorizedDataAccessException;
import org.example.business.exception.UsernameAlreadyExistsException;
import org.example.business.impl.converters.UserConverter;
import org.example.domain.*;
import org.example.domain.classes.AccessToken;
import org.example.domain.classes.UserInfo;
import org.example.persistance.UserInfoRepository;
import org.example.persistance.UserRepository;
import org.example.persistance.UserRoleRepository;
import org.example.persistance.entity.RoleEnum;
import org.example.persistance.entity.UserEntity;
import org.example.persistance.entity.UserInfoEntity;
import org.example.persistance.entity.UserRoleEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class  UserManager implements IUserManager {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private AccessToken requestAccessToken;

    private UserRoleRepository userRoleRepository;

    @Transactional
    @Override
    public CreateUserResponse createCustomer(CreateUserRequest request) {
        if (userInfoRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        UserInfoEntity savedUser = saveNewUserInfo(request);

        saveNewCustomer(savedUser, request.getPassword());

        return CreateUserResponse.builder()
                .userId(savedUser.getId())
                .build();
    }
    @Transactional
    @Override
    public CreateUserResponse createRealtor(CreateUserRequest request) {
        if (userInfoRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        UserInfoEntity savedUser = saveNewUserInfo(request);

        saveNewRealtor(savedUser, request.getPassword());

        return CreateUserResponse.builder()
                .userId(savedUser.getId())
                .build();
    }

    private void saveNewCustomer(UserInfoEntity userInfoEntity, String password) {
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity newUser = UserEntity.builder()
                .username(userInfoEntity.getUsername())
                .password(encodedPassword)
                .userinfo(userInfoEntity)
                .build();

        newUser.setUserRoles(Set.of(
                UserRoleEntity.builder()
                        .user(newUser)
                        .role(RoleEnum.CUSTOMER)
                        .build()));
        userRepository.save(newUser);
    }
    private void saveNewRealtor(UserInfoEntity userInfoEntity, String password) {
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity newUser = UserEntity.builder()
                .username(userInfoEntity.getUsername())
                .password(encodedPassword)
                .userinfo(userInfoEntity)
                .build();

        newUser.setUserRoles(Set.of(
                UserRoleEntity.builder()
                        .user(newUser)
                        .role(RoleEnum.REALTOR)
                        .build()));
        userRepository.save(newUser);
    }

    private UserInfoEntity saveNewUserInfo(CreateUserRequest request) {
        UserInfoEntity newUser = UserInfoEntity.builder()
                .surname(request.getSurname())
                .username(request.getUsername())
                .name(request.getName())
                .build();
        return userInfoRepository.save(newUser);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        this.userInfoRepository.deleteById(userId);
    }

    @Override
    public GetAllUsersResponse getAllUsers() {
        List<UserInfo> results = userInfoRepository.findAll()
                .stream()
                .map(UserConverter::convert)
                .toList();

        return GetAllUsersResponse.builder()
                .users(results)
                .build();
    }

    @Override
    public Optional<UserInfo> getUser(long userID) {
        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()))
        {
            if (requestAccessToken.getUserId() != userID)
            {
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }
        return
                userInfoRepository.findById(userID).map(UserConverter::convert);
    }

    @Transactional
    @Override
    public void updateUser(UpdateUserRequest request) {
        Optional<UserInfoEntity> userOptional = userInfoRepository.findById(request.getId());
        if (userOptional.isEmpty()) {
            throw new InvalidUserException("USER_ID_INVALID");
        }

        UserInfoEntity user = userOptional.get();
        if (requestAccessToken.hasRole(RoleEnum.ADMIN.name()))
        {
            if (requestAccessToken.getUserId() != user.getId().longValue())
            {
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
            throw new UnauthorizedDataAccessException("ADMIN_CANNOT_CHANGE_USER_DATA");
        }
        updateFields(request, user);
    }

    private void updateFields(UpdateUserRequest request, UserInfoEntity userInfo) {
        userInfo.setSurname(request.getSurname());
        userInfo.setName(request.getName());
        userInfo.setUsername(request.getUsername());

        userInfoRepository.save(userInfo);
    }

    @Transactional
    public void deactivateUser(Long id)
    {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new InvalidUserException("USER_ID_INVALID");
        }
        UserEntity user = userOptional.get();
        if (requestAccessToken.hasRole(RoleEnum.ADMIN.name()))
        {
            userRoleRepository.deactivateUser(RoleEnum.INACTIVE.name(), user.getId());
        }
    }
}
