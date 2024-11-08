package com.curso.diccionarios.jpa.dominio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(
        name = "palabras",
        uniqueConstraints = @UniqueConstraint(columnNames = {"palabra", "diccionario_id"})
)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PalabraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20) // ESTA BIEN ESTO?
    private String palabra;

    @ManyToOne()
    @JoinColumn(name = "diccionario_id", nullable = false)
    private DiccionarioEntity diccionario;

    @OneToMany(mappedBy = "palabra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VarianteEntity> variantes;

    @OneToMany(mappedBy = "palabra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SignificadoEntity> significados;

}
/*
DDL
    CREATE TABLE palabras (
        id INT PRIMARY KEY,
        palabra VARCHAR(20) NOT NULL,
        diccionario_id INT NOT NULL,
        FOREIGN KEY (diccionario_id) REFERENCES diccionarios(id)
    );

    CREATE UNIQUE INDEX idx_palabra_diccionario ON palabras(palabra, diccionario_id);
 */