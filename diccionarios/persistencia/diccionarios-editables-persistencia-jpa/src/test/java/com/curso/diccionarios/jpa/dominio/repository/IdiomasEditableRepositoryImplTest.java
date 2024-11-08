package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.model.IdiomaEditable;
import com.curso.diccionarios.jpa.dominio.entity.IdiomaEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.IdiomaJpaRepository;
import com.curso.diccionarios.jpa.dominio.mappers.IdiomaMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdiomasEditableRepositoryImplTest {

    @Mock
    private IdiomaJpaRepository idiomaJpaRepository;

    @Mock
    private IdiomaMapper idiomaMapper;

    @InjectMocks
    private IdiomasEditableRepositoryImpl idiomasEditableRepository;

    public IdiomasEditableRepositoryImplTest() {
        // Inicializamos los mocks
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetIdioma() {
        // Dado: Simulamos una entidad existente en la base de datos
        String idioma = "Español";
        IdiomaEntity idiomaEntity = new IdiomaEntity();
        idiomaEntity.setIdioma(idioma);
        idiomaEntity.setIcono("🇪🇸");

        IdiomaEditable idiomaEditable = IdiomaEditable.builder()
                .idioma(idioma)
                .icono("🇪🇸")
                .build();

        // Configuramos el mock para que devuelva la entidad simulada
        when(idiomaJpaRepository.findByIdioma(idioma)).thenReturn(Optional.of(idiomaEntity));
        when(idiomaMapper.entityToModel(idiomaEntity)).thenReturn(idiomaEditable);

        // Cuando: Se invoca el método para obtener el idioma
        Optional<IdiomaEditable> resultado = idiomasEditableRepository.getIdioma(idioma);

        // Entonces: Se verifica que el idioma es retornado y tiene el icono correcto
        assertTrue(resultado.isPresent());
        assertEquals("Español", resultado.get().getIdioma());
        assertEquals("🇪🇸", resultado.get().getIcono());
        verify(idiomaJpaRepository, times(1)).findByIdioma(idioma);
        verify(idiomaMapper, times(1)).entityToModel(idiomaEntity);
    }
    @Test
    void testDeleteIdioma() {
        // Dado: Simulamos que el idioma ya existe en la base de datos
        String idioma = "Francés";

        IdiomaEntity idiomaEntity = new IdiomaEntity();
        idiomaEntity.setIdioma(idioma);
        idiomaEntity.setIcono("🇫🇷");

        IdiomaEditable idiomaEditable = IdiomaEditable.builder()
                .idioma(idioma)
                .icono("🇫🇷")
                .build();

        // Configuramos el mock para devolver la entidad existente
        when(idiomaJpaRepository.findByIdioma(idioma)).thenReturn(Optional.of(idiomaEntity));
        when(idiomaMapper.entityToModel(idiomaEntity)).thenReturn(idiomaEditable);

        // Cuando: Se invoca el mét odo para eliminar el idioma
        Optional<IdiomaEditable> resultado = idiomasEditableRepository.deleteIdioma(idioma);

        // Entonces: Se verifica que el idioma fue eliminado
        assertTrue(resultado.isPresent());
        verify(idiomaJpaRepository, times(1)).delete(idiomaEntity);
        verify(idiomaJpaRepository, times(1)).findByIdioma(idioma);
        verify(idiomaMapper, times(1)).entityToModel(idiomaEntity);
    }

}
