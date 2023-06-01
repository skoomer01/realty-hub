
import org.example.business.AccessTokenEncoder;
import org.example.business.ILoginManager;
import org.example.business.exception.InvalidCredentialsException;
import org.example.business.impl.LoginManager;
import org.example.domain.*;
import org.example.persistance.UserRepository;

import org.example.persistance.entity.RoleEnum;
import org.example.persistance.entity.UserEntity;
import org.example.persistance.entity.UserRoleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginManagerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private LoginManager loginManager;


    @Test
    public void testLogin_Successful() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("username", "password");
        final UserRoleEntity userRoleEntity = UserRoleEntity.builder().id(1L).role(RoleEnum.CUSTOMER).user(mock(UserEntity.class)).build();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("encodedPassword");
        userEntity.setUserRoles(Set.of(userRoleEntity));

        when(userRepository.findByUsername(anyString())).thenReturn(userEntity);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn("accessToken");

        // Act
        LoginResponse loginResponse = loginManager.login(loginRequest);

        // Assert
        assertNotNull(loginResponse);
        assertEquals("accessToken", loginResponse.getAccessToken());

        verify(userRepository, times(1)).findByUsername("username");
        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
        verify(accessTokenEncoder, times(1)).encode(any());
    }

    @Test
    public void testLogin_InvalidUsername() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("nonexistentUser", "password");

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            loginManager.login(loginRequest);
        });

        verify(userRepository, times(1)).findByUsername("nonexistentUser");
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(accessTokenEncoder);
    }

    @Test
    public void testLogin_InvalidPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("username", "wrongPassword");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByUsername(anyString())).thenReturn(userEntity);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            loginManager.login(loginRequest);
        });

        verify(userRepository, times(1)).findByUsername("username");
        verify(passwordEncoder, times(1)).matches("wrongPassword", "encodedPassword");
        verifyNoMoreInteractions(accessTokenEncoder);
    }
}
