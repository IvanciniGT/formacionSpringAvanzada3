package com.curso.diccionarios.dominio.exception;

public class NonExistentEntityException extends Exception {
    public NonExistentEntityException(String contextoNoExistente) {
        super("No existe un registro: " + contextoNoExistente);
    }
}
