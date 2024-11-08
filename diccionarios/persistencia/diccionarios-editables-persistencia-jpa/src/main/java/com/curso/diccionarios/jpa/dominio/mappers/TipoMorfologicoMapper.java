package com.curso.diccionarios.jpa.dominio.mappers;

import com.curso.diccionarios.dominio.model.TipoMorfologicoEditable;
import com.curso.diccionarios.jpa.dominio.entity.TipoMorfologicoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoMorfologicoMapper {

    TipoMorfologicoEditable entityToModel(TipoMorfologicoEntity entity);

    TipoMorfologicoEntity modelToEntity(TipoMorfologicoEditable model);
}
