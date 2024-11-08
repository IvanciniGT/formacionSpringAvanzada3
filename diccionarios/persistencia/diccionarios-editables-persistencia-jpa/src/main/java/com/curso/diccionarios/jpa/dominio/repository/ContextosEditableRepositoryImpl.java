package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.ContextoEditable;
import com.curso.diccionarios.dominio.repository.ContextosEditableRepository;
import com.curso.diccionarios.jpa.dominio.entity.ContextoEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.ContextoJpaRepository;
import com.curso.diccionarios.jpa.dominio.mappers.ContextoMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository     // Es un componente (que le explico al desarrollador que es un componente de tipo repositorio... tiene logica de persistencia)
                // Spring al arrancar, va a crear una instancia de esta clase... Y A cualquiera que pida un
                // ContextosEditableRepository, le entregará esa instancia

    // Spring -> ContextosEditableRepositoryImpl -> ContextoJpaRepository -> BBDD
    //                                           -> ContextoMapper

@RequiredArgsConstructor
public class ContextosEditableRepositoryImpl implements ContextosEditableRepository {

    private final ContextoJpaRepository contextoJpaRepository; // Solicito la inyección de dependencias de un ContextoJpaRepository
    private final ContextoMapper mapeador;

    public Optional<ContextoEditable> getContexto(@NonNull String contexto){
        /*
        Optional<ContextoEntity> contextoEntity = contextoJpaRepository.findByContexto(contexto);
        if(contextoEntity.isEmpty()){
            return Optional.empty();
        }else {
            ContextoEntity contextoEntity1 = contextoEntity.get();
            ContextoEditable contextoEditable = mapeador.entityToModel(contextoEntity1);
            return Optional.of(contextoEditable);
        }
        */
        return contextoJpaRepository.findByContexto(contexto).map(mapeador::entityToModel);

    } // Optional<Entidad> -> Optional<Modelo>

    public List<ContextoEditable> getContextos(){
        return contextoJpaRepository.findAll().stream().map(mapeador::entityToModel).toList();
    }

    public void updateContexto(@NonNull ContextoEditable contexto) throws InvalidArgumentException, NonExistentEntityException{
        Optional<ContextoEntity> existintingContexto = contextoJpaRepository.findByContexto(contexto.getContexto());
        if(existintingContexto.isEmpty()){
            throw new NonExistentEntityException("Contexto no existente");
        }
        existintingContexto.ifPresent(c -> {
            c.setDescripcion(contexto.getDescripcion());
            contextoJpaRepository.save(c);
        });
    }

    public ContextoEditable newContexto(@NonNull String contexto, @NonNull String descripcion) throws InvalidArgumentException, AlreadyExistsEntityException{
        if(contextoJpaRepository.findByContexto(contexto).isPresent()){
            throw new AlreadyExistsEntityException("Contexto ya existente");
        }
        ContextoEntity contextoEntity = new ContextoEntity();
        contextoEntity.setContexto(contexto);
        contextoEntity.setDescripcion(descripcion);
        return mapeador.entityToModel(contextoJpaRepository.save(contextoEntity));

    }
    public Optional<ContextoEditable> deleteContexto(@NonNull String contexto){
        Optional<ContextoEntity> existintingContexto = contextoJpaRepository.findByContexto(contexto);
        existintingContexto.ifPresent(contextoJpaRepository::delete);
        return existintingContexto.map(mapeador::entityToModel);
    }

}
