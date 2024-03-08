package universidad.ecommerce_universitario_mysql_webflux.service;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;


public interface UserService {

    public Mono<User> guardarUsuario(User user);

    public Mono<User> obtenerUsuarioPorCorreo(String email);

}
