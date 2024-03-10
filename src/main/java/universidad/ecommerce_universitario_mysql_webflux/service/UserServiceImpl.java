package universidad.ecommerce_universitario_mysql_webflux.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;


    @Override
    public Mono<Void> eliminarUsuario(User user) {
        // TODO Auto-generated method stub
        return userRepository.delete(user);
    }

    @Override
    public Mono<Boolean> existsByUsernameOrEmail(String username, String email) {
        // TODO Auto-generated method stub
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public Mono<User> guardarUsuario(User user) {
        // TODO Auto-generated method stub
        return userRepository.save(user);
    }

    @Override
    public Mono<User> obtenerUsuarioPorId(int id) {
        // TODO Auto-generated method stub
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> obtenerUsuarioPorNombre(String username) {
        // TODO Auto-generated method stub
        return userRepository.findByUsername(username);
    }

    @Override
    public Flux<User> obtenerUsuarios() {
        // TODO Auto-generated method stub
        return userRepository.findAll();
    }
    
}
