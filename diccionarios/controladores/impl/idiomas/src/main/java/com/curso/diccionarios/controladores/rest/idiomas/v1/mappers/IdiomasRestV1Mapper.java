package com.curso.diccionarios.controladores.rest.idiomas.v1.mappers;

import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaRestV1DTO;
import com.curso.diccionarios.services.idiomas.dtos.IdiomaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
// Crea una clase que implemente esta interfaz y añadela al contexto de Spring
// De forma que si alguien pide un mapper de este tipo, Spring le dará la clase que MapStruct va a crear al compilar este codigo
public interface IdiomasRestV1Mapper {
    IdiomaRestV1DTO idiomaDTO2idiomaRestV1DTO(IdiomaDTO idiomaDevuelto);

    IdiomaDTO idiomaRestV1DTO2idiomaDTO(IdiomaRestV1DTO datosIdioma);
}
