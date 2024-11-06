package com.curso.diccionarios.controladores.rest.idiomas.api;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test-api")
public class ConfiguracionDeLaAppDePruebasParaAPI {

    @Bean
    @Primary
    public IdiomasRestControllerV1 controladorIdiomasRestV1Mock() {
        return Mockito.mock(IdiomasRestControllerV1.class);
    }
}
