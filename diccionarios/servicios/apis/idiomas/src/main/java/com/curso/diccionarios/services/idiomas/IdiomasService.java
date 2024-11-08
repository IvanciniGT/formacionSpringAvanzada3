package com.curso.diccionarios.services.idiomas;

import com.curso.diccionarios.services.idiomas.dtos.IdiomaDTO;

import java.util.List;
import java.util.Optional;

public interface IdiomasService {
    Optional<IdiomaDTO> recuperarIdiomaPorCodigo(String codigoIdioma);

    List<IdiomaDTO> recuperarTodosLosIdiomas();

    IdiomaDTO nuevoIdioma(IdiomaDTO nuevoIdiomaACrear);
}
