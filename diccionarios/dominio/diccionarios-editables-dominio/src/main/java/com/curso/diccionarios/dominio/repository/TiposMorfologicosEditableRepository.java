package com.curso.diccionarios.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.TipoMorfologicoEditable;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface TiposMorfologicosEditableRepository {

    Optional<TipoMorfologicoEditable> getTipoMorfologico(@NonNull String tipoMorfologico);
    List<TipoMorfologicoEditable> getTiposMorfologicos();
    void updateTipoMorfologico(@NonNull TipoMorfologicoEditable tipoMorfologico) throws InvalidArgumentException, NonExistentEntityException;
    TipoMorfologicoEditable newTipoMorfologico(@NonNull String tipoMorfologico, @NonNull String descripcion) throws AlreadyExistsEntityException, InvalidArgumentException;
    Optional<TipoMorfologicoEditable> deleteTipoMorfologico(@NonNull String tipoMorfologico);

}
