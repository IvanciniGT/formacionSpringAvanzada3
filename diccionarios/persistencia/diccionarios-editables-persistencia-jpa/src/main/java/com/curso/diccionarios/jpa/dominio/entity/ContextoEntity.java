package com.curso.diccionarios.jpa.dominio.entity;

import jakarta.persistence.*;       // En versiones de JAVA < 17: import javax.persistence.*;
import lombok.*;

// Qué tipo de lenguaje estamos aplicando: DECLARATIVO
@Entity                             // Objeto persistible en BBDD... que necesita un ID
@Table(name = "contextos")          // En que tabla de la BBDD se guarda
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ContextoEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // -> Oracle: Sequence... SQLServer: Autoincrement
    private Integer id;

    @Column(nullable = false, length = 10, unique = true)
    private String contexto;

    @Column(nullable = false, length = 100)                 // Lenguaje DECLARATIVO
    private String descripcion;

}
/*

    Spring-> Hibernate -> DDL


    // Oracle
    CREATE TABLE contextos (
        id INT PRIMARY KEY,
        contexto VARCHAR(10) NOT NULL UNIQUE,
        descripcion VARCHAR(100) NOT NULL
    );

    CREATE SEQUENCE contextos_seq START WITH 1 INCREMENT BY 1;


    Contextos
    contexto | descripcion
    ----------------------------------
    sm         | Sustantivo masculino
    sf.        | Sustantivo femenino
    adj.       | Adjetivo
    verbo      | Verbo
    adv.       | Adverbio

    Palabras
    palabra | contexto
    ---------------------
    casa    | sf.
    coche   | sm
    rápido  | adj.
    correr  | verbo
    comer   | verbo
    bailar  | verbo


*/