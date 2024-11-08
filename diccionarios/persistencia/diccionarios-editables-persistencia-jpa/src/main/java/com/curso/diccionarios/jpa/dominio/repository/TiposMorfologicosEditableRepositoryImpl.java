package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.TipoMorfologicoEditable;
import com.curso.diccionarios.dominio.repository.TiposMorfologicosEditableRepository;
import com.curso.diccionarios.jpa.dominio.entity.TipoMorfologicoEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.TipoMorfologicoJpaRepository;
import com.curso.diccionarios.jpa.dominio.mappers.TipoMorfologicoMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TiposMorfologicosEditableRepositoryImpl implements TiposMorfologicosEditableRepository {

    private final TipoMorfologicoJpaRepository tipoMorfologicoJpaRepository;
    private final TipoMorfologicoMapper tipoMorfologicoMapper;

    @Override
    public Optional<TipoMorfologicoEditable> getTipoMorfologico(@NonNull String tipo) {
        return tipoMorfologicoJpaRepository.findByTipo(tipo)
                .map(tipoMorfologicoMapper::entityToModel);
    }

    @Override
    public List<TipoMorfologicoEditable> getTiposMorfologicos() {
        return tipoMorfologicoJpaRepository.findAll().stream()
                .map(tipoMorfologicoMapper::entityToModel)
                .toList();
    }

    @Override
    public void updateTipoMorfologico(@NonNull TipoMorfologicoEditable tipoMorfologicoEditable) throws InvalidArgumentException, NonExistentEntityException {
        Optional<TipoMorfologicoEntity> existingTipo = tipoMorfologicoJpaRepository.findByTipo(tipoMorfologicoEditable.getTipo());

        if (existingTipo.isEmpty()) {
            throw new NonExistentEntityException("El tipo morfológico no existe");
        }

        existingTipo.ifPresent(entity -> {
            entity.setDescripcion(tipoMorfologicoEditable.getDescripcion());
            tipoMorfologicoJpaRepository.save(entity); // Guardamos los cambios en la base de datos
        });
    }

    @Override
    public TipoMorfologicoEditable newTipoMorfologico(@NonNull String tipo, @NonNull String descripcion) throws InvalidArgumentException, AlreadyExistsEntityException {
        if (tipoMorfologicoJpaRepository.findByTipo(tipo).isPresent()) {
            throw new AlreadyExistsEntityException("El tipo morfológico ya existe");
        }

        TipoMorfologicoEntity newTipo = new TipoMorfologicoEntity();
        newTipo.setTipo(tipo);
        newTipo.setDescripcion(descripcion);

        return tipoMorfologicoMapper.entityToModel(tipoMorfologicoJpaRepository.save(newTipo));
    }

    @Override
    public Optional<TipoMorfologicoEditable> deleteTipoMorfologico(@NonNull String tipo) {
        Optional<TipoMorfologicoEntity> existingTipo = tipoMorfologicoJpaRepository.findByTipo(tipo);

        existingTipo.ifPresent(tipoMorfologicoJpaRepository::delete); // Eliminamos por tipo

        return existingTipo.map(tipoMorfologicoMapper::entityToModel);
    }
}
