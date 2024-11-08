package com.curso.diccionarios.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.IdiomaEditable;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface IdiomasEditableRepository {

    Optional<IdiomaEditable> getIdioma(@NonNull String idioma);
    List<IdiomaEditable> getIdiomas();
    void updateIdioma(@NonNull IdiomaEditable idioma) throws InvalidArgumentException, NonExistentEntityException;
    IdiomaEditable newIdioma(@NonNull String idioma) throws InvalidArgumentException, AlreadyExistsEntityException;
    Optional<IdiomaEditable> deleteIdioma(@NonNull String idioma);

}
