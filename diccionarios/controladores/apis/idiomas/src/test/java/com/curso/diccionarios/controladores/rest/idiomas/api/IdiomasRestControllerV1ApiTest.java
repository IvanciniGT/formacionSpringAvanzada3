package com.curso.diccionarios.controladores.rest.idiomas.api;

import com.curso.diccionarios.AplicacionDePruebas;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.ErrorRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaRestV1DTO;
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

    private final IdiomasRestControllerV1 idiomasRestControllerV1;
    // Quiero tener una implementación de mentirijilla de mi controlador rest
    // Mockito genera una implementación DUMMY de mi controlador.. e inyectala en el contexto de Spring
    // De forma que al arranque la tengas en cuenta:
    // - Si alguien pide una instancia de ese interfaz (IdiomasRestControllerV1), dale esta (ESTO NO LO VAMOS A USAR)
    // - Como será un RestController, móntale los endpoints en un tomcat embebido
    @MockBean
    private MiIdiomasRestControllerAdvice idiomasRestControllerAdvice;

    // Para informar a JUnit que este parámetro lo debe proveer Spring
    @Autowired
    public IdiomasRestControllerV1ApiTest(MockMvc clienteHttp,
                                          ObjectMapper transformadorAJSON,
                                          IdiomasRestControllerV1 idiomasRestControllerV1) {
        this.clienteHttp = clienteHttp;
        this.transformadorAJSON = transformadorAJSON;
        this.idiomasRestControllerV1 = idiomasRestControllerV1;
    }

    // Qué tipo de prueba estoy definiendo?
    // Funcionales + Integración (porque estamos testeando un endpoint: http)
    // Yo llamo a Tomcat -> Spring -> Controlador

    // Al aplicar TDD vamos a ir definiendo las pruebas más sencillas que pueda... paso a paso
    // Una vez definida una prueba, escribo el código que la super... EL MINIMO que pueda
    // Una vez superada, refactorizo el código si es necesario
    // Y continuo con más pruebas.. o amplio la prueba actual

    @Test
    @DisplayName("Debemos tener un endpoint llamado " +
            IdiomasRestControllerV1.BASE_ENDPOINT +
            IdiomasRestControllerV1.CUSTOM_ENDPOINT +
            " y le debemos poder hacer un GET")
    void testEndpointIdiomas() throws Exception {
        // dado que tengo un cliente http y una app en un tomcat.,..
        // cuando
        clienteHttp.perform(get(IdiomasRestControllerV1.BASE_ENDPOINT + IdiomasRestControllerV1.CUSTOM_ENDPOINT))
                // entonces
                .andExpect(status().isOk()); // Código http 200
    }
/*
    @Test
    @DisplayName("Si llamo al endPoint de recuperación de idiomas: " +
            IdiomasRestControllerV1.BASE_ENDPOINT +
            IdiomasRestControllerV1.CUSTOM_ENDPOINT +
            " y hay un problema no controlado, debe devolver un código 500")
    void testErrorEndpointIdiomas() throws Exception {
        // dado que tengo un cliente http y una app en un tomcat.,..
        // cuando
        clienteHttp.perform(get(IdiomasRestControllerV1.BASE_ENDPOINT + IdiomasRestControllerV1.CUSTOM_ENDPOINT))
                // entonces
                .andExpect(status().isInternalServerError());
    }*/
    @Test
    @DisplayName("No debo poder crear un idioma con datos guays sin esta autenticado haciendo un POST a: " +
            IdiomasRestControllerV1.BASE_ENDPOINT +
            IdiomasRestControllerV1.CUSTOM_ENDPOINT +
            " y me tiene que devolver un 403 y un JSON con los datos del idioma creado")
    @WithAnonymousUser
    void testCrearIdiomaGuaySinRoleEditor() throws Exception {

        IdiomaRestV1DTO idioma = new IdiomaRestV1DTO("ES", "Español");
        // Le digo a mockito, cuanto te llamen en el controlador ese que TU VAS A CREAR
        // A la funcion crearIdioma, pasándote esos datos devuelve este idioma
        //                                          VV                VV
        clienteHttp.perform(
                        post(IdiomasRestControllerV1.BASE_ENDPOINT + IdiomasRestControllerV1.CUSTOM_ENDPOINT)
                                .contentType("application/json")
                                .content(transformadorAJSON.writeValueAsString(idioma))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debo poder crear un idioma con datos guays si soy un usuario con role "+AppDiccionariosRoles.ROLE_EDITOR_NAME+" haciendo un POST a: " +
            IdiomasRestControllerV1.BASE_ENDPOINT +
            IdiomasRestControllerV1.CUSTOM_ENDPOINT +
            " y me tiene que devolver un 201 y un JSON con los datos del idioma creado")
    @WithMockUser( username = "pepe", roles = {AppDiccionariosRoles.ROLE_EDITOR_NAME})
    void testCrearIdiomaGuay() throws Exception {

        IdiomaRestV1DTO idioma = new IdiomaRestV1DTO("ES", "Español");
        // Le digo a mockito, cuanto te llamen en el controlador ese que TU VAS A CREAR
        // A la funcion crearIdioma, pasándote esos datos devuelve este idioma
        //                                          VV                VV
        when(idiomasRestControllerV1.crearIdioma(idioma)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(idioma));

        // Código: ES
        // Nombre: Español
        clienteHttp.perform(
                        post(IdiomasRestControllerV1.BASE_ENDPOINT + IdiomasRestControllerV1.CUSTOM_ENDPOINT)
                                .contentType("application/json")
                                .content(transformadorAJSON.writeValueAsString(idioma))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(transformadorAJSON.writeValueAsString(idioma)));
    }
    // El código del pais no puede ser nulo, se admiten números? NO... Al menos cuantas letras? 2 y como mucho? 5
    // Y si me pasan algo así? Entonces debo devolver un BAD REQUEST... y voy a devolver algo en el BODY?
    // Lo suyo sería algo de información relativa al error: El campo código del idioma no puede ser nulo
    @Test
    @DisplayName("Si intento crear un idioma cuyo código tenga menos de 2 letras, debe devolver un 400 y los datos del error")
    void testCrearIdiomaConCodigoCorto() throws Exception {
        ErrorRestV1DTO valorADevolver = new ErrorRestV1DTO(ErrorRestV1DTO.TipoError.ERROR_DE_DATOS, "El código de idioma debe tener entre 2 y 5 caracteres", "codigo");
        IdiomaRestV1DTO idioma = new IdiomaRestV1DTO("E", "Español");
        validarErrorRespuestaCreacion(idioma, valorADevolver);
    }
    // El código del pais no puede ser nulo, se admiten números? NO... Al menos cuantas letras? 2 y como mucho? 5
    // Y si me pasan algo así? Entonces debo devolver un BAD REQUEST... y voy a devolver algo en el BODY?
    // Lo suyo sería algo de información relativa al error: El campo código del idioma no puede ser nulo
    @Test
    @DisplayName("Si intento crear un idioma cuyo código tenga más de 5 letras, debe devolver un 400 y los datos del error")
    void testCrearIdiomaConCodigoLargo() throws Exception {
        ErrorRestV1DTO valorADevolver = new ErrorRestV1DTO(ErrorRestV1DTO.TipoError.ERROR_DE_DATOS, "El código de idioma debe tener entre 2 y 5 caracteres", "codigo");
        IdiomaRestV1DTO idioma = new IdiomaRestV1DTO("ESPANA", "Español");
        validarErrorRespuestaCreacion(idioma, valorADevolver);
    }
    @Test
    @DisplayName("Si intento crear un idioma cuyo código tenga cosas raras, debe devolver un 400 y los datos del error")
    void testCrearIdiomaConCodigoRaro() throws Exception {
        ErrorRestV1DTO valorADevolver = new ErrorRestV1DTO(ErrorRestV1DTO.TipoError.ERROR_DE_DATOS, "El código de idioma solo puede contener letras y guión bajo", "codigo");
        IdiomaRestV1DTO idioma = new IdiomaRestV1DTO("ESPAÑA", "Español");
        validarErrorRespuestaCreacion(idioma, valorADevolver);
    }
    @Test
    @DisplayName("Si intento crear un idioma sin código, debe devolver un 400 y los datos del error")
    void testCrearIdiomaSinCodigo() throws Exception {
        ErrorRestV1DTO valorADevolver = new ErrorRestV1DTO(ErrorRestV1DTO.TipoError.ERROR_DE_DATOS, "El código de idioma no puede ser nulo", "codigo");
        IdiomaRestV1DTO idioma = new IdiomaRestV1DTO(null, "Español");
        validarErrorRespuestaCreacion(idioma, valorADevolver);
    }

    private void validarErrorRespuestaCreacion(IdiomaRestV1DTO idioma, ErrorRestV1DTO valorADevolver) throws Exception {
        when(idiomasRestControllerAdvice.argumentoNoValido(any())).thenReturn(valorADevolver);

        clienteHttp.perform(
                        post(IdiomasRestControllerV1.BASE_ENDPOINT + IdiomasRestControllerV1.CUSTOM_ENDPOINT)
                                .contentType("application/json")
                                .content(transformadorAJSON.writeValueAsString(idioma))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(transformadorAJSON.writeValueAsString(valorADevolver)));

    }

    @Test
    @DisplayName("Debo poder recuperar un idioma que exista haciendo un GET a: " +
            IdiomasRestControllerV1.BASE_ENDPOINT +
            IdiomasRestControllerV1.CUSTOM_ENDPOINT +
            "/{codigo} y me tiene que devolver un 200 y un JSON con los datos del idioma creado")
    void testRecuperarIdiomaGuay() throws Exception {

        String codigoIdioma = "ES";
        IdiomaRestV1DTO idioma = new IdiomaRestV1DTO(codigoIdioma, "Español");
        when(idiomasRestControllerV1.recuperarIdioma(codigoIdioma)).thenReturn(ResponseEntity.status(HttpStatus.OK).body(idioma));

        clienteHttp.perform(
                        get(IdiomasRestControllerV1.BASE_ENDPOINT + IdiomasRestControllerV1.CUSTOM_ENDPOINT + "/" + codigoIdioma)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(transformadorAJSON.writeValueAsString(idioma)));
    }
    @Test
    @DisplayName("Debo no poder recuperar un idioma con un código muy corto: " +
            IdiomasRestControllerV1.BASE_ENDPOINT +
            IdiomasRestControllerV1.CUSTOM_ENDPOINT +
            "/{codigo} y me tiene que devolver un 400 y un JSON con los datos del error")
    void testRecuperarIdiomaCorto() throws Exception {
        ErrorRestV1DTO valorADevolver = new ErrorRestV1DTO(ErrorRestV1DTO.TipoError.ERROR_DE_DATOS, "El código de idioma debe tener entre 2 y 5 caracteres", "codigo");

        String codigoIdioma = "E";
        when(idiomasRestControllerAdvice.argumentoNoValido(any())).thenReturn(valorADevolver);

        clienteHttp.perform(
                        get(IdiomasRestControllerV1.BASE_ENDPOINT + IdiomasRestControllerV1.CUSTOM_ENDPOINT + "/" + codigoIdioma)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(transformadorAJSON.writeValueAsString(valorADevolver)));
    }

    @RestControllerAdvice
    public interface MiIdiomasRestControllerAdvice{
        @ExceptionHandler({MethodArgumentNotValidException.class,HandlerMethodValidationException.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        ErrorRestV1DTO argumentoNoValido(Exception ex);
    }
}
