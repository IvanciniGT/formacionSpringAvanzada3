package com.curso.diccionarios.jpa.dominio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "diccionarios")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DiccionarioEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50, unique = true)
    private String nombre;

    @ManyToOne()
    @JoinColumn(name = "idioma")
    private IdiomaEntity idioma;

    @OneToMany(mappedBy = "diccionario")
    private List<PalabraEntity> palabras;

}
/*

    Idioma                 -<       Diccionario
    id | idioma  | icono            id | nombre    | idioma
    1 | Español | 🇪🇸                1  | Oxford    |  2
    2 | Inglés  | 🇬🇧                2  | Cambridge |  2
    3 | Francés | 🇫🇷

    DDL
    CREATE TABLE diccionarios (
        id INT PRIMARY KEY,
        nombre VARCHAR(10) NOT NULL UNIQUE,
        idioma INT NOT NULL,
        FOREIGN KEY (idioma) REFERENCES idiomas(id)
    );

*/
