package com.curso.diccionarios.jpa.dominio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_morfologicos")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TipoMorfologicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 10, unique = true)
    private String tipo; // sm

    @Column(nullable = false, length = 100)
    private String descripcion;  // Sustantivo Masculino
}
