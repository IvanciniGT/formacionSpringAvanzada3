package com.curso.diccionarios.dominio.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Variante {

    String variante;
    TipoMorfologico tipoMorfologico;

}
