package universidad.ecommerce_universitario_mysql_webflux.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;
import universidad.ecommerce_universitario_mysql_webflux.exception.CustomException;
import universidad.ecommerce_universitario_mysql_webflux.exception.UnauthorizedException;

@Component
public class JWTFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private JWTUtil jwtUtil;

    private JJWTSecurityContextRepository jjwtSecurityContextRepository;

    @Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getPath().value();
		if (path.contains("/api/auth") || path.contains("/api/register") || path.contains("/api/email/send") || path.contains("/api/email/send-html"))
			return chain.filter(exchange);
		String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (auth == null)
			return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "el token no se encontro!!"));
		if (!auth.startsWith("Bearer "))
			return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "autentificacion inavlida!!"));
		String token = auth.replace("Bearer ", "");
		exchange.getAttributes().put("token", token);
		return chain.filter(exchange);

	}

}
