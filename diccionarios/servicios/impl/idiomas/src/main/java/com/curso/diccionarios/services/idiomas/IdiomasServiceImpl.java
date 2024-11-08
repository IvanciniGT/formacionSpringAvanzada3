package com.curso.diccionarios.services.idiomas;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.IdiomaEditable;
import com.curso.diccionarios.dominio.repository.IdiomasEditableRepository;
import com.curso.diccionarios.services.idiomas.dtos.IdiomaDTO;
import com.curso.diccionarios.services.idiomas.mappers.IdiomasServicioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdiomasServiceImpl implements IdiomasService {

    private final IdiomasEditableRepository repositorio;
    private final IdiomasServicioMapper mapeador;

    @Override
    public Optional<IdiomaDTO> recuperarIdiomaPorCodigo(String codigoIdioma) {
        return repositorio.getIdioma(codigoIdioma).map(mapeador::idiomaEditable2IdiomaDTO);
    }

    @Override
    public List<IdiomaDTO> recuperarTodosLosIdiomas() {
        return repositorio.getIdiomas().stream().map(mapeador::idiomaEditable2IdiomaDTO).toList();
    }

    @Override
    public IdiomaDTO nuevoIdioma(IdiomaDTO nuevoIdiomaACrear) {
        Optional<IdiomaEditable> potencialIdiomaExistente = repositorio.getIdioma(nuevoIdiomaACrear.codigo());
        if(potencialIdiomaExistente.isPresent()) {
            return mapeador.idiomaEditable2IdiomaDTO(potencialIdiomaExistente.get());
        }
        try {
            IdiomaEditable idiomaCreado = repositorio.newIdioma(nuevoIdiomaACrear.codigo());
            idiomaCreado.setNombre(nuevoIdiomaACrear.nombre());
            repositorio.updateIdioma(idiomaCreado);
            return mapeador.idiomaEditable2IdiomaDTO(idiomaCreado);
        } catch (InvalidArgumentException e) {
            log.error("Error en los datos. Deberíamos lanzar una exception custom", e);
        } catch (AlreadyExistsEntityException e) {
            log.error("Error idioma duplicado. Deberíamos lanzar una exception custom", e);
        } catch (NonExistentEntityException e) {
            // Lo tengo que poner... pero de clave.. este error no puede producirse. O eso , o tengo un bug
            log.error("Error inesperado. Bug en el programa", e);
        }
        throw new RuntimeException();

    }
}
