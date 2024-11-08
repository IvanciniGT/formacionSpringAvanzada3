package com.curso.diccionarios.jpa.dominio.mappers;

import com.curso.diccionarios.dominio.model.IdiomaEditable;
import com.curso.diccionarios.jpa.dominio.entity.IdiomaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IdiomaMapper {
    // Convierte de entidad a modelo de dominio
    IdiomaEditable entityToModel(IdiomaEntity entity);

    // Convierte de modelo de dominio a entidad
    IdiomaEntity modelToEntity(IdiomaEditable model);
}
