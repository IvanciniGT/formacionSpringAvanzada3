package com.curso.diccionarios.jpa.dominio.repository.testdoubles;

import com.curso.diccionarios.dominio.model.ContextoEditable;
import com.curso.diccionarios.jpa.dominio.entity.ContextoEntity;
import com.curso.diccionarios.jpa.dominio.mappers.ContextoMapper;

public class ContextoMapperDeMentirijilla implements ContextoMapper{
    @Override
    public ContextoEditable entityToModel(ContextoEntity contextoEntity) {
        return ContextoEditable.builder()
                .contexto(contextoEntity.getContexto())
                .descripcion(contextoEntity.getDescripcion())
                .build();
    }

    @Override
    public ContextoEntity modelToEntity(ContextoEditable contextoEntity) {
        return null;
    }
}
