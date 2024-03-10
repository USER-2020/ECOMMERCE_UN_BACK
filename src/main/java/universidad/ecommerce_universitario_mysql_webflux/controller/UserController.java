package universidad.ecommerce_universitario_mysql_webflux.controller;

import java.time.LocalDateTime;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.dto.TokenAndUserData;
import universidad.ecommerce_universitario_mysql_webflux.exception.CustomException;
import universidad.ecommerce_universitario_mysql_webflux.response.AuthErrorResponse;
import universidad.ecommerce_universitario_mysql_webflux.security.config.JWTAuthResponse;
import universidad.ecommerce_universitario_mysql_webflux.security.config.JWTUtil;
import universidad.ecommerce_universitario_mysql_webflux.security.config.PBKDF2Encoder;
import universidad.ecommerce_universitario_mysql_webflux.service.UserService;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/api/auth")
    public Mono<ResponseEntity<TokenAndUserData>> login(@RequestBody JWTAuthResponse ar) {
        String usernameOrEmail = ar.getUsernameOrEmail(); // Puede ser email o username
        String password = ar.getPassword();

        // Decodificar la cadena (si es necesario)
        Base64.Decoder decoder = Base64.getDecoder();
        String passwordAux = ar.getPassword();
        logger.info(passwordAux);

        // Implementa lógica adicional de decodificación aquí si es necesario

        return userService.obtenerUsuarioPorNombre(usernameOrEmail)
                .doOnNext(user -> {
                    // Registro de información para verificar si se encuentra el usuario
                    logger.info("Usuario encontrado en la base de datos: {}",
                            user != null ? user.getUsername() : "No encontrado");
                })
                .filter(user -> {
                    // Registro de información para verificar si la contraseña coincide
                    boolean passwordMatches = user != null && passwordEncoder.matches(passwordAux, user.getPassword());
                    logger.info("La contraseña coincide: {}", passwordMatches);
                    return passwordMatches;
                })
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    Boolean tokenizacion = jwtUtil.validateToken(token);
                    logger.info("El valor del password es: {}", passwordAux);
                    logger.info("El valor del token es: {}", token);
                    System.out.print(tokenizacion);

                    // Crear un objeto TokenAndUserData con el token y los datos del usuario
                    TokenAndUserData tokenAndUserData = new TokenAndUserData(HttpStatus.OK, token);
                    tokenAndUserData.setData(user); // Configura los datos del usuario

                    return ResponseEntity.ok(tokenAndUserData);
                })
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Invalid credentials")))
                .onErrorResume(e -> {
                    if (e instanceof CustomException) {
                        CustomException customException = (CustomException) e;
                        HttpStatus status = customException.getStatus();
                        String message = customException.getMessage();

                        // Customize your error response structure here
                        AuthErrorResponse authErrorResponse = new AuthErrorResponse(
                                LocalDateTime.now().toString(),
                                "/api/auth",
                                status.value(),
                                "Invalid c",
                                "your-request-id", // customize as needed
                                e.getMessage());

                        // Convert AuthErrorResponse to TokenAndUserData (or create a new
                        // TokenAndUserData)
                        TokenAndUserData tokenAndUserData = new TokenAndUserData(HttpStatus.BAD_REQUEST,
                                "Invalid Credentials");

                        return Mono.just(ResponseEntity.status(status).body(tokenAndUserData));
                    } else {
                        // Handle other types of errors or exceptions
                        // You can customize this part based on your requirements
                        AuthErrorResponse authErrorResponse = new AuthErrorResponse(
                                LocalDateTime.now().toString(),
                                "/api/auth",
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "your-request-id", // customize as needed
                                e.getMessage());

                        // Convert AuthErrorResponse to TokenAndUserData (or create a new
                        // TokenAndUserData)
                        TokenAndUserData tokenAndUserData = new TokenAndUserData(HttpStatus.BAD_REQUEST,
                                "Invalid Credentials");

                        return Mono
                                .just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenAndUserData));
                    }
                });

    }

}
