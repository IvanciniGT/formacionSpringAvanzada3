package com.curso.diccionarios.jpa.dominio.jparepository;

import com.curso.diccionarios.jpa.dominio.entity.PalabraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PalabraJpaRepository extends JpaRepository<PalabraEntity, Integer> {

    Optional<PalabraEntity> findByPalabraAndDiccionarioNombre(String palabra, String diccionarioNombre);

    Optional<PalabraEntity> findByPalabra(String palabra);

}
