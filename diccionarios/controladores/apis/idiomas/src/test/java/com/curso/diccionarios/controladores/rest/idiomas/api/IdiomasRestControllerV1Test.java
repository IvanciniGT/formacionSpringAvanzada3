package com.curso.diccionarios.controladores.rest.idiomas.api;

import com.curso.diccionarios.AplicacionDePruebas;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.junit.jupiter.api.Assertions.*;
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
class IdiomasRestControllerV1Test {

    private MockMvc clienteHttp;

    // Quiero tener una implementación de mentirijilla de mi controlador rest
    // Mockito genera una implementación DUMMY de mi controlador.. e inyectala en el contexto de Spring
    // De forma que al arranque la tengas en cuenta:
    // - Si alguien pide una instancia de ese interfaz (IdiomasRestControllerV1), dale esta (ESTO NO LO VAMOS A USAR)
    // - Como será un RestController, móntale los endpoints en un tomcat embebido
    @MockBean
    private IdiomasRestControllerV1 idiomasRestControllerV1;

    // Para informar a JUnit que este parámetro lo debe proveer Spring
    public IdiomasRestControllerV1Test(@Autowired MockMvc clienteHttp) {
        this.clienteHttp = clienteHttp;
    }

    // Qué tipo de prueba estoy definiendo?
    // Funcionales + Integración (porque estamos testeando un endpoint: http)
    // Yo llamo a Tomcat -> Spring -> Controlador

    // Al aplicar TDD vamos a ir definiendo las pruebas más sencillas que pueda... paso a paso
    // Una vez definida una prueba, escribo el código que la super... EL MINIMO que pueda
    // Una vez superada, refactorizo el código si es necesario
    // Y continuo con más pruebas.. o amplio la prueba actual

    @Test
    @DisplayName("Debemos tener un endpoint llamado /api/v1/idiomas y le debemos poder hacer un GET")
    void testEndpointIdiomas() throws Exception {
        // dado que tengo un cliente http y una app en un tomcat.,..
        // cuando
        clienteHttp.perform(get("/api/v1/idiomas"))
                // entonces
                .andExpect(status().isOk()); // Código http 200
    }
}