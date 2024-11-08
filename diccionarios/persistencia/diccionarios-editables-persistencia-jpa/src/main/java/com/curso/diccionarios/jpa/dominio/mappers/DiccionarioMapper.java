package com.curso.diccionarios.jpa.dominio.mappers;

import com.curso.diccionarios.dominio.model.Diccionario;
import com.curso.diccionarios.dominio.model.Idioma;
import com.curso.diccionarios.jpa.dominio.entity.DiccionarioEntity;
import com.curso.diccionarios.jpa.dominio.entity.IdiomaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiccionarioMapper {

    Diccionario entityToModel(DiccionarioEntity entity);

    DiccionarioEntity modelToEntity(Diccionario model);

    IdiomaEntity idiomaModelToEntity(Idioma idioma);
}
