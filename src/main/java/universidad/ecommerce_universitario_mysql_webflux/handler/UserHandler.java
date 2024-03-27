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
import universidad.ecommerce_universitario_mysql_webflux.controller.UserController;
import universidad.ecommerce_universitario_mysql_webflux.entity.User;
import universidad.ecommerce_universitario_mysql_webflux.response.DataAndStatusResponse;
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
		UserResponse notfoundResponse = new UserResponse(HttpStatus.NOT_FOUND, "Usuarios no encontrados");
		return userService.obtenerUsuarios()
				.collectList() // Recolecta todos los usuarios en una lista
				.flatMap(users -> {
					if (users.isEmpty()) {
						// Si la lista de usuarios está vacía, devolver una respuesta not found
						return ServerResponse.status(HttpStatus.NOT_FOUND)
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(notfoundResponse);
					} else {
						// Si hay usuarios, devolver una respuesta OK con la lista de usuarios
						DataAndStatusResponse dataResponse = new DataAndStatusResponse(HttpStatus.OK, users);
						return ServerResponse.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(dataResponse);
					}
				});
	}

	public Mono<ServerResponse> getUserById(ServerRequest request) {
		int id = Integer.valueOf(request.pathVariable("id"));
		return userService.obtenerUsuarioPorId(id)
				.flatMap(user -> {
					DataAndStatusResponse response = new DataAndStatusResponse(HttpStatus.OK, user);
					return ServerResponse.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.bodyValue(response);
				})
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
		logger.info("Actualizando usuario con ID: {}", id);

		// Crear un objeto UserResponse para notificar que el usuario fue actualizado
		// exitosamente
		UserResponse userResponse = new UserResponse(HttpStatus.OK, "Usuario actualizado exitosamente");

		// Crear un objeto UserResponse para notificar que el usuario no fue encontrado
		UserResponse notfoundResponse = new UserResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado");

		return userService.obtenerUsuarioPorId(id)
				.flatMap(user -> {
					logger.info("Información del usuario obtenida: {}", user);

					// Actualizar el usuario con los datos del body de la solicitud
					return userMono.zipWith(Mono.just(user), (updateUser, originalUser) -> {
						String encodedPassword = passwordEncoder.encode(updateUser.getPassword());
						originalUser.setUsername(updateUser.getUsername());
						originalUser.setEmail(updateUser.getEmail());
						originalUser.setPassword(encodedPassword);
						originalUser.setNumero_telefono(updateUser.getNumero_telefono());
						originalUser.setRole(updateUser.getRole());
						originalUser.setActivo(true);
						originalUser.setFecha_ingreso(user.getFecha_ingreso());
						return originalUser;
					});
				})
				.flatMap(userService::guardarUsuario)
				.flatMap(updateUser -> {
					logger.info("Usuario actualizado correctamente: {}", updateUser);
					// Si el usuario fue actualizado correctamente, se devuelve una respuesta OK
					return ServerResponse.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.bodyValue(userResponse);
				})
				// Si el usuario no fue encontrado, se devuelve una respuesta not found
				.switchIfEmpty(Mono.just(notfoundResponse)
						.flatMap(response -> ServerResponse.status(response.getStatus())
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(response)));

	}

	public Mono<ServerResponse> deleteUser(ServerRequest request) {
		int id = Integer.valueOf(request.pathVariable("id"));
		Mono<User> userMono = userService.obtenerUsuarioPorId(id);
		logger.info("Eliminando usuario con ID: {}", id);
		logger.info("Info de usuario a eliminar: {}", userMono);
		/* Objeto perzonalizada para usuario eliminado .OK */
		UserResponse userResponse = new UserResponse(HttpStatus.OK, "Usuario eliminado exitosamente");
		/* Obejto perzonalizado para usuario no encontrado .NOT FOUND */
		UserResponse notfoundResponse = new UserResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		return userMono
				.flatMap(userToDelete -> userService.eliminarUsuario(userToDelete)
						.then(ServerResponse.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(userResponse)))
				.switchIfEmpty(Mono.just(notfoundResponse)
						.flatMap(response -> ServerResponse.status(response.getStatus())
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(response)));

	}

}
