package universidad.ecommerce_universitario_mysql_webflux.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AuthErrorResponse {
    private final String timestamp;
    private final String path;
    private final int status;
    private final String error;
    private final String requestId;
    private final String message;

    public AuthErrorResponse(String timestamp, String path, int status, String error, String requestId,
            String message) {
        this.timestamp = timestamp;
        this.path = path;
        this.status = status;
        this.error = error;
        this.requestId = requestId;
        this.message = message;
    }

}
