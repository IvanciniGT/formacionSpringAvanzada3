package com.curso.diccionarios.jpa.dominio.repository.testdoubles;

import com.curso.diccionarios.jpa.dominio.entity.ContextoEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.ContextoJpaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// MOCK. El mock cambia del SPy en el uso. Los mock los preconfiguramos... y ya ellos se encargan de validar la llamada
// Los SPY solo capturan datos.. pero no validan ellos.. lo tengo que validar yo en la prueba.
public class ContextoJpaRepositoryDeMentijilla implements ContextoJpaRepository {

    String contextoConElQueTeHanLlamado = null;
    String contextoConElQueDeberianHaberLlamado = null;

//    public boolean teHanLlamadoALaFuncionFindByContext(){
//        return contextoConElQueTeHanLlamado != null;
//    }
//    public String getContextoConElQueTeHanLlamado(){
//        return contextoConElQueTeHanLlamado;
//    }

    public void teVanALlamarConElContexto(String contexto){
        contextoConElQueDeberianHaberLlamado = contexto;
    }

    public boolean teHanLlamado(){
        return contextoConElQueTeHanLlamado != null;
    }

    @Override
    public Optional<ContextoEntity> findByContexto(String contexto) {
        if(contextoConElQueDeberianHaberLlamado == null){
            throw new RuntimeException("No se ha llamado a la función teVanALlamarConElContexto");
        }
        if(!contextoConElQueDeberianHaberLlamado.equals(contexto)){
            throw new RuntimeException("No se ha llamado a la función teVanALlamarConElContexto con el contexto correcto");
        }
        contextoConElQueTeHanLlamado = contexto;
        ContextoEntity contextoEntity = new ContextoEntity();
        contextoEntity.setContexto(contexto);
        contextoEntity.setDescripcion("Esta es la descripcion del contexto");
        contextoEntity.setId(33);
        return Optional.of(contextoEntity);
    } // 4 hierros mal soldaos! Tengo garantia de que este código funcionará?

    @Override
    public void flush() {

    }

    @Override
    public <S extends ContextoEntity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends ContextoEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<ContextoEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ContextoEntity getOne(Integer integer) {
        return null;
    }

    @Override
    public ContextoEntity getById(Integer integer) {
        return null;
    }

    @Override
    public ContextoEntity getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends ContextoEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ContextoEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends ContextoEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends ContextoEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ContextoEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ContextoEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ContextoEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends ContextoEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ContextoEntity> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<ContextoEntity> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<ContextoEntity> findAll() {
        return List.of();
    }

    @Override
    public List<ContextoEntity> findAllById(Iterable<Integer> integers) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(ContextoEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends ContextoEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ContextoEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<ContextoEntity> findAll(Pageable pageable) {
        return null;
    }
}
