package universidad.ecommerce_universitario_mysql_webflux.security.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JWTUserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final String id_usuario;
    private final String nombre;
    private final String contrasena;
    private final Collection<? extends GrantedAuthority> authorities;

    public JWTUserDetailsImpl(String id_usuario, String nombre, String contrasena,
            Collection<? extends GrantedAuthority> authorities) {
        super();
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return this.authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.id_usuario;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.nombre;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}
