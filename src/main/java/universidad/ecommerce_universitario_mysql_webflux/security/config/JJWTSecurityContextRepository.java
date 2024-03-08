package universidad.ecommerce_universitario_mysql_webflux.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JJWTSecurityContextRepository implements ServerSecurityContextRepository {

    // private JWTAuthenticationManager authenticationManager;

    // public JJWTSecurityContextRepository(JWTAuthenticationManager
    // authenticationManager) {
    // this.authenticationManager = authenticationManager;
    // }

    // @Override
    // public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    // throw new UnsupportedOperationException(
    // "Not supported: should only be used in combination with `saveWhenComplete'");
    // }

    // @Override
    // public Mono<SecurityContext> load(ServerWebExchange exchange) {
    // return
    // Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
    // .filter(authHeader -> authHeader.startsWith("Bearer "))
    // .flatMap(authHeader -> {
    // String authToken = authHeader.substring(7);// Cadena de caracteres que
    // corresponde a Bearer + ""
    // Authentication auth = new UsernamePasswordAuthenticationToken(authToken,
    // authToken);
    // return
    // this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
    // });
    // }
    private final JWTAuthenticationManager jwtAuthenticationManager;

    public JJWTSecurityContextRepository(JWTAuthenticationManager jwtAuthenticationManager) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = exchange.getAttribute("token");
        return jwtAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, token))
                .map(SecurityContextImpl::new);
    }

}
