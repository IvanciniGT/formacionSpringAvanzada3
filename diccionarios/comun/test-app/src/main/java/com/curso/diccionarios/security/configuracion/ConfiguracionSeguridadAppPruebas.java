package com.curso.diccionarios.security.configuracion;

import com.curso.diccionarios.security.roles.AppDiccionariosRoles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class ConfiguracionSeguridadAppPruebas {

    @Bean
    // A nivel del modulo de diccionarios-seguridad ya tenemos una configuración cors..
    // Pero esta es para pruebas... y en entornos de pruebas quier que esta sobreescriba a la otra.
    // De ahí el @Primary
    @Primary
    public CorsConfigurationSource miFuncionQueConfiguraLaPoliticaDeCorsAppPruebas(){
        CorsConfiguration miPoliticaDeCors = new CorsConfiguration().applyPermitDefaultValues(); // Esto va a ir a pruebas

        UrlBasedCorsConfigurationSource origenes = new UrlBasedCorsConfigurationSource();
        origenes.registerCorsConfiguration("/**", miPoliticaDeCors);

        return origenes;
    }
}
