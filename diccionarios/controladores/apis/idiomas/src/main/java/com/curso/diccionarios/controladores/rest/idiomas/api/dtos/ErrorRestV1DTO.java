package com.curso.diccionarios.controladores.rest.idiomas.api.dtos;

public record ErrorRestV1DTO(
        TipoError tipoError,
        String mensaje,
        String dato
) implements IdiomaCreadoRestV1DTO {
    public enum TipoError {
        ERROR_INTERNO,
        ERROR_DE_DATOS,
        ERROR_DE_RUTA
    }
}
