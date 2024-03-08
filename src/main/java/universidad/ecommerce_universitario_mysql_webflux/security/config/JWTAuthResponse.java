package universidad.ecommerce_universitario_mysql_webflux.security.config;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JWTAuthResponse {
    private int id_usuario;
    private String nombre;
    private String email;
    private String contrasena;
    private String role;
    private Boolean activo;
    private String token;
    private String direccion;
    private String numero_telefono;
    private Date  fecha_ingreso;
    private Object dataUser; // Propiedad para los datos de la respuesta
    private HttpStatus status; // Propiedad para el estado de la respuesta
}
