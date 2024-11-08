package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.model.Diccionario;
import com.curso.diccionarios.dominio.model.Idioma;
import com.curso.diccionarios.dominio.repository.DiccionariosEditableRepository;
import com.curso.diccionarios.jpa.dominio.entity.DiccionarioEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.DiccionarioJpaRepository;
import com.curso.diccionarios.jpa.dominio.mappers.DiccionarioMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DiccionariosEditableRepositoryImpl implements DiccionariosEditableRepository {

    private final DiccionarioJpaRepository diccionarioJpaRepository;
    private final DiccionarioMapper diccionarioMapper;

    @Override
    public Optional<Diccionario> getDiccionario(@NonNull Idioma idioma, @NonNull String nombre) {
        return diccionarioJpaRepository.findByNombre(nombre)
                .filter(diccionario -> diccionario.getIdioma().getCodigo().equals(idioma.getCodigo()))
                .map(diccionarioMapper::entityToModel);
    }

    @Override
    public List<Diccionario> getDiccionario(@NonNull Idioma idioma) {
        return diccionarioJpaRepository.findAll().stream()
                .filter(diccionario -> diccionario.getIdioma().getCodigo().equals(idioma.getCodigo()))
                .map(diccionarioMapper::entityToModel)
                .toList();
    }

    @Override
    public Diccionario newDiccionario(@NonNull String nombre, @NonNull Idioma idioma) throws InvalidArgumentException, AlreadyExistsEntityException {
        if (diccionarioJpaRepository.findByNombre(nombre).isPresent()) {
            throw new AlreadyExistsEntityException("El diccionario ya existe");
        }

        DiccionarioEntity diccionarioEntity = new DiccionarioEntity();
        diccionarioEntity.setNombre(nombre);
        diccionarioEntity.setIdioma(diccionarioMapper.idiomaModelToEntity(idioma));

        return diccionarioMapper.entityToModel(diccionarioJpaRepository.save(diccionarioEntity));
    }

    @Override
    public Optional<Diccionario> deleteDiccionario(@NonNull String nombre) {
        Optional<DiccionarioEntity> existingDiccionario = diccionarioJpaRepository.findByNombre(nombre);

        existingDiccionario.ifPresent(diccionarioJpaRepository::delete);

        return existingDiccionario.map(diccionarioMapper::entityToModel);
    }
}
