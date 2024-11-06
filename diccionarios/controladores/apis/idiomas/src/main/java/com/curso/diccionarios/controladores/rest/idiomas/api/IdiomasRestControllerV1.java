package com.curso.diccionarios.controladores.rest.idiomas.api;

import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.*;
import com.curso.diccionarios.controladores.rest.idiomas.api.validations.CodigoIdiomaValido;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                            description = "Lista de idiomas devuelta correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = IdiomasRestV1DTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorRestV1DTO.class)
                            )
                    )
            }
    )
    GetIdiomasRestV1DTO obtenerIdiomas();

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
    //@ResponseStatus(HttpStatus.CREATED) // Si quitamos el ResponseEntity
    ResponseEntity<IdiomaCreadoRestV1DTO> crearIdioma(@Valid @RequestBody IdiomaRestV1DTO datosIdioma);

    @Operation(
            summary = "Recupera un idioma por su código",
            description = "Este endpoint devuelve un idioma específico dado su código."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Idioma devuelto correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = IdiomaRestV1DTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Código de idioma no válido",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorRestV1DTO.class)
                            )
                    )
            }
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "codigoIdioma",
            description = "Código del idioma a recuperar",
            required = true,
            schema = @Schema(
                    minLength = CodigoIdiomaValido.LONGITUD_MINIMA,
                    maxLength = CodigoIdiomaValido.LONGITUD_MAXIMA,
                    pattern = CodigoIdiomaValido.PATRON,
                    example = "es"
            )
    )
    @GetMapping(CUSTOM_ENDPOINT + "/{codigoIdioma}")
    ResponseEntity<IdiomaCreadoRestV1DTO> recuperarIdioma(

            @CodigoIdiomaValido
            @PathVariable("codigoIdioma") String codigoIdioma);

}
