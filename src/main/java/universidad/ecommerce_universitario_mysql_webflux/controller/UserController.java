package universidad.ecommerce_universitario_mysql_webflux.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.dto.TokenAndUserData;
import universidad.ecommerce_universitario_mysql_webflux.exception.CustomException;
import universidad.ecommerce_universitario_mysql_webflux.request.AuthRequest;
import universidad.ecommerce_universitario_mysql_webflux.response.AuthResponse;
import universidad.ecommerce_universitario_mysql_webflux.security.config.JWTAuthResponse;
import universidad.ecommerce_universitario_mysql_webflux.security.config.JWTUtil;
import universidad.ecommerce_universitario_mysql_webflux.security.config.PBKDF2Encoder;
import universidad.ecommerce_universitario_mysql_webflux.security.config.WebSecurityConfig;
import universidad.ecommerce_universitario_mysql_webflux.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Base64;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private JWTUtil jwtUtil;
    private PBKDF2Encoder passwordEncoder;
    private UserService userService;

    @PostMapping("/api/auth")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername())
                .filter(userDetails -> passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
                .map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

}
