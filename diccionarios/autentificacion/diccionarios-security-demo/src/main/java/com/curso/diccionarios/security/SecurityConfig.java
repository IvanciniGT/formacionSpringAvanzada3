package com.curso.diccionarios.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    //@Value("${mivar.propiedad}")
    //private String mivar;

    // Definir la cadena de filtros de seguridad
    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MiFiltro jwtRequestFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)                        // Deshabilitar CSRF
                .authorizeHttpRequests(
                        //requests -> requests.anyRequest().permitAll()  // Permitir todas las peticiones
                        requests -> requests.anyRequest().permitAll()  // Permitir todas las peticiones
                                //.requestMatchers("/api/**").authenticated() // Permitir las peticiones a /api autenticadas
                                //.requestMatchers("/admin/**").hasRole("ADMIN") // Permitir las peticiones a /admin con rol ADMIN
                )
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // No se va a mantener la sesión

        // Agregar el filtro JWT antes de la autenticación de username y password
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Configurar el AuthenticationManager para que use el userDetailsService y el passwordEncoder
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Autenticación por usuario y contraseña que nos da Spring
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authProvider);
    }
}

// Si yo usase un proveedor externo de Identidades, el AuthenticationManager no lo monto...