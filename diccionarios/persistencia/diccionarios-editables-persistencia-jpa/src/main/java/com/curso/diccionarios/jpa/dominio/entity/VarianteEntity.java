package com.curso.diccionarios.jpa.dominio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "variantes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"variante", "palabra_id"}) // Asegura que no haya duplicados de variante en la misma palabra
})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VarianteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 25)
    private String variante;

    @ManyToOne
    @JoinColumn(name = "palabra_id", nullable = false)
    private PalabraEntity palabra;

    @ManyToOne()
    @JoinColumn(name = "tipo_morfologico_id")
    private TipoMorfologicoEntity tipoMorfologico;

}
