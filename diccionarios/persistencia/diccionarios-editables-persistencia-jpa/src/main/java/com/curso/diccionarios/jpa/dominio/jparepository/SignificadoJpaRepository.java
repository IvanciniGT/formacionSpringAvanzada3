package com.curso.diccionarios.jpa.dominio.jparepository;


import com.curso.diccionarios.jpa.dominio.entity.SignificadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SignificadoJpaRepository extends JpaRepository<SignificadoEntity, Integer> {
    Optional<SignificadoEntity> findByDefinicionAndPalabraId(String definicion, Integer palabraId);

    Optional<SignificadoEntity> findByPublicId(String publicId);

    void deleteByPublicIdAndPalabraId(String publicId, Integer id);

    List<SignificadoEntity> findByPalabraId(Integer id);

    Optional<SignificadoEntity> findByPublicIdAndPalabraId(String publicId, Integer id);
}
