package universidad.ecommerce_universitario_mysql_webflux.request;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RestController

public class AuthRequest {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
}
