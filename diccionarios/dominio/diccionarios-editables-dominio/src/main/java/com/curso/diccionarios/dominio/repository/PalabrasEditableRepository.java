package com.curso.diccionarios.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.*;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface PalabrasEditableRepository {

    Optional<PalabraEditable> getPalabra(@NonNull Diccionario diccionario, @NonNull String palabra);
    List<PalabraEditable> getPalabras();
    void updatePalabra(@NonNull PalabraEditable palabra) throws InvalidArgumentException, NonExistentEntityException;
    PalabraEditable newPalabra(@NonNull Diccionario diccionario, @NonNull String palabra) throws InvalidArgumentException, AlreadyExistsEntityException;
    Optional<PalabraEditable> deletePalabra(@NonNull String palabra);

    void addVariante(@NonNull PalabraEditable palabra, @NonNull String variante, @NonNull TipoMorfologico contexto) throws InvalidArgumentException, NonExistentEntityException, AlreadyExistsEntityException;
    Optional<Variante> removeVariante(@NonNull PalabraEditable palabra, @NonNull String variante);

    SignificadoEditable addSignificado(@NonNull PalabraEditable palabra, @NonNull String definicion) throws InvalidArgumentException, NonExistentEntityException, AlreadyExistsEntityException;
    void updateSignificado(@NonNull SignificadoEditable significado) throws InvalidArgumentException, NonExistentEntityException;
    Optional<SignificadoEditable> removeSignificado(@NonNull String publicIdSignificado) ;
    List<SignificadoEditable> rearrangeSignificados(@NonNull PalabraEditable palabra, @NonNull List<SignificadoEditable> significados) throws NonExistentEntityException, InvalidArgumentException;

    void addEjemplo(@NonNull SignificadoEditable significado, @NonNull String ejemplo) throws NonExistentEntityException;
    Optional<String> removeEjemplo(@NonNull SignificadoEditable significado, @NonNull String ejemplo) throws NonExistentEntityException;

    void addSinonimo(@NonNull SignificadoEditable significado,@NonNull SignificadoEditable sinonimo) throws NonExistentEntityException;
    Optional<SignificadoEditable> removeSinonimo(@NonNull SignificadoEditable significado,@NonNull SignificadoEditable sinonimo) throws NonExistentEntityException;

    void addContexto(@NonNull SignificadoEditable significado, @NonNull String contexto) throws NonExistentEntityException;
    Optional<Contexto> removeContexto(@NonNull SignificadoEditable significado, @NonNull String contexto) throws NonExistentEntityException;

    void addTipoMorfologico(@NonNull SignificadoEditable significado, @NonNull String tipoMorfologico) throws NonExistentEntityException;
    Optional<TipoMorfologico> removeTipoMorfologico(@NonNull SignificadoEditable significado, @NonNull String tipoMorfologico) throws NonExistentEntityException;

}

// KISS: Keep It Simple, Stupid ~ YAGNI: You Aren't Gonna Need It
// Queremos poder añadir un diccionario sin palabras? TO DO EL SENTIDO
// Puedo querer ir registrando Contextos de uso... para luego poder irlos aplicando a palabras? SI
// Puedo querer ir registrando Tipos Morfológicos... para luego poder irlos aplicando a palabras? SI
// Puedo querer cambiar el icono de un idioma? SI
// Voy a asignar significados fuera del contexto de una palabra? NO
