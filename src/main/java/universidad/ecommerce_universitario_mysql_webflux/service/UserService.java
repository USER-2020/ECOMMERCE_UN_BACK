package universidad.ecommerce_universitario_mysql_webflux.service;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.repository.UserRepository;
import universidad.ecommerce_universitario_mysql_webflux.security.config.Role;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import java.util.List;
import java.time.LocalDate;
import java.util.Collections;

public interface UserService {

    // private Map<String, User> data;

    // private final UserRepository userRepository;

    // @PostConstruct
    // public void init() {
    // data = new HashMap<>();

    // data.put("user", new User(
    // 1, // ID del usuario
    // "user", // Nombre de usuario
    // "user@example.com", // Correo electrónico
    // "hashedPassword", // Contraseña
    // Collections.singleton(Role.ROLE_USER), // Conjunto de roles
    // true, // Activo
    // "User Address", // Dirección
    // "9876543210", // Número de teléfono
    // LocalDate.now() // Fecha de ingreso
    // ));

    // data.put("admin", new User(
    // 2, // ID del usuario
    // "admin", // Nombre de usuario
    // "admin@example.com", // Correo electrónico
    // "hashedPassword", // Contraseña
    // Collections.singleton(Role.ROLE_ADMIN), // Conjunto de roles
    // true, // Activo
    // "Admin Address", // Dirección
    // "1234567890", // Número de teléfono
    // LocalDate.now() // Fecha de ingreso
    // ));

    // }

    // public Mono<User> findByUsername(String username) {
    // return Mono.justOrEmpty(data.get(username));
    // }
    // public Mono<User>findByUsername (String username){
    // return userRepository.
    // }
    public Flux<User> obtenerUsuarios();

    public Mono<User> obtenerUsuarioPorId(int id);

    public Mono<User> obtenerUsuarioPorNombre(String username);

    public Mono<User> guardarUsuario(User user);

    public Mono<Void> eliminarUsuario(User user);

    public Mono<Boolean> existsByUsernameOrEmail(String username, String email);

}
