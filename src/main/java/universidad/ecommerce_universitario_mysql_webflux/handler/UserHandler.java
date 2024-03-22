package universidad.ecommerce_universitario_mysql_webflux.handler;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.response.UserResponse;
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

	public Mono<ServerResponse> getUserById(ServerRequest request) {
		int id = Integer.valueOf(request.pathVariable("id"));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(userService.obtenerUsuarioPorId(id)
						.map(u -> {
							return u;
						}), User.class)
				.switchIfEmpty(ServerResponse.notFound().build());

	}

	// This `createUser` method in the `UserHandler` class is responsible for
	// handling the creation of
	// a new user. Here's a breakdown of what it does:
	public Mono<ServerResponse> createUser(ServerRequest request) {
		Mono<User> userMono = request.bodyToMono(User.class);
		String role = request.queryParam("role").orElse("ROLE_ADMIN");

		return userMono.flatMap(user -> {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			user.setRole(role);

			return userService.guardarUsuario(user)
					.flatMap(savedUser -> {
						UserResponse userResponse = new UserResponse(HttpStatus.OK, "Usuario creado exitosamente");
						return ServerResponse.ok().bodyValue(userResponse);
					})
					.switchIfEmpty(ServerResponse.badRequest().build());
		});
	}

	public Mono<ServerResponse> updateUser(ServerRequest request) {
		int id = Integer.valueOf(request.pathVariable("id"));
		Mono<User> userMono = request.bodyToMono(User.class).doOnNext(validator::validate);
		return userService.obtenerUsuarioPorId(id)
				.zipWith(userMono, (user, updateUser) -> {
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

	public Mono<ServerResponse> deleteUser(ServerRequest request) {
		int id = Integer.valueOf(request.pathVariable("id"));
		Mono<User> userMono = userService.obtenerUsuarioPorId(id);
		return userMono
				.flatMap(userToDelete -> userService.eliminarUsuario(userToDelete)
						.then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());

	}

}
