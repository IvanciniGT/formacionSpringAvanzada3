package com.curso.diccionarios.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class MyUserService {

    // Aqui configuramos nuestra BBDD de detalles adicionales del usuario.
    // Yo la tengo implementada muy cutre
    // Usando una utilidad que nos da Spring, llamada: InMemoryUserDetailsManager
    // Es genial pra pruebas... Para producción, si yo quiero implementar mi propia BBDD de detalles de usuario NO VALE.
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("password")) // Definir contraseña hash
                .roles("User")
                .build();

        UserDetails admin = User.withUsername("editor")
                .password(passwordEncoder().encode("admin123")) // Definir contraseña hash
                .roles("Editor")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
    /*
    @Bean
    public UserDetailsService jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?");
        manager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username=?");
        return manager;
    }
    @Bean
    public UserDetailsService ldapUserDetailsManager(BaseLdapPathContextSource contextSource) {
        LdapUserDetailsManager manager = new LdapUserDetailsManager(contextSource);
        manager.setUserDnPatterns("uid={0},ou=people");
        return manager;
    }
    */
    // Configurar el PasswordEncoder para BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
// COn un proveedor externo, si tiene todos los datos que necesito. NO NECESITO NADA DE ESTO!