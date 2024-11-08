package com.curso.diccionarios.jpa.dominio.jparepository;

import com.curso.diccionarios.jpa.dominio.entity.TipoMorfologicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoMorfologicoJpaRepository extends JpaRepository<TipoMorfologicoEntity, Integer> {

    // Méto do para buscar un tipo morfológico por su tipo
    Optional<TipoMorfologicoEntity> findByTipo(String tipo);

}