package com.curso.diccionarios.controladores.rest.idiomas.api.dtos;

// Java 17. Me permite montar fácilmente un DTO. Yo declaro unos datos entre paréntesis
// Y en automático es como si montase una clase que permite acceder a esos datos.
public record IdiomaRestV1DTO(
        String codigo,
        String idioma
) implements IdiomaCreadoRestV1DTO {
}
