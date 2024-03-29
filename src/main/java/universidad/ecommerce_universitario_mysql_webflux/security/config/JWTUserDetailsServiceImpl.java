package universidad.ecommerce_universitario_mysql_webflux.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
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
        public Mono<UserDetails> findByUsername(String usernameOrEmail) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                String username = "";
                return userRepository.findByUsernameOrEmail(username, usernameOrEmail)
                                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException(
                                                "El usuario con el email" + usernameOrEmail + "no existe!!"))))
                                .map(user -> {
                                        if (user == null) {
                                                logger.error(String.format("Error en el login: no existe el usuario %s",
                                                                usernameOrEmail));
                                                throw new UsernameNotFoundException(
                                                                "Error en el login: no existe el usuario"
                                                                                + usernameOrEmail + "en el sistema");
                                        }
                                        return new User(
                                                        user.getUsername(),
                                                        user.getPassword(),
                                                        authorities);
                                });

        }

        // @Override
        // public Mono<UserDetails> findByUsername(String usernameOrEmail) {
        // List<GrantedAuthority> authorities = new ArrayList<>();
        // return userRepository.findByUsernameOrEmail(usernameOrEmail)
        // .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException(
        // "El usuario con el correo electrónico " + email + " no existe"))))
        // .map(user -> {
        // if (user == null) {
        // logger.error(String.format(
        // "Error en la autenticación: no existe el usuario con correo electrónico %s",
        // email));
        // throw new UsernameNotFoundException(
        // "Error en la autenticación: no existe el usuario con correo electrónico "
        // + email + " en el sistema");
        // }
        // return org.springframework.security.core.userdetails.User.builder()
        // .username(user.getEmail())
        // .password(user.getPassword())
        // .authorities(authorities)
        // .build();
        // });
        // }

}
