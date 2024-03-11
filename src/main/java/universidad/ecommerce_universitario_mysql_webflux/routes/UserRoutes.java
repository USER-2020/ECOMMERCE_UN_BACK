package universidad.ecommerce_universitario_mysql_webflux.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import lombok.extern.slf4j.Slf4j;
import universidad.ecommerce_universitario_mysql_webflux.handler.UserHandler;

@Configuration
@Slf4j
public class UserRoutes {

    private static final String PATH_USERS = "/api/users/admin";

    @Bean
	public RouterFunction<ServerResponse> routerUser(UserHandler handler){
		
		return route(GET(PATH_USERS), handler::getAll)
				.andRoute(GET(PATH_USERS + "/{id}"), handler::getUserById)
//				.andRoute(GET(PATH_USERS + "/{username}"), handler::obtenerUsuarioPorNombre)
				.andRoute(POST(PATH_USERS + "/crear"), handler::createUser)
				.andRoute(PUT(PATH_USERS + "/update/{id}"), handler::updateUser)
				.andRoute(DELETE(PATH_USERS + "/eliminar/{id}"), handler::deleteUser);
				
	}
}
