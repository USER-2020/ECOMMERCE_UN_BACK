package universidad.ecommerce_universitario_mysql_webflux.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.repository.UserRepository;

import java.util.List;
import java.util.ArrayList;

@Service
public class JWTUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException(
                        "El usuario con el correo electrónico " + email + " no existe"))))
                .map(user -> {
                    if (user == null) {
                        logger.error(String.format(
                                "Error en la autenticación: no existe el usuario con correo electrónico %s",
                                email));
                        throw new UsernameNotFoundException(
                                "Error en la autenticación: no existe el usuario con correo electrónico "
                                        + email + " en el sistema");
                    }
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(user.getEmail())
                            .password(user.getContrasena())
                            .authorities(authorities)
                            .build();
                });
    }

}
