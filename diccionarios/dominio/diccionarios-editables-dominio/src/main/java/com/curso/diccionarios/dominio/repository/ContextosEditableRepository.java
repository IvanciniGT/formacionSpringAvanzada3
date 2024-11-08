package com.curso.diccionarios.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.ContextoEditable;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface ContextosEditableRepository {

    Optional<ContextoEditable> getContexto(@NonNull String contexto);
    List<ContextoEditable> getContextos();
    void updateContexto(@NonNull ContextoEditable contexto) throws InvalidArgumentException, NonExistentEntityException;
    ContextoEditable newContexto(@NonNull String contexto, @NonNull String descripcion) throws InvalidArgumentException, AlreadyExistsEntityException;
                                // KISS: Keep It Simple, Stupid
    Optional<ContextoEditable> deleteContexto(@NonNull String contexto);

}
