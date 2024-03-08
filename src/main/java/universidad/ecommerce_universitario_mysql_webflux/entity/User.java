package universidad.ecommerce_universitario_mysql_webflux.entity;

import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import universidad.ecommerce_universitario_mysql_webflux.security.config.Role;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

    @Id
    private int id_usuario;

    @NotBlank(message = "Name is obligatory")
    private String username;

    @NotBlank(message = "Email is obligatory")
    private String email;

    @NotBlank(message = "Password  is obligatory")
    private String password;

    @Column("user_roles")
    private Set<Role> roles;

    private Boolean activo;

    private String direccion;

    private String numero_telefono;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private LocalDate fecha_ingreso;

    
}
