package com.curso.diccionarios.jpa.dominio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "idiomas")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IdiomaEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 10, unique = true)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "idioma")
    private List<DiccionarioEntity> diccionarios;

}

/*

    Idioma                 -<       Diccionario
    id | idioma  | icono            id | nombre    | idioma
    1 | Español | 🇪🇸                1  | Oxford    |  2
    2 | Inglés  | 🇬🇧                2  | Cambridge |  2
    3 | Francés | 🇫🇷

    DDL
    CREATE TABLE idiomas (
        id INT PRIMARY KEY,
        idioma VARCHAR(10) NOT NULL UNIQUE,
        icono VARCHAR(100) NOT NULL
    );


*/
