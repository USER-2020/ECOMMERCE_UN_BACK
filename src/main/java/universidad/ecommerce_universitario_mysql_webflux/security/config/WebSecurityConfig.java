package universidad.ecommerce_universitario_mysql_webflux.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.server.ServerWebExchange;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

// private JWTUtil jwtUtil;

@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

        @Autowired
        private JWTAuthenticationManager authenticationManager;

        @Autowired
        private JJWTSecurityContextRepository securityContextRepository;

        @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

        @Bean
        public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
                return http
                                .exceptionHandling()
                                .and()
                                .authenticationManager(authenticationManager)
                                .securityContextRepository(securityContextRepository)
                                .authorizeExchange()
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .pathMatchers("/api/auth").permitAll()
                                .pathMatchers("/api/register").permitAll()
                                // .pathMatchers("/api/email/send").permitAll()
                                // .pathMatchers("/api/email/send-html").permitAll()
                                // .pathMatchers("/api/motosV2").permitAll()
                                // .pathMatchers("/api/motosV2/marca/{marca}").permitAll()
                                // .pathMatchers("/api/tusmotos").permitAll()
                                // .pathMatchers("/api/users/{id}").permitAll()
                                .pathMatchers("/**").hasRole("ADMIN")// Permitir acceso sin autenticación a "/api/auth"
                                .anyExchange().authenticated() // Requerir autenticación para cualquier otro endpoint
                                .and()
                                .httpBasic().disable()
                                // .and()
                                // .httpBasicWithDefaults()
                                .csrf().disable()
                                .build();
        }
}
