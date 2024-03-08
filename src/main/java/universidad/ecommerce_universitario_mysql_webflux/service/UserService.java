package universidad.ecommerce_universitario_mysql_webflux.service;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.security.config.Role;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.time.LocalDate;
import java.util.Collections;

@Service
public class UserService {

    private Map<String, User> data;

    @PostConstruct
    public void init() {
        data = new HashMap<>();

        data.put("user", new User(
                1, // ID del usuario
                "user", // Nombre de usuario
                "user@example.com", // Correo electrónico
                "hashedPassword", // Contraseña
                Collections.singleton(Role.ROLE_USER), // Conjunto de roles
                true, // Activo
                "User Address", // Dirección
                "9876543210", // Número de teléfono
                LocalDate.now() // Fecha de ingreso
        ));

        data.put("admin", new User(
                2, // ID del usuario
                "admin", // Nombre de usuario
                "admin@example.com", // Correo electrónico
                "hashedPassword", // Contraseña
                Collections.singleton(Role.ROLE_ADMIN), // Conjunto de roles
                true, // Activo
                "Admin Address", // Dirección
                "1234567890", // Número de teléfono
                LocalDate.now() // Fecha de ingreso
        ));

    }

    public Mono<User> findByUsername(String username) {
        return Mono.justOrEmpty(data.get(username));
    }

}
