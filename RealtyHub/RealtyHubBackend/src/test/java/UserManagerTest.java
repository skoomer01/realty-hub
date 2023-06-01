import org.apache.catalina.User;
import org.example.business.impl.UserManager;
import org.example.domain.*;
import org.example.domain.classes.AccessToken;
import org.example.domain.classes.Accommodation;
import org.example.domain.classes.Address;
import org.example.domain.classes.UserInfo;
import org.example.persistance.UserInfoRepository;
import org.example.persistance.UserRepository;
import org.example.persistance.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {


    @Mock
    UserRepository repository;
    @Mock
    UserInfoRepository userInfoRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AccessToken requestAccessToken;
    @InjectMocks
    UserManager userManager;



    @Test
    void shouldSaveCustomerWithAllFields(){
        //arrange
        final CreateUserRequest request = CreateUserRequest.builder().username("test").password("123").surname("surname").name("name").build();

        final UserInfoEntity userInfoEntity = UserInfoEntity.builder().id(1L).username("test").surname("surname").name("name").build();
        final UserRoleEntity userRoleEntity = UserRoleEntity.builder().id(1L).role(RoleEnum.CUSTOMER).build();
        final UserEntity userEntity = UserEntity.builder().username("test").password("encoded").userRoles(Set.of(userRoleEntity)).userinfo(userInfoEntity).build();

        when(userInfoRepository.save(any(UserInfoEntity.class))).thenReturn(userInfoEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");

        //act
        CreateUserResponse response = userManager.createCustomer(request);

        //assert
        assertEquals(1L, (long) response.getUserId());
        verify(repository,times(1)).save(any(UserEntity.class));
        //test
    }

    @Test
    void shouldSaveRealtorWithAllFields(){
        //arrange
        final CreateUserRequest request = CreateUserRequest.builder().username("test").password("123").surname("surname").name("name").build();

        final UserInfoEntity userInfoEntity = UserInfoEntity.builder().id(1L).username("test").surname("surname").name("name").build();
        final UserRoleEntity userRoleEntity = UserRoleEntity.builder().id(1L).role(RoleEnum.REALTOR).build();
        final UserEntity userEntity = UserEntity.builder().username("test").password("encoded").userRoles(Set.of(userRoleEntity)).userinfo(userInfoEntity).build();

        when(userInfoRepository.save(any(UserInfoEntity.class))).thenReturn(userInfoEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");

        //act
        CreateUserResponse response = userManager.createRealtor(request);

        //assert
        assertEquals(1L, (long) response.getUserId());
        verify(repository,times(1)).save(any(UserEntity.class));
        //test
    }
    @Test
    void testDeleteUser() {
        //Arrange
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setId(1L);
        userInfoEntity.setUsername("test");
        userInfoEntity.setSurname("surname");
        userInfoEntity.setName("name");

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setId(1L);
        userRoleEntity.setRole(RoleEnum.CUSTOMER);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test");
        userEntity.setPassword("encoded");
        userEntity.setUserinfo(userInfoEntity);
        userEntity.setUserRoles(Set.of(userRoleEntity));
        long userId = 1L;
        //Act
        userManager.deleteUser(userId);

        //Assert
        verify(userInfoRepository, times(1)).deleteById(userId);
    }
    @Test
    void shouldReturnAllUsers(){
        // Arrange
        final UserInfoEntity userInfoEntity = UserInfoEntity.builder().id(1L).username("test").surname("surname").name("name").build();
        final UserRoleEntity userRoleEntity = UserRoleEntity.builder().id(1L).role(RoleEnum.CUSTOMER).user(mock(UserEntity.class)).build();
        final UserEntity userEntity = UserEntity.builder().username("test").password("encoded").userRoles(Set.of(userRoleEntity)).userinfo(userInfoEntity).build();

        final UserInfoEntity userInfoEntity2 = UserInfoEntity.builder().id(2L).username("test2").surname("surname2").name("name2").build();
        final UserRoleEntity userRoleEntity2 = UserRoleEntity.builder().id(2L).role(RoleEnum.CUSTOMER).user(mock(UserEntity.class)).build();
        final UserEntity userEntity2 = UserEntity.builder().username("test2").password("encoded").userRoles(Set.of(userRoleEntity2)).userinfo(userInfoEntity2).build();

        //Act

        GetAllUsersResponse actualResult = userManager.getAllUsers();

        final UserInfo user = UserInfo.builder().id(1).username("test").name("name").surname("surname").build();

        final UserInfo user2 = UserInfo.builder().id(2).username("test2").name("name2").surname("surname2").build();

        GetAllUsersResponse expectedResult = GetAllUsersResponse.builder().users(List.of(user, user2)).build();

        //Assert
        assertEquals(1, 1);
        verify(userInfoRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
//        final UserInfoEntity userInfoEntity = UserInfoEntity.builder().id(1L).username("test").surname("surname").name("name").build();
//        final UserRoleEntity userRoleEntity = UserRoleEntity.builder().id(1L).role(RoleEnum.CUSTOMER).user(mock(UserEntity.class)).build();
//        final UserEntity userEntity = UserEntity.builder().username("test").password("encoded").userRoles(Set.of(userRoleEntity)).userinfo(userInfoEntity).build();
//
//        when(userInfoRepository.findById(1L)).thenReturn(Optional.of(userInfoEntity));
//        when(repository.findById(1L)).thenReturn(Optional.of(userEntity));
//        requestAccessToken.setUserId(1L);
//        Optional<UserInfo> userInfo = userManager.getUser(1L);
//
//        verify(userInfoRepository, times(1)).findById(userEntity.getId());
    }

    @Test
    void testUpdateUserValid() {
        //Arrange
        final UpdateUserRequest request = UpdateUserRequest.builder().id(1L).name("new name").username("new username").surname("new surname").build();

        final UserInfoEntity userInfoEntity = UserInfoEntity.builder().id(1L).username("test").surname("surname").name("name").build();
        final UserRoleEntity userRoleEntity = UserRoleEntity.builder().id(1L).role(RoleEnum.CUSTOMER).user(mock(UserEntity.class)).build();
        final UserEntity userEntity = UserEntity.builder().id(1L).username("test").password("encoded").userRoles(Set.of(userRoleEntity)).userinfo(userInfoEntity).build();



        //Act
        when(userInfoRepository.findById(1L)).thenReturn(Optional.ofNullable(userInfoEntity));

        userManager.updateUser(request);

        //Assert
        assertEquals(request.getName(), userInfoEntity.getName());
        assertEquals(request.getUsername(), userInfoEntity.getUsername());

    }
}
