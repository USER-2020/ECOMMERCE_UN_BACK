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

    public Flux<User> obtenerUsuarios();

    public Mono<User> obtenerUsuarioPorId(int id_usuario);

    public Mono<User> obtenerUsuarioPorNombre(String username);

    public Mono<User> guardarUsuario(User user);

    public Mono<Void> eliminarUsuario(User user);

    public Mono<User> obtenerUsuarioPorNombreOEmail(String username, String email);

    public Mono<Boolean> existsByUsernameOrEmail(String username, String email);

}
