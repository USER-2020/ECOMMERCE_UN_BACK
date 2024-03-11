package universidad.ecommerce_universitario_mysql_webflux.handler;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.security.config.WebSecurityConfig;
import universidad.ecommerce_universitario_mysql_webflux.service.UserService;

@Component
@Slf4j
public class UserHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private Validator validator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.obtenerUsuarios()
                .map(users -> {
                    return users;

                }), User.class);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request){
		int id = Integer.valueOf(request.pathVariable("id"));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(userService.obtenerUsuarioPorId(id)
						.map(u->{
							return u;
						}), User.class)
				.switchIfEmpty(ServerResponse.notFound().build());
		
	}

    public Mono<ServerResponse> createUser(ServerRequest request){
		Mono<User> user = request.bodyToMono(User.class);
		String role = request.queryParam("role").orElse("ROLE_ADMIN");
		return user.flatMap(u-> {
			 String encodedPassword = passwordEncoder.encode(u.getPassword());
		        u.setPassword(encodedPassword);
		        u.setRole(role); // establecer el rol del usuario
		        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.guardarUsuario(u), User.class);
		});
	}

    public Mono<ServerResponse> updateUser(ServerRequest request){
		int id = Integer.valueOf(request.pathVariable("id"));
		Mono<User> userMono = request.bodyToMono(User.class).doOnNext(validator::validate);
		return userService.obtenerUsuarioPorId(id)
				.zipWith(userMono, (user, updateUser)->{
					user.setUsername(updateUser.getUsername());
					user.setEmail(updateUser.getEmail());
					user.setPassword(updateUser.getPassword());
					user.setNumero_telefono(updateUser.getNumero_telefono());
					user.setRole(updateUser.getRole());
					return user;
				
				})
				.flatMap(userService::guardarUsuario)
				.flatMap(updateUser -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(updateUser))
				.switchIfEmpty(ServerResponse.notFound().build());
		}
	
        public Mono<ServerResponse> deleteUser(ServerRequest request){
            int id = Integer.valueOf(request.pathVariable("id"));
            Mono<User> userMono = userService.obtenerUsuarioPorId(id);
            return userMono
                    .flatMap(userToDelete -> userService.eliminarUsuario(userToDelete)
                            .then(ServerResponse.noContent().build()))
                    .switchIfEmpty(ServerResponse.notFound().build());
            
        }

}
