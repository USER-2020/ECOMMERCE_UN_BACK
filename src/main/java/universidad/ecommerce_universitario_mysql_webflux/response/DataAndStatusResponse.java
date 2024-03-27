package universidad.ecommerce_universitario_mysql_webflux.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

public class DataAndStatusResponse {
    @Getter
    @Setter
    private HttpStatus status;
    @Getter
    @Setter
    private Object data;
    
    public DataAndStatusResponse(HttpStatus status, Object data) {
        this.status = status;
        this.data = data;
    }

    

}
