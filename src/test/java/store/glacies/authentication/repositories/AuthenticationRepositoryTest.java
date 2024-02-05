package store.glacies.authentication.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import store.glacies.authentication.models.AuthenticationInfo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataRedisTest
@ExtendWith(SpringExtension.class)
public class AuthenticationRepositoryTest {

    @Autowired
    private AuthenticationRepository repository;


    @Test
    public void testRedisConnection() {
        // Create a new AuthenticationInfo object
        AuthenticationInfo authInfo = AuthenticationInfo.builder()
                .UUID("sub")
                .username("username")
                .refreshToken("refreshToken")
                .idToken("idToken")
                .accessToken("accessToken")
                .build();

        // Save the AuthenticationInfo object in Redis
        repository.save(authInfo);

        // Retrieve the saved AuthenticationInfo object from Redis
        Optional<AuthenticationInfo> retrievedAuthInfo = repository.findById(authInfo.getUUID());

        // Assert that the retrieved AuthenticationInfo object matches the saved AuthenticationInfo object
        assertTrue(retrievedAuthInfo.isPresent());
        assertEquals(authInfo.getUUID(), retrievedAuthInfo.get().getUUID());
        assertEquals(authInfo.getUsername(), retrievedAuthInfo.get().getUsername());
        assertEquals(authInfo.getRefreshToken(), retrievedAuthInfo.get().getRefreshToken());
        assertEquals(authInfo.getIdToken(), retrievedAuthInfo.get().getIdToken());
        assertEquals(authInfo.getAccessToken(), retrievedAuthInfo.get().getAccessToken());
    }
}
