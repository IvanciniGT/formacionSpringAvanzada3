package com.curso.diccionarios.dominio.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ContextoEditable {

    private String contexto;

    @Setter
    private String descripcion;
 }