package com.curso.diccionarios.security.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.curso.diccionarios.security.roles.AppDiccionariosRoles;
import java.util.List;

// Le indica a Spring que esta clase debe INSTANCIARLA al arrancar, y buscar las funciones anotadas con @Bean en ella
@Configuration
@EnableWebSecurity
// Añade contextos de seguridad para mi app web
// Añade filtros de seguridad
// Y de hecho configura una politica de seguridad por defecto: DONDE TO-DO ESTA PROHIBIDO POR DEFECTO

// Una vez hecho esto, que va de serie en cualquier proyecto, hemos de decidir cómo se gestionará la seguridad (los permisos)
// - Centralizados      Los permisos los vamos a definir AQUI!
// - Descentralizados   Los permisos los vamos a definir en cada función! << ESTO ES EL CURSO

// Si decido hacer una gestión descentralizada, he de indicar qué métodos van a poder usar los desarrolladores para ir configurando esa seguridad
@EnableMethodSecurity(
        prePostEnabled = true,   // Reglas de Autorización ofrecidas por Spring: @PreAuthorize @PostAuthorize
        securedEnabled = false,  // Otra forma de trabajar con las autorizaciones: @Secured (es muy cutre y se usa poco)
        jsr250Enabled = false    // Autorización según está definida en JEE: @RolesAllowed @PermitAll @DenyAll
)
public class ConfiguracionSeguridad {

    // Como decía, SpringSecurity va a añadir algunos FILTROS que se aplicarán cuando se hagan peticiones HTTP
    // Spring Security va a buscar esi existe un SecurityFilterChain, que haya sido configurado...
    // Vamos a configurarlo
    @Bean
    public SecurityFilterChain misFiltrosDeSeguridad(HttpSecurity httpSecurity, CorsConfigurationSource politicaDeCors) throws Exception { // Solicitud de inyección de dependencias
        httpSecurity
                // Quiero una app stateless. Esto aplica a microservicios, REST
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Desactivamos la seguridad de CSRF Cross site resources forgery
                // En apps stateless no aplica... se desactiva!
                .csrf( CsrfConfigurer::disable )
                // Política de Cors según convenga en el entorno que esté yo trabajando (alguien habrá configurado esa politica
                .cors( cors -> cors.configurationSource(politicaDeCors) )
                // Permisos básicos.
                .authorizeHttpRequests( auth ->
                        //auth.anyRequest().permitAll()           // Permito cualquier request sin autorización previa ni autenticación.
                        auth.requestMatchers("/public/**").permitAll()
                            .requestMatchers("/admin/**").hasRole(AppDiccionariosRoles.ROLE_ADMIN.getNombre())
                            .anyRequest().permitAll()      //authenticated(): ESTO SERIA LO NORMAL EN UNA APP
                );
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource miFuncionQueConfiguraLaPoliticaDeCors(){
        // Una cosa es lo que voy a definir AQUI, que aplicará a la app en entorno de prod...
        // Pero este objeto (BEAN) lo querré sobreescribir para pruebas. En test, vamos a permitir que a mi servidor le ataquen desde cualquier sitio.
        //CorsConfiguration miPoliticaDeCors = new CorsConfiguration().applyPermitDefaultValues(); // Esto va a ir a pruebas
        CorsConfiguration miPoliticaDeCors = new CorsConfiguration();
        miPoliticaDeCors.setAllowedOrigins(List.of("https://origen-permitido1", "https://origen-permitido2"));
        miPoliticaDeCors.setAllowedMethods(List.of("GET", "POST"));
        miPoliticaDeCors.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource origenes = new UrlBasedCorsConfigurationSource();
        origenes.registerCorsConfiguration("/rutas/**", miPoliticaDeCors);

        return origenes;
    }
}
