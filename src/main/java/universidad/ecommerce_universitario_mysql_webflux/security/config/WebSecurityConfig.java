package universidad.ecommerce_universitario_mysql_webflux.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    // private JWTUtil jwtUtil;

    @Autowired
    private JWTAuthenticationManager authenticationManager;

    // @Autowired
    // private JJWTSecurityContextRepository securityContextRepository;

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

    // @Bean
    // public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity
    // http, JWTFilter jwtFilter) {
    // return http
    // .csrf(ServerHttpSecurity.CsrfSpec::disable)
    // .exceptionHandling(exceptionHandlingConfigurer -> {
    // })
    // .authorizeExchange(exchangeSpec -> exchangeSpec
    // .pathMatchers(HttpMethod.OPTIONS).permitAll()
    // .pathMatchers("/api/auth").permitAll()
    // .pathMatchers("/api/register").permitAll()
    // .pathMatchers("/**").hasRole("ADMIN")
    // .anyExchange().authenticated())
    // .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
    // .securityContextRepository(securityContextRepository)
    // .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
    // .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
    // .logout(ServerHttpSecurity.LogoutSpec::disable)
    // .build();
    // }
    private final JJWTSecurityContextRepository securityContextRepository;
    private final JWTFilter jwtFilter;

    @Autowired
    public WebSecurityConfig(JJWTSecurityContextRepository securityContextRepository, JWTFilter jwtFilter) {
        this.securityContextRepository = securityContextRepository;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/api/auth").permitAll()
                        .anyExchange().authenticated())
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .build();
    }

    // Agregamos un manejador de excepciones para la excepción CSRF
    // private Mono<Void> handleCsrfException(ServerWebExchange exchange, Exception
    // ex) {
    // // Logueamos la excepción CSRF
    // logger.error("CSRF Exception: {}", ex.getMessage());

    // // Puedes personalizar la respuesta de error CSRF según tus necesidades
    // // En este ejemplo, respondemos con un estado 403 (Forbidden)
    // exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
    // return exchange.getResponse().setComplete();
    // }
}
