package universidad.ecommerce_universitario_mysql_webflux.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import universidad.ecommerce_universitario_mysql_webflux.response.UserResponse;
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

    String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInN1YiI6Ik5vbWJyZVVzdWFyaW8iLCJpYXQiOjE3MTEwNTkyNTIsImV4cCI6MTcxMTA4ODA1Mn0._WdlIxfKzF-ZQu699bq9Y8ySAuHrR0qxgl6SJYcHv1ItzH4tFxnx_P7pvgU0ChHpkDgTUo6h-Lrs6yEzy6PEIg";

    @BeforeEach
    void setUp() {
        // Configurar el mock del servicio para devolver un Mono<User> con un usuario simulado
        when(userService.guardarUsuario(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    // Simular la respuesta del servicio con el usuario guardado
                    return Mono.just(user);
                });
    }

    @Test
    void testCreateUser() {
        // Arrange
        User testUser = new User();
        testUser.setUsername("testUserPrueba45");
        testUser.setEmail("testPrueba656@example.com");
        testUser.setPassword("password123");

        // Act & Assert
        webTestClient.post().uri("/api/users/admin/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(userResponse -> {
                    // Asegura que el estado de la respuesta sea OK y que el mensaje sea el esperado
                    Assertions.assertThat(userResponse).isNotNull();
                    Assertions.assertThat(userResponse.getStatus()).isEqualTo(HttpStatus.OK);
                    Assertions.assertThat(userResponse.getMessage()).isEqualTo("Usuario creado exitosamente");
                });
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
