package com.curso.diccionarios.controladores.rest.idiomas.v1;

import com.curso.diccionarios.AplicacionDePruebas;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.ErrorRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaRestV1DTO;
import com.curso.diccionarios.controladores.services.idiomas.IdiomasService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import com.curso.diccionarios.security.roles.AppDiccionariosRoles;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Spring arranca mi app de pruebas, en paralelo con estas pruebas que voy a ejecutar
// Esa app es una app web.. con lo que necesito que la despliegues en un app server
// Ya le hemos dicho que es una app web (starter: web) y ya Spring me regala el Tomcat
// En qué puerto abre el tomcat? 8080? No tengo garantías de que esté libre
@SpringBootTest(classes = AplicacionDePruebas.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Si Spring abre el tomcat en un puerto NPI de cuál... Como le ataco?? Como le hago un GET?
// Spring configura un cliente de pruebas que esté configurado para atacar a ese servidor
@AutoConfigureMockMvc
// Oye JUnit, que hay parámetros que se los puede pedir a este: A SPRING
@ExtendWith(SpringExtension.class)
// Le pido a Spring que cuando arranque la app de pruebas, la arranque con un perfil concreto: test-api
@ActiveProfiles("test-api")
class IdiomasRestControllerV1ApiTest {

    private final MockMvc clienteHttp;

    private final ObjectMapper transformadorAJSON;

    @MockBean
    private IdiomasService idiomasService;

    private final IdiomasRestControllerV1Impl idiomasRestControllerV1;

    @Autowired
    public IdiomasRestControllerV1ApiTest(MockMvc clienteHttp,
                                          ObjectMapper transformadorAJSON,
                                          IdiomasRestControllerV1Impl idiomasRestControllerV1) {
        this.clienteHttp = clienteHttp;
        this.transformadorAJSON = transformadorAJSON;
        this.idiomasRestControllerV1 = idiomasRestControllerV1;
    }

    @Test
    @DisplayName("Recuperar un idioma por su código")
    @WithAnonymousUser
    void recuperarUnIdiomaPorSuCodigo() throws Exception {
        String codigoIdioma = "es";
        // Dado que el servicio de idiomas devuelve un idioma
        IdiomaRestV1DTO idioma = new IdiomaRestV1DTO(codigoIdioma, "Español");
        when(idiomasService.recuperarIdiomaPorCodigo(codigoIdioma)).thenReturn(idioma);

        idiomasRestControllerV1.recuperarIdioma("codigoIdioma");
        // Aseguraciones
    }
}
