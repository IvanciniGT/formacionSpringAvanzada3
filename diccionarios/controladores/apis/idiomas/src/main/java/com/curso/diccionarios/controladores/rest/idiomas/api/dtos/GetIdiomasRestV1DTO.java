package com.curso.diccionarios.controladores.rest.idiomas.api.dtos;

// JAVA 15 preview... Java 17 ya lo tiene
public sealed interface GetIdiomasRestV1DTO permits IdiomasRestV1DTO, ErrorRestV1DTO {}
