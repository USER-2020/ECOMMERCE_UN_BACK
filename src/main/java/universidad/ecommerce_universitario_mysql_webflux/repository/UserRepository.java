package universidad.ecommerce_universitario_mysql_webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
    Mono<User> findByUsernameOrEmail(String username, String email);

    Mono<Boolean> existsByUsernameOrEmail(String username, String email);

    Mono<User> findByUsername(String username);

    Mono<User> findById(Integer id_usuario);

    Flux<User> findAll();
}
