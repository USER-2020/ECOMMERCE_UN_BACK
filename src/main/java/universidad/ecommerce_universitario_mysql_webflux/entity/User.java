package universidad.ecommerce_universitario_mysql_webflux.entity;

import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    // @Column("role")
    // private Set<Role> role;

    @Getter
	@Setter
    private String role;

    @Getter
	@Setter
    private Boolean activo;

    private String direccion;

    private String numero_telefono;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private LocalDate fecha_ingreso;

}
