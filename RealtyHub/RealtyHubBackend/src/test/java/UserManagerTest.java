import org.example.business.exception.UnauthorizedDataAccessException;
import org.example.business.impl.UserManager;
import org.example.domain.*;
import org.example.domain.classes.AccessToken;
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

        final UserInfoEntity userInfoEntity2 = UserInfoEntity.builder().id(2L).username("test2").surname("surname2").name("name2").build();


        //Act
        when(userInfoRepository.findAll())
                .thenReturn(List.of(userInfoEntity, userInfoEntity2));

        GetAllUsersResponse actualResult = userManager.getAllUsers();

        final UserInfo user = UserInfo.builder().id(1).username("test").name("name").surname("surname").build();

        final UserInfo user2 = UserInfo.builder().id(2).username("test2").name("name2").surname("surname2").build();

        GetAllUsersResponse expectedResult = GetAllUsersResponse.builder().users(List.of(user, user2)).build();

        //Assert
        assertEquals(2, actualResult.getUsers().size());
        assertEquals(expectedResult.getUsers().get(0).getName(),actualResult.getUsers().get(0).getName());
        assertEquals(expectedResult.getUsers().get(1).getName(),actualResult.getUsers().get(1).getName());
        verify(userInfoRepository, times(1)).findAll();
    }
    @Test
    public void testGetUser_WithNonAdminRoleAndMatchingUserID() {
        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        long loggedInUserID = 456;
        when(requestAccessToken.getUserId()).thenReturn(loggedInUserID);

        long userID = 456;
        Optional<UserInfo> result = userManager.getUser(userID);

        //Assert
        verify(userInfoRepository, times(1)).findById(userID);
    }
    @Test
    public void testGetUser_WithAdminRole() {

        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);

        long userID = 123;
        Optional<UserInfo> result = userManager.getUser(userID);

        //Assert
        verify(userInfoRepository, times(1)).findById(userID);
    }
    @Test
    public void testGetUser_WithNonAdminRoleAndDifferentUserID() {
        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        long loggedInUserID = 456;
        when(requestAccessToken.getUserId()).thenReturn(loggedInUserID);

        long userID = 789;
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> userManager.getUser(userID));

        //Assert
        verify(userInfoRepository, never()).findById(anyLong());

        assertEquals("403 FORBIDDEN \"USER_ID_NOT_FROM_LOGGED_IN_USER\"", exception.getMessage());
    }


    @Test
    void testUpdateUserValid() {
        //Arrange
        final UpdateUserRequest request = UpdateUserRequest.builder().id(1L).name("new name").username("new username").surname("new surname").build();

        final UserInfoEntity userInfoEntity = UserInfoEntity.builder().id(1L).username("test").surname("surname").name("name").build();

        //Act
        when(userInfoRepository.findById(1L)).thenReturn(Optional.ofNullable(userInfoEntity));

        userManager.updateUser(request);

        //Assert
        assertEquals(request.getName(), userInfoEntity.getName());
        assertEquals(request.getUsername(), userInfoEntity.getUsername());

    }
}
