package com.curso.diccionarios.dominio.exception;

public class AlreadyExistsEntityException extends Exception {
    public AlreadyExistsEntityException(String contextoYaExistente) {
        super("Ya existe un registro con el mismo contexto: " + contextoYaExistente);
    }
}
