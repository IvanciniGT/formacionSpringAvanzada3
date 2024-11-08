package com.curso.diccionarios.dominio.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder

public class PalabraEditable  {
    private String palabra;
    private Diccionario diccionario;

    private List<Variante> variantes;

    private List<SignificadoEditable> significados;
}
