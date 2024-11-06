package com.curso.diccionarios.controladores.rest.idiomas.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ErrorRestV1DTO",
        description = "DTO que se devuelve cuando se produce un error",
        example = """
               {
                    "tipoError": "ERROR_DE_DATOS"
                    "mensaje": "El campo 'nombre' es obligatorio"
                    "dato": "nombre"
                }""")
public record ErrorRestV1DTO(
        @Schema(
                description = "Tipo de error",
                example = "ERROR_DE_DATOS"
        )
        TipoError tipoError,
        @Schema(
                description = "Mensaje de error",
                example = "El campo 'nombre' es obligatorio"
        )
        String mensaje,
        @Schema(
                description = "Dato que ha causado el error",
                example = "nombre"
        )
        String dato
) implements IdiomaCreadoRestV1DTO, GetIdiomasRestV1DTO {
    public enum TipoError {
        ERROR_INTERNO,
        ERROR_DE_DATOS,
        ERROR_DE_RUTA
    }
}
/*
En versiones anteriores de java, habríamos escrito:
@Getter
@Setter
public sealed class ErrorRestV1DTO implements IdiomaCreadoRestV1DTO{
        private TipoError tipoError;
        private String mensaje;
        private String dato;

        public ErrorRestV1DTO(TipoError tipoError, String mensaje, String dato){
                this.tipoError = tipoError;
                this.mensaje = mensaje;
                this.dato = dato;
        }

        public void setTipoError(TipoError tipoError){
                this.tipoError = tipoError;
        }
}
* */