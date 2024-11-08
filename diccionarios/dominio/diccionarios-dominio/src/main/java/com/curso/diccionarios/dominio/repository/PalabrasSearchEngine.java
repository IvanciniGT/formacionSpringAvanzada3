package com.curso.diccionarios.dominio.repository;

import com.curso.diccionarios.dominio.model.Diccionario;
import com.curso.diccionarios.dominio.model.Idioma;
import com.curso.diccionarios.dominio.model.Palabra;
import lombok.NonNull;

import java.util.List;

public interface PalabrasSearchEngine {

    List<Palabra> getPalabra(@NonNull Idioma idioma, @NonNull String palabra);
    List<Palabra> getPalabrasQueEmpiecenPor(@NonNull Diccionario diccionario, @NonNull String palabra);
    List<Palabra> getPalabrasSimilaresA(@NonNull Diccionario diccionario, @NonNull String palabra);

}
