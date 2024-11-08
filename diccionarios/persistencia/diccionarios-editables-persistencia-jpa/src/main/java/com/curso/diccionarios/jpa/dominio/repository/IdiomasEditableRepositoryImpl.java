package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.IdiomaEditable;
import com.curso.diccionarios.dominio.repository.IdiomasEditableRepository;
import com.curso.diccionarios.jpa.dominio.entity.IdiomaEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.IdiomaJpaRepository;
import com.curso.diccionarios.jpa.dominio.mappers.IdiomaMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IdiomasEditableRepositoryImpl implements IdiomasEditableRepository {

    private final IdiomaJpaRepository idiomaJpaRepository;
    private final IdiomaMapper idiomaMapper;

    @Override
    public Optional<IdiomaEditable> getIdioma(@NonNull String idioma) {
        return idiomaJpaRepository.findByCodigo(idioma)
                .map(idiomaMapper::entityToModel);
    }

    @Override
    public List<IdiomaEditable> getIdiomas() {
        return idiomaJpaRepository.findAll().stream()
                .map(idiomaMapper::entityToModel)
                .toList();
    }

    @Override
    public void updateIdioma(@NonNull IdiomaEditable idiomaEditable) throws InvalidArgumentException, NonExistentEntityException {
        Optional<IdiomaEntity> existingIdioma = idiomaJpaRepository.findByCodigo(idiomaEditable.getCodigo());

        if (existingIdioma.isEmpty()) {
            throw new NonExistentEntityException("El idioma no existe");
        }

        existingIdioma.ifPresent(entity -> {
            entity.setNombre(idiomaEditable.getNombre());
            idiomaJpaRepository.save(entity); // Guardamos los cambios en la base de datos
        });
    }

    @Override
    public IdiomaEditable newIdioma(@NonNull String idioma) throws InvalidArgumentException, AlreadyExistsEntityException {
        if (idiomaJpaRepository.findByCodigo(idioma).isPresent()) {
            throw new AlreadyExistsEntityException("El idioma ya existe");
        }

        IdiomaEntity newIdioma = new IdiomaEntity();
        newIdioma.setCodigo(idioma);
        newIdioma.setNombre("default_icono"); // Icono por defecto

        return idiomaMapper.entityToModel(idiomaJpaRepository.save(newIdioma));
    }

    @Override
    public Optional<IdiomaEditable> deleteIdioma(@NonNull String idioma) {
        Optional<IdiomaEntity> existingIdioma = idiomaJpaRepository.findByCodigo(idioma);

        existingIdioma.ifPresent(idiomaJpaRepository::delete); // Eliminamos por nombre

        return existingIdioma.map(idiomaMapper::entityToModel);
    }
}
