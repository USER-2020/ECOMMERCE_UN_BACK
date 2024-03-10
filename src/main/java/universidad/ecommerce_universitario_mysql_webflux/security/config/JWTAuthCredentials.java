package universidad.ecommerce_universitario_mysql_webflux.security.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JWTAuthCredentials {
    private String usernameOrEmail;
	private String password;
}
