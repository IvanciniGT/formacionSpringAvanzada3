package com.curso.diccionarios.services.idiomas.mappers;

import com.curso.diccionarios.dominio.model.IdiomaEditable;
import com.curso.diccionarios.services.idiomas.dtos.IdiomaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IdiomasServicioMapper {
    IdiomaDTO idiomaEditable2IdiomaDTO(IdiomaEditable idiomaEditable);
}
