package com.curso.diccionarios.controladores.rest.idiomas.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "IdiomasRestV1DTO",
        description = "DTO que incluye los idiomas existentes",
        example = """
                { "data":
                        [
                                {
                                    "codigo": "es",
                                    "nombre": "Español"
                                },
                                {
                                    "codigo": "en",
                                    "nombre": "Inglés"
                                }
                        ]
                }
                """
)
public record IdiomasRestV1DTO(
        @Schema(
                description = "Listado de idiomas"
        )
        List<IdiomaRestV1DTO> data
) implements GetIdiomasRestV1DTO {
}
