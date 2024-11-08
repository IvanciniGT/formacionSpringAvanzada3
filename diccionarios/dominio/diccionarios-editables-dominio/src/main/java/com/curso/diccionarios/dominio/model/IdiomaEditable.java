package com.curso.diccionarios.dominio.model;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
public class IdiomaEditable {

    private String codigo;
    @Setter
    private String nombre;
    private List<Diccionario> diccionarios;

}
