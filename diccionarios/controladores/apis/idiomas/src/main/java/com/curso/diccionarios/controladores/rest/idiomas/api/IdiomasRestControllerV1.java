package com.curso.diccionarios.controladores.rest.idiomas.api;

import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.ErrorRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaCreadoRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaRestV1DTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Esto es una variante de @Component
// Pero además, Spring espera que aquí definamos endpoints HTTP
// Y esos los da de alta en automático en un tomcat
// Los endpoints de este controlador todos llevan el prefijo /api
@RequestMapping(IdiomasRestControllerV1.BASE_ENDPOINT)
@OpenAPIDefinition(
        info = @Info (
                title = "API REST de Idiomas para la app de diccionarios",
                description = "Este API ofrece funciones para.....",
                version = "1.0.0",
                contact = @Contact(
                        name = "Iván Osuna",
                        email = "ivan@ivan.com"),
                license = @License(
                        name = "Licencia Creative Commons Reconocimiento-NoComercial-CompartirIgual 4.0 Internacional (CC BY-NC-SA 4.0)",
                        url = "https://creativecommons.org/licenses/by-nc-sa/4.0/"
                )
        )
)
public interface IdiomasRestControllerV1 {

    String BASE_ENDPOINT = "/api";
    String CUSTOM_ENDPOINT = "/v1/idiomas";

    // En concreto quiero tener un endpoint que permita get en "/api" (lo que venía de arriba, el prefijo) seguido de /v1/idiomas
    @GetMapping(CUSTOM_ENDPOINT)
    @Operation(
            summary = "Obtiene todos los idiomas",
            description = "Este endpoint devuelve una lista de todos los idiomas disponibles."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de idiomas devuelta correctamente"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    void obtenerIdiomas();

    @PostMapping(CUSTOM_ENDPOINT)
    @Operation(
            summary = "Crea un nuevo idioma",
            description = "Este endpoint crea un nuevo idioma en el sistema."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Idioma creado correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = IdiomaRestV1DTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos incorrectos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorRestV1DTO.class)
                            )
                    )
            }
    )
    IdiomaCreadoRestV1DTO crearIdioma(IdiomaRestV1DTO datosIdioma);

}
