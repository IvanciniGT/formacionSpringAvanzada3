package com.curso.diccionarios.jpa.dominio.jparepository;

import com.curso.diccionarios.jpa.dominio.entity.IdiomaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdiomaJpaRepository extends JpaRepository<IdiomaEntity, Integer> {

    // Mét odo para buscar un idioma por su nombre
    Optional<IdiomaEntity> findByIdioma(String idioma);

}