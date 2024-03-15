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
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Mock
    private WebTestClient webTestClient;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserHandler userHandler;

    @BeforeEach
    void setUp() {
        // Mock del UserService
        when(userService.guardarUsuario(any(User.class)))
                .thenAnswer(invocation -> {
                    User savedUser = invocation.getArgument(0);
                    savedUser.setId_usuario(1); // Simulando que la base de datos asigna un ID
                    return Mono.just(savedUser);
                });
    }

    @Test
    void testCreateUser() {
        // Datos de prueba para el usuario
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setRole("ROLE_USER");
        testUser.setActivo(true);
        testUser.setDireccion("Calle Principal");
        testUser.setNumero_telefono("123456789");
        testUser.setFecha_ingreso(LocalDate.now());

        System.out.println("Usuario formato: " + testUser.toString());

        // Simular el comportamiento del servicio UserService
        when(userService.guardarUsuario(any(User.class)))
                .thenAnswer(invocation -> {
                    System.out.println("Método guardarUsuario invocado");

                    User user = invocation.getArgument(0);
                    // Simular la asignación de ID por la base de datos
                    user.setId_usuario(1); // Supongamos que el ID asignado es 1
                    System.out.println("Id del usuario" + user.getId_usuario());
                    return Mono.just(user);
                });

        // Realizar la solicitud POST al endpoint
        webTestClient.post().uri("/api/users/admin/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> {
                    // Verificar que los campos se establecen correctamente
                    if (user != null) {
                        assertEquals(1, user.getId_usuario()); // Verifica el ID asignado
                    } else {
                        fail("El usuario devuelto es nulo");
                    }
                    assert user.getUsername().equals(testUser.getUsername());
                    assert user.getEmail().equals(testUser.getEmail());
                    assert user.getPassword().equals(testUser.getPassword());
                    assert user.getRole().equals(testUser.getRole());
                    assert user.getActivo().equals(testUser.getActivo());
                    assert user.getDireccion().equals(testUser.getDireccion());
                    assert user.getNumero_telefono().equals(testUser.getNumero_telefono());
                    // Verificar otros campos según sea necesario
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
