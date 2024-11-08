package com.curso.diccionarios.dominio.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class Palabra {
    String palabra;
    Diccionario diccionario;
    List<Variante> variantes;
    List<Significado> significados;
}