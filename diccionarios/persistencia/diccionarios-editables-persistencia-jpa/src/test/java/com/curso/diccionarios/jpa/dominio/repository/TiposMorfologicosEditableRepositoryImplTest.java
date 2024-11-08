package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.model.TipoMorfologicoEditable;
import com.curso.diccionarios.jpa.dominio.entity.TipoMorfologicoEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.TipoMorfologicoJpaRepository;
import com.curso.diccionarios.jpa.dominio.mappers.TipoMorfologicoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TiposMorfologicosEditableRepositoryImplTest {

    @Mock
    private TipoMorfologicoJpaRepository tipoMorfologicoJpaRepository;

    @Mock
    private TipoMorfologicoMapper tipoMorfologicoMapper;

    @InjectMocks
    private TiposMorfologicosEditableRepositoryImpl tiposMorfologicosEditableRepository;

    public TiposMorfologicosEditableRepositoryImplTest() {
        // Inicializamos los mocks
        MockitoAnnotations.openMocks(this); // Mockito!!! Aplica esas anotaciones en esta instancia
    }
    @Test
    void testGetTipoMorfologico() {
        // Dado: Simulamos una entidad existente en la base de datos
        String tipo = "sm";
        TipoMorfologicoEntity tipoEntity = new TipoMorfologicoEntity();
        tipoEntity.setTipo(tipo);
        tipoEntity.setDescripcion("Sustantivo Masculino");

        TipoMorfologicoEditable tipoEditable = TipoMorfologicoEditable.builder()
                .tipo(tipo)
                .descripcion("Sustantivo Masculino")
                .build();

        // Configuramos el mock para que devuelva la entidad simulada
        when(tipoMorfologicoJpaRepository.findByTipo(tipo)).thenReturn(Optional.of(tipoEntity));
        when(tipoMorfologicoMapper.entityToModel(tipoEntity)).thenReturn(tipoEditable);

        // Cuando: Se invoca el método para obtener el tipo morfológico
        Optional<TipoMorfologicoEditable> resultado = tiposMorfologicosEditableRepository.getTipoMorfologico(tipo);

        // Entonces: Se verifica que el tipo morfológico es retornado y tiene la descripción correcta
        assertTrue(resultado.isPresent());
        assertEquals("sm", resultado.get().getTipo());
        assertEquals("Sustantivo Masculino", resultado.get().getDescripcion());
        verify(tipoMorfologicoJpaRepository, times(1)).findByTipo(tipo);
        verify(tipoMorfologicoMapper, times(1)).entityToModel(tipoEntity);
    }
    @Test
    void testDeleteTipoMorfologico() {
        // Dado: Simulamos que el tipo morfológico ya existe en la base de datos
        String tipo = "sm";

        TipoMorfologicoEntity tipoEntity = new TipoMorfologicoEntity();
        tipoEntity.setTipo(tipo);
        tipoEntity.setDescripcion("Sustantivo Masculino");

        TipoMorfologicoEditable tipoEditable = TipoMorfologicoEditable.builder()
                .tipo(tipo)
                .descripcion("Sustantivo Masculino")
                .build();
        // Configuramos el mock para devolver la entidad existente
        when(tipoMorfologicoJpaRepository.findByTipo(tipo)).thenReturn(Optional.of(tipoEntity));
        when(tipoMorfologicoMapper.entityToModel(tipoEntity)).thenReturn(tipoEditable);

        // Cuando: Se invoca el método para eliminar el tipo morfológico
        Optional<TipoMorfologicoEditable> resultado = tiposMorfologicosEditableRepository.deleteTipoMorfologico(tipo);

        // Entonces: Se verifica que el tipo morfológico fue eliminado
        assertTrue(resultado.isPresent());
        verify(tipoMorfologicoJpaRepository, times(1)).delete(tipoEntity);
        verify(tipoMorfologicoJpaRepository, times(1)).findByTipo(tipo);
        verify(tipoMorfologicoMapper, times(1)).entityToModel(tipoEntity);
    }

}
