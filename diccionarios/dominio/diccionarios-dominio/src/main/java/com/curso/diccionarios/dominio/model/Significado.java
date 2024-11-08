package com.curso.diccionarios.dominio.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class Significado {
    Integer numero;
    String definicion;
    List<String> ejemplos;
    Palabra palabra;
    List<Significado> sinonimos;
    List<Contexto> contextos;
    List<TipoMorfologico> tiposMorfologicos;
}
