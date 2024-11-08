package com.curso.diccionarios.dominio.repository;

import com.curso.diccionarios.dominio.model.Idioma;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface IdiomasRepository {

    Optional<Idioma> getIdioma(@NonNull String idioma);
    List<Idioma> getIdiomas();

}
