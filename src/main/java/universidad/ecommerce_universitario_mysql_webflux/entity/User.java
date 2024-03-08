package universidad.ecommerce_universitario_mysql_webflux.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

    @Id
    private int id_usuario;
    @Getter
    @Setter
    @NotBlank(message = "Name is obligatory")
    private String nombre;
    @Getter
    @Setter
    @NotBlank(message = "Email is obligatory")
    private String email;
    @NotBlank(message = "Password  is obligatory")
    private String contrasena;
    @Getter
    @Setter
    @NotBlank(message = "Rol is obligatory")
    private String role;
    @Getter
    @Setter
    private Boolean activo; // Activo o Inactivo
    @Getter
    @Setter
    private String direccion;
    @Getter
    @Setter
    private String numero_telefono;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private LocalDate fecha_ingreso;
}
