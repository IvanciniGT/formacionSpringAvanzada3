package com.curso.diccionarios.controladores.rest.idiomas.v1;

import com.curso.diccionarios.AplicacionDePruebas;
import com.curso.diccionarios.controladores.rest.idiomas.api.IdiomasRestControllerV1;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.*;
import com.curso.diccionarios.services.idiomas.dtos.IdiomaDTO;
import com.curso.diccionarios.services.idiomas.IdiomasService;
import org.junit.jupiter.api.Assertions;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


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
class IdiomasRestControllerV1ImplUnitTest {

    private final MockMvc clienteHttp;

    @MockBean // Por debajo hace un Autowired
    private IdiomasService idiomasService;

    private final IdiomasRestControllerV1 idiomasRestControllerV1;

    @Autowired
    public IdiomasRestControllerV1ImplUnitTest(IdiomasRestControllerV1Impl idiomasRestControllerV1, MockMvc clienteHttp) {
        this.idiomasRestControllerV1 = idiomasRestControllerV1;
        this.clienteHttp = clienteHttp;
    }

    @Test
    @DisplayName("Recuperar un idioma por su código")
    @WithAnonymousUser
    void recuperarUnIdiomaPorSuCodigo()  {
        String codigoIdioma = "en";
        String nombreIdioma = "Inglés";
        // Dado que el servicio de idiomas devuelve un idioma
        IdiomaDTO idiomaServicio = new IdiomaDTO(codigoIdioma, nombreIdioma);
        when(idiomasService.recuperarIdiomaPorCodigo(codigoIdioma)).thenReturn(Optional.of(idiomaServicio));

        ResponseEntity<IdiomaCreadoRestV1DTO> resultado = idiomasRestControllerV1.recuperarIdioma(codigoIdioma);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(HttpStatus.OK, resultado.getStatusCode());
        Assertions.assertNotNull(resultado.getBody());
        Assertions.assertEquals(IdiomaRestV1DTO.class, resultado.getBody().getClass());
        Assertions.assertEquals(codigoIdioma, ((IdiomaRestV1DTO)resultado.getBody()).codigo());
        Assertions.assertEquals(nombreIdioma, ((IdiomaRestV1DTO)resultado.getBody()).nombre());
    }

    @Test
    @DisplayName("Recuperar idiomas existentes")
    @WithAnonymousUser
    void recuperarTodosLosIdiomas() {
        String codigoIdioma1 = "en";
        String nombreIdioma1 = "Inglés";
        String codigoIdioma2 = "es";
        String nombreIdioma2 = "Español";
        // Dado que el servicio de idiomas devuelve un idioma
        IdiomaDTO idiomaServicio1 = new IdiomaDTO(codigoIdioma1, nombreIdioma1);
        IdiomaDTO idiomaServicio2 = new IdiomaDTO(codigoIdioma2, nombreIdioma2);
        when(idiomasService.recuperarTodosLosIdiomas()).thenReturn(
                List.of(idiomaServicio1, idiomaServicio2)
        );

        GetIdiomasRestV1DTO resultado = idiomasRestControllerV1.obtenerIdiomas();
        Assertions.assertNotNull(resultado);
        Assertions.assertInstanceOf(IdiomasRestV1DTO.class, resultado);
        Assertions.assertEquals(2, ((IdiomasRestV1DTO)resultado).data().size());
        Assertions.assertEquals(codigoIdioma1, ((IdiomasRestV1DTO)resultado).data().get(0).codigo());
        Assertions.assertEquals(nombreIdioma1, ((IdiomasRestV1DTO)resultado).data().get(0).nombre());
        Assertions.assertEquals(codigoIdioma2, ((IdiomasRestV1DTO)resultado).data().get(1).codigo());
        Assertions.assertEquals(nombreIdioma2, ((IdiomasRestV1DTO)resultado).data().get(1).nombre());
    }

    @Test
    @DisplayName("Crear un nuevo idioma")
    @WithMockUser(roles = "Editor")
    void crearUnNuevoIdioma() {
        String codigoIdioma = "en";
        String nombreIdioma = "Inglés";
        // Dado que el servicio de idiomas devuelve un idioma
        IdiomaDTO idiomaServicio = new IdiomaDTO(codigoIdioma, nombreIdioma);
        when(idiomasService.nuevoIdioma(any(IdiomaDTO.class))).thenReturn(idiomaServicio);

        IdiomaRestV1DTO idiomaACrear = new IdiomaRestV1DTO(codigoIdioma, nombreIdioma);
        ResponseEntity<IdiomaCreadoRestV1DTO> resultado = idiomasRestControllerV1.crearIdioma(idiomaACrear);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        Assertions.assertNotNull(resultado.getBody());
        Assertions.assertEquals(IdiomaRestV1DTO.class, resultado.getBody().getClass());
        Assertions.assertEquals(codigoIdioma, ((IdiomaRestV1DTO) resultado.getBody()).codigo());
        Assertions.assertEquals(nombreIdioma, ((IdiomaRestV1DTO) resultado.getBody()).nombre());
    }

    @Test
    @DisplayName("Recuperar un idioma que no existe")
    @WithAnonymousUser
    void recuperarUnIdiomaNoExiste()  {
        String codigoIdioma = "en";
        // Dado que el servicio de idiomas devuelve un idioma
        when(idiomasService.recuperarIdiomaPorCodigo(codigoIdioma)).thenReturn(Optional.empty());

        ResponseEntity<IdiomaCreadoRestV1DTO> resultado = idiomasRestControllerV1.recuperarIdioma(codigoIdioma);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
    }

    @Test
    @DisplayName("Recuperar un idioma con código muy largo")
    @WithAnonymousUser
    void recuperarUnIdiomaNoValido() throws Exception {
        String codigoIdioma = "english";
        // Dado que el servicio de idiomas devuelve un idioma
        clienteHttp.perform(get("/api/v1/idiomas/"+codigoIdioma))
                .andExpect(status().isBadRequest());
        // Y Revisar el DTO de error que se genera

    }
}
