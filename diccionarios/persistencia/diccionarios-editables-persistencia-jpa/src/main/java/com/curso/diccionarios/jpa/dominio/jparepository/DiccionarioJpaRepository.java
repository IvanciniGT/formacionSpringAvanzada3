package com.curso.diccionarios.jpa.dominio.jparepository;

import com.curso.diccionarios.jpa.dominio.entity.DiccionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiccionarioJpaRepository extends JpaRepository<DiccionarioEntity, Integer> {

    // Méto do para buscar un diccionario por su nombre
    Optional<DiccionarioEntity> findByNombre(String nombre);

}