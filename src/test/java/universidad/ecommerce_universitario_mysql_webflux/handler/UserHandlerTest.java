package universidad.ecommerce_universitario_mysql_webflux.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.service.UserService;

@SpringBootTest
@AutoConfigureWebTestClient
public class UserHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserHandler userHandler;

    String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInN1YiI6Ik5vbWJyZVVzdWFyaW8iLCJpYXQiOjE3MTA4NzgzNjEsImV4cCI6MTcxMDkwNzE2MX0._Bl_JUzQfmpJB8-LsXR7mhy_uNMogrT0Ti7lYTduT8uDdETZAO6WARSe48p1RzOrzbYnDzA9MBj6uL47P3TdAQ";

    // @BeforeEach
    // void setUp() {
    // when(userService.guardarUsuario(any(User.class)))
    // .thenReturn(Mono.just(testUser));
    // }

    @Test
    void testCreateUser() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        webTestClient.post().uri("/api/users/admin/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(testUser), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> {
                    assertEquals(testUser, user, "El usuario creado no coincide con el esperado");
                });

        testUser.setId_usuario(1);
    }

    // @Test
    // void testDeleteUser() {

    // }

    // @Test
    // void testGetAll() {

    // }

    // @Test
    // void testGetUserById() {

    // }

    // @Test
    // void testUpdateUser() {

    // }
}
