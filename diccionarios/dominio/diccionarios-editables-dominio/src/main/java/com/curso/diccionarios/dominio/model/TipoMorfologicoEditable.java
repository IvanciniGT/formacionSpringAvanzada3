package com.curso.diccionarios.dominio.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Builder

public class TipoMorfologicoEditable  {

    private String tipo; // sm
    @Setter
    private String descripcion;  // Sustantivo Masculino
}
