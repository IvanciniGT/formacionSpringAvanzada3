package com.curso.diccionarios.dominio.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class Idioma {

    String codigo;
    String nombre;
    List<Diccionario> diccionarios;

}

// POJO: Plain Old Java Object: Clase con setters y getters.
// Los DTOs son POJOs que se utilizan para transferir datos entre subsistemas de un sistema de información.
// Los modelos de dominio son POJOs que representan conceptos del dominio de un sistema de información.
// Las entidades son POJOs que representan conceptos del dominio de un sistema de información y que se persisten en una base de datos.