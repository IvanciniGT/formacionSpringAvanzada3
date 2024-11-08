package com.curso.diccionarios.jpa.dominio.jparepository;

import com.curso.diccionarios.jpa.dominio.entity.VarianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VarianteJpaRepository extends JpaRepository<VarianteEntity, Integer> {
    Optional<VarianteEntity> findByVarianteAndPalabraId(String variante, Integer palabraId);

    void deleteByVarianteAndPalabraId(String variante, Integer palabraId);

}