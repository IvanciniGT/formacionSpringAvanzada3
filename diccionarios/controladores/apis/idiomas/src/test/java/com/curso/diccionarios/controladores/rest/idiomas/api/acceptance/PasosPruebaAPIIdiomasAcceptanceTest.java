package com.curso.diccionarios.controladores.rest.idiomas.api.acceptance;

import com.curso.diccionarios.AplicacionDePruebas;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Junit, aquí te defino una suite de pruebas
@Suite
// Las pruebas delegalas al CUCUMBER
@IncludeEngines("cucumber") // Puedo poner "cucumber" por haber añadido como dependencia: cucumber-junit-platform-engine
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty,html:target/cucumber.html")
@SpringBootTest(classes = AplicacionDePruebas.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberContextConfiguration // Que cucumber tenga acceso al contexto de spring, para pedirle cosas
@SuppressWarnings("java:S2187")
public class PasosPruebaAPIIdiomasAcceptanceTest {
    private JSONObject objetoJson;
    private final MockMvc clienteHttp;
    private String endpoint;
    private String verbo;
    private ResultActions resultado;
    private boolean envioObjeto = false;

    public PasosPruebaAPIIdiomasAcceptanceTest(MockMvc clienteHttp) {
        this.clienteHttp = clienteHttp;
    }

    @Dado("que tengo un endpoint para dar de alta Idiomas")
    public void que_tengo_un_endpoint_para_dar_de_alta_idiomas() {
    }

    @Dado("que tengo un objeto JSON")
    public void que_tengo_un_objeto_json() {
        objetoJson = new JSONObject();
    }

    @Dado("ese objeto JSON tiene un campo {string} con un valor {string}")
    public void ese_objeto_json_tiene_un_campo_con_un_valor(String campo, String valor) throws JSONException {
        objetoJson.put(campo, valor);
    }

    @Dado("tengo el role {string}")
    @WithMockUser(roles = "Editor")
    public void tengo_el_role(String role) { }

    @Dado("no tengo el role {string}")
    @WithAnonymousUser
    public void no_tengo_el_role(String role) { }

    @Cuando("hago una petición {string} al endpoint {string}")
    public void hago_una_petición_al_endpoint(String verbo, String endpoint) {
        this.endpoint = endpoint;
        this.verbo = verbo;
    }

    @Cuando("mando en el cuerpo el objeto JSON que tengo")
    public void mando_en_el_cuerpo_el_objeto_json_que_tengo() {
        envioObjeto = true;
    }

    @Entonces("recibo un código de respuesta {string}")
    public void recibo_un_código_de_respuesta(String codigo) throws Exception {
        MockHttpServletRequestBuilder request = switch (verbo.toUpperCase()) {
            case "GET" -> get(endpoint);
            case "POST" -> post(endpoint);
            case "PUT" -> put(endpoint);
            case "DELETE" -> delete(endpoint);
            case "HEAD" -> head(endpoint);
            default -> throw new IllegalArgumentException("Verbo no soportado");
        };
        if(envioObjeto) {
            request.contentType("application/json")
                    .content(objetoJson.toString());
        }
        resultado = clienteHttp.perform( request );

        switch (codigo.toUpperCase()) {
            case "CREATED":
                resultado.andExpect(status().isCreated());
                break;
            case "OK":
                resultado.andExpect(status().isOk());
                break;
            case "BAD REQUEST":
                resultado.andExpect(status().isBadRequest());
                break;
            case "FORBIDDEN":
                resultado.andExpect(status().isForbidden());
                break;
            default:
                throw new IllegalArgumentException("Código de respuesta no soportado");
        }
    }

    @Entonces("en el cuerpo de la petición recibo un objeto JSON")
    public void en_el_cuerpo_de_la_petición_recibo_un_objeto_json() throws Exception {
        resultado.andExpect(content().contentType("application/json"));
    }

    @Entonces("el Objeto JSON recibido tiene un campo {string} con un valor {string}")
    public void el_objeto_json_recibido_tiene_un_campo_con_un_valor(String campo, String valor) throws Exception {
        resultado.andExpect(jsonPath("$."+campo).value(valor));
    }

    @Entonces("el Objeto JSON recibido tiene un campo {string} que contiene el texto {string}")
    public void el_objeto_json_recibido_tiene_un_campo_que_contiene_el_texto(String campo, String valor) throws Exception {
        // Sacamos la respuesta como json
        String respuesta = resultado.andReturn().getResponse().getContentAsString();
        // Creamos un objeto json con la respuesta
        JSONObject objetoRespuesta = new JSONObject(respuesta);
        // Comprobamos que el campo contiene el valor
        Assertions.assertTrue(objetoRespuesta.getString(campo).contains(valor));
    }

}
