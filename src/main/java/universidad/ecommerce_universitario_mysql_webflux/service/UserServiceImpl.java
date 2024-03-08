package universidad.ecommerce_universitario_mysql_webflux.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> guardarUsuario(User user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<User> obtenerUsuarioPorCorreo(String email) {
        
        return userRepository.findByEmail(email);
    }
}
