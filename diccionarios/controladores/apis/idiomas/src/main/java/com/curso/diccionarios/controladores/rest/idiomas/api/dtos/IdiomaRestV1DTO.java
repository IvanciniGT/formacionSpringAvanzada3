package com.curso.diccionarios.controladores.rest.idiomas.api.dtos;

import com.curso.diccionarios.controladores.rest.idiomas.api.validations.CodigoIdiomaValido;
import com.curso.diccionarios.controladores.rest.idiomas.api.validations.NombreIdiomaValido;
import io.swagger.v3.oas.annotations.media.Schema;

// Java 17. Me permite montar fácilmente un DTO. Yo declaro unos datos entre paréntesis
// Y en automático es como si montase una clase que permite acceder a esos datos.
@Schema(
        name = "IdiomaRestV1DTO",
        description = "DTO que incluye los datos de un idioma",
        example = """
                {
                    "codigo": "es",
                    "nombre": "Español"
                }
                """
)
public record IdiomaRestV1DTO(
        @Schema(
                description = "Código del idioma",
                minLength = CodigoIdiomaValido.LONGITUD_MINIMA,
                maxLength = CodigoIdiomaValido.LONGITUD_MAXIMA,
                pattern = CodigoIdiomaValido.PATRON,
                example = "es"
        )
        @CodigoIdiomaValido String codigo,
        @Schema(
                description = "Nombre del idioma",
                minLength = NombreIdiomaValido.LONGITUD_MINIMA,
                maxLength = NombreIdiomaValido.LONGITUD_MAXIMA,
                pattern = NombreIdiomaValido.PATRON,
                example = "Español"
        )
        @NombreIdiomaValido String nombre
) implements IdiomaCreadoRestV1DTO {
}
