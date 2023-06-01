import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import org.example.business.AccessTokenDecoder;
import org.example.business.AccessTokenEncoder;
import org.example.business.exception.InvalidAccessTokenException;
import org.example.business.impl.AccessTokenEncoderDecoderImpl;
import org.example.domain.classes.AccessToken;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
public class AccessTokenManagerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @Mock
    private AccessTokenDecoder accessTokenDecoder;

    private AccessTokenEncoderDecoderImpl accessTokenManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        accessTokenManager = new AccessTokenEncoderDecoderImpl("E91E158E4C6656F68B1B5D1C316766DE98D2AD6EF3BFB44F78E9CFCDF5");
    }
    @Test
    public void testEncode() {
        // Arrange
        AccessToken accessToken = AccessToken.builder()
                .subject("username")
                .roles(List.of("ROLE_USER", "ROLE_ADMIN"))
                .userId(123L)
                .build();

        // Act
        String encodedToken = accessTokenManager.encode(accessToken);

        // Assert
        assertNotNull(encodedToken);
        assertFalse(StringUtils.isEmpty(encodedToken));

        // Additional assertions on the token content can be added if required

        verifyNoInteractions(accessTokenEncoder);
        verifyNoInteractions(accessTokenDecoder);
    }


}
