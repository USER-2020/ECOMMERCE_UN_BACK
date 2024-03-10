package universidad.ecommerce_universitario_mysql_webflux.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

public class TokenAndUserData {
    
    public String token;
    @Getter
    @Setter
    private Object data;
    @Getter
    @Setter
    private HttpStatus status;

    public TokenAndUserData(HttpStatus status, String token){
        this.status = status;
        this.token = token;
    }


}
