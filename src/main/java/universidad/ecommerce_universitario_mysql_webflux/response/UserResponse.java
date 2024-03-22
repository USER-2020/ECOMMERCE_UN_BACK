package universidad.ecommerce_universitario_mysql_webflux.response;

import org.springframework.http.HttpStatus;

public class UserResponse {
    
    HttpStatus status;
    String message;

    public UserResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
    
}
