package com.curso.diccionarios.dominio.model;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
public class IdiomaEditable {

    private String idioma;
    @Setter
    private String icono;
    private List<Diccionario> diccionarios;

}
