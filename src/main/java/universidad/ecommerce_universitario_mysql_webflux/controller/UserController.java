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
import universidad.ecommerce_universitario_mysql_webflux.security.config.JWTAuthResponse;
import universidad.ecommerce_universitario_mysql_webflux.security.config.JWTUtil;
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

    private final UserService userService;

    private JWTUtil jwtUtil;

    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/auth")
    public Mono<ResponseEntity<TokenAndUserData>> login(@RequestBody JWTAuthResponse ar) {
        logger.info("Entre al  metodo de autenticacion");
        String username = ar.getEmail();
        String contrasena = ar.getContrasena();

        logger.info(username + " Contraseña a validar");
        logger.info(contrasena + " Contraseña a validar");

        Base64.Decoder decoder = Base64.getDecoder();
        String passwordAux = ar.getContrasena();
        logger.info(passwordAux);

        return userService.obtenerUsuarioPorCorreo(username)
                .filter(user -> passwordEncoder.matches(passwordAux, user.getContrasena()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    Boolean tokenizacion = jwtUtil.validateToken(token);
                    logger.info("El valor de la contrasena es: {}", passwordAux);
                    logger.info("El valor del token es: {}", token);
                    // System.out.println(tokenizacion);
                    // Crear un objeto TokenAndUserData con el token y los datos del usuario
                    TokenAndUserData tokenAndUserData = new TokenAndUserData(HttpStatus.OK, token);
                    tokenAndUserData.setData(user); // Configura los datos del usuario

                    return ResponseEntity.ok(tokenAndUserData);
                })
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Invalid credentials")));
    }

}
