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
import org.mockito.Mockito;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.response.DataAndStatusResponse;
import universidad.ecommerce_universitario_mysql_webflux.response.UserResponse;
import universidad.ecommerce_universitario_mysql_webflux.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
public class UserHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserHandler userHandler;

    String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInN1YiI6Ik5vbWJyZVVzdWFyaW8iLCJpYXQiOjE3MTE0NzgzMjQsImV4cCI6MTcxMTUwNzEyNH0.GbQF4fP8Rsb6znNfpGi0mF-Vt9pFQ42zNR46P_yN6KGa31YcdkOR63gRMUeVJhagzecEGdVRL5WtJIN0FnDQwA";

    @BeforeEach
    void setUp() {
        // Configurar el mock del servicio para devolver un Mono<User> con un usuario
        // simulado
        when(userService.guardarUsuario(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    // Simular la respuesta del servicio con el usuario guardado
                    return Mono.just(user);
                });

        // Configurar el comportamiento esperado del servicio userService para findById
        int userId = 3; // ID del usuario a buscar
        User user = new User(userId, "testUserPrueba45", "testPrueba656@example.com", "password123", "ROLE_ADMIN", null,
                null, null, null);
        when(userService.obtenerUsuarioPorId(userId)).thenReturn(Mono.just(user));

        // Configurar comportamiento simulado para el servicio
        // userService.eliminarUsuario
        // Se asume que el método eliminarUsuario acepta un objeto User y devuelve un
        // Mono<User>
        when(userService.eliminarUsuario(any(User.class)))
                .thenAnswer(invocation -> {
                    User userToDelete = invocation.getArgument(0);
                    // Simular la eliminación del usuario devolviendo el mismo usuario eliminado
                    return Mono.just(userToDelete);
                });

        // Configurar el comportamiento esperado del servicio userService para
        // actualizar un usuario
        when(userService.guardarUsuario(any(User.class)))
                .thenAnswer(invocation -> {
                    User updatedUser = invocation.getArgument(0);
                    // Simular la respuesta del servicio con el usuario actualizado
                    return Mono.just(updatedUser);
                });

        // // Configurar el comportamiento simulado del servicio userService
        // when(userService.obtenerUsuarios()).thenReturn(Flux.fromIterable(expectedUsers));
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

    @Test
    void testDeleteUser() {
        // Arrange
        int userId = 3; // ID del usuario a eliminar

        // Act & Assert
        webTestClient.delete().uri("/api/users/admin/eliminar/{id}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk() // Esperamos un estado OK, ya que el usuario se elimina exitosamente
                .expectBody(UserResponse.class)
                .value(userResponse -> {
                    // Aseguramos que la respuesta sea la esperada
                    Assertions.assertThat(userResponse).isNotNull();
                    Assertions.assertThat(userResponse.getStatus()).isEqualTo(HttpStatus.OK);
                    Assertions.assertThat(userResponse.getMessage()).isEqualTo("Usuario eliminado exitosamente");
                });

    }

    @Test
    void testGetAll() {
        // Preparar la respuesta esperada
        List<User> expectedUsers = Arrays.asList(
                new User(1, "NombreUsuario", "usuario@example.com",
                        "$2y$10$cPDpInGWe.YpoAHqnNKeL.uCj7VTo9Q3auRGwnUqW7fJtoZVjv2jy", "ROLE_ADMIN", true,
                        "DireccionUsuario",
                        "1234567890", null),
                new User(2, "UserAdminPrueba2024", "prueba48@gmail.com",
                        "$2a$10$Zp0IaGK43jG9X7rgspko6.pRgBL22YJ3XYhtSn.GpKWYYEZ3jYB8K", "ROLE_USER", true, null, null,
                        null),
                new User(5, "UsuarioTest", "test@example.com",
                        "$2a$10$2st6I7PK0wra0JnytMfo5OSz/zow14lzDZE4cRmTVKy/IWmYUp8Y6", "ROLE_USER", true, null, null,
                        null));
        DataAndStatusResponse expectedResponse = new DataAndStatusResponse(HttpStatus.OK, expectedUsers);

        // Realizar la solicitud GET y verificar la respuesta
        webTestClient.get().uri("/api/users/admin")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DataAndStatusResponse.class)
                .isEqualTo(expectedResponse);

    }

    @Test
    void testGetUserById() {
        // Arrange
        int userId = 3; // Replace with the actual ID you want to test

        // Act && Assert
        webTestClient.get().uri("/api/users/admin/{id}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON) // Ensure expected content type
                .expectBody(User.class)
                .value(returnedUser -> {
                    // Assert the returned user's properties
                    Assertions.assertThat(returnedUser).isNotNull();
                    Assertions.assertThat(returnedUser.getId_usuario()).isEqualTo(userId);
                    Assertions.assertThat(returnedUser.getUsername()).isEqualTo("testUserPrueba45");
                    Assertions.assertThat(returnedUser.getEmail()).isEqualTo("testPrueba656@example.com");
                    // Add more assertions as needed for other user fields
                });
    }

    // }

    @Test
    void testUpdateUser() {
        // Arrange
        int userId = 5; // ID del usuario a actualizar
        User updatedUser = new User(userId, "updatedUsername", "updatedEmail@example.com", "updatedPassword",
                "ROLE_USER", true, null, null, null);

        // Simular la solicitud de actualización del usuario con los datos actualizados
        Mono<User> updatedUserMono = Mono.just(updatedUser);

        // Simular el comportamiento del cuerpo de la solicitud (request body) para la
        // validación
        when(userService.guardarUsuario(any(User.class))).thenReturn(updatedUserMono);

        // Act & Assert
        webTestClient.put().uri("/api/users/admin/update/{id}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isOk() // Esperamos un estado OK, ya que la actualización se realiza correctamente
                .expectBody(UserResponse.class)
                .value(userResponse -> {
                    // Aseguramos que la respuesta sea la esperada
                    Assertions.assertThat(userResponse).isNotNull();
                    Assertions.assertThat(userResponse.getStatus()).isEqualTo(HttpStatus.OK);
                    Assertions.assertThat(userResponse.getMessage()).isEqualTo("Usuario actualizado exitosamente");
                });
    }

}
