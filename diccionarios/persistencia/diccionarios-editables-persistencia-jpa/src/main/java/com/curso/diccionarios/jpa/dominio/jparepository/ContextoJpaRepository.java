package com.curso.diccionarios.jpa.dominio.jparepository;

import com.curso.diccionarios.jpa.dominio.entity.ContextoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// JpaRepository: Interfaz de Spring Data JPA que proporciona métodos CRUD para la entidad ContextoEntity.
// save(S), saveAll(Iterable<S>), findById(ID), exists(ID), findAll(), findAllById(Iterable<ID>), count(), deleteById(ID), delete(S), deleteAll(Iterable<? extends S>), deleteAll()

// Spring, en automático, al arrancar, si lee esta clase y lee las clases de las Entidades:
// - Me creará las tablas en la base de datos (y secuencias, indices, etc.)
// - Me creará una clase que implementa esta interfaz (Spring-hibernate- rellena con la lógica de acceso a la base de datos de turno)
// - Además, si alguien pide un  ContextoJpaRepository (Solicita una inyección de dependencias) se le entregará
//   una instancia de esa clase que ha creado Spring
// Mucha magia

// Adicionalmente Spring me permite definir funciones Adicionales en esta interfaz...
// Y también las implementará con toda la lógica de acceso a la base de datos.
// Es necesario seguir cierta nomenclatura para que Spring las detecte y las implemente
public interface ContextoJpaRepository extends JpaRepository<ContextoEntity, Integer> {

    Optional<ContextoEntity> findByContexto(String contexto);

}