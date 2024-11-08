package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.ContextoEditable;
import com.curso.diccionarios.jpa.dominio.entity.ContextoEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.ContextoJpaRepository;
import com.curso.diccionarios.jpa.dominio.mappers.ContextoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContextosEditableRepositoryImplTest {

    @Mock
    private ContextoJpaRepository contextoJpaRepository;

    @Mock
    private ContextoMapper contextoMapper;

    @InjectMocks
    private ContextosEditableRepositoryImpl contextosEditableRepository;

    public ContextosEditableRepositoryImplTest() {
        // Inicializamos los mocks
        MockitoAnnotations.openMocks(this); // Mockito!!! Aplica esas anotaciones en esta instancia
    }

    // Test para obtener un contexto existente
    @Test
    public void testGetContexto() {
        // Dado: Se configura el entorno simulando una entidad existente y el mapeo a ContextoEditable
        String contexto = "contexto_prueba";
        ContextoEntity contextoEntity = new ContextoEntity();
        contextoEntity.setContexto(contexto);
        contextoEntity.setDescripcion("Descripción de prueba");

        ContextoEditable contextoEditable = ContextoEditable.builder()
                .contexto(contexto)
                .descripcion("Descripción de prueba")
                .build();

        // Se configura el mock para que devuelva la entidad simulada
        when(contextoJpaRepository.findByContexto(contexto)).thenReturn(Optional.of(contextoEntity));
        when(contextoMapper.entityToModel(contextoEntity)).thenReturn(contextoEditable);

        // Cuando: Se invoca el método para obtener el contexto
        Optional<ContextoEditable> resultado = contextosEditableRepository.getContexto(contexto);

        // Entonces: Se verifica que el contexto es retornado y tiene la descripción correcta
        assertTrue(resultado.isPresent());
        assertEquals("contexto_prueba", resultado.get().getContexto());
        assertEquals("Descripción de prueba", resultado.get().getDescripcion());
        verify(contextoJpaRepository, times(1)).findByContexto(eq(contexto));
        verify(contextoMapper, times(1)).entityToModel(contextoEntity);
    }

    // Test cuando el contexto no existe
    @Test
    public void testGetContextoNoExiste() {
        // Dado: Se configura el mock para devolver un Optional.empty() cuando el contexto no exista
        String contexto = "contexto_inexistente";
        when(contextoJpaRepository.findByContexto(contexto)).thenReturn(Optional.empty());

        // Cuando: Se invoca el método para obtener el contexto
        Optional<ContextoEditable> resultado = contextosEditableRepository.getContexto(contexto);

        // Entonces: Se verifica que el resultado está vacío
        assertFalse(resultado.isPresent());
        verify(contextoJpaRepository, times(1)).findByContexto(eq(contexto));
        verify(contextoMapper, times(0)).entityToModel(any()); // No se debe llamar al mapper
    }

    // Test para crear un nuevo contexto
    @Test
    public void testNewContexto() throws AlreadyExistsEntityException, InvalidArgumentException {
        // Dado: Configuramos los mocks para simular la creación de un nuevo contexto
        String contexto = "contexto_nuevo";
        String descripcion = "Nueva descripción";

        when(contextoJpaRepository.findByContexto(contexto)).thenReturn(Optional.empty());

        // Simulamos la entidad y el modelo editable
        ContextoEntity contextoEntity = new ContextoEntity();
        contextoEntity.setContexto(contexto);
        contextoEntity.setDescripcion(descripcion);

        ContextoEditable contextoEditable = ContextoEditable.builder()
                .contexto(contexto)
                .descripcion(descripcion)
                .build();

        // Configuramos el mapper y el guardado en el repositorio
        when(contextoMapper.modelToEntity(any(ContextoEditable.class))).thenReturn(contextoEntity);
        when(contextoMapper.entityToModel(contextoEntity)).thenReturn(contextoEditable);
        when(contextoJpaRepository.save(any(ContextoEntity.class))).thenReturn(contextoEntity);

        // Cuando: Se invoca el método para crear un nuevo contexto
        ContextoEditable resultado = contextosEditableRepository.newContexto(contexto, descripcion);

        // Entonces: Se verifica que el contexto se ha creado correctamente
        assertNotNull(resultado);
        assertEquals("Nueva descripción", resultado.getDescripcion());
        verify(contextoJpaRepository, times(1)).findByContexto(eq(contexto));
        verify(contextoJpaRepository, times(1)).save(any(ContextoEntity.class));
        verify(contextoMapper, times(1)).entityToModel(any(ContextoEntity.class));
    }

    // Test para actualizar un contexto existente
    @Test
    public void testUpdateContexto() throws InvalidArgumentException, NonExistentEntityException, AlreadyExistsEntityException {
        // Dado: Simulamos que el contexto ya existe en la base de datos
        String contexto = "contexto_existente";

        // Simulamos una entidad existente en la base de datos
        ContextoEntity contextoEntity = new ContextoEntity();
        contextoEntity.setContexto(contexto);
        contextoEntity.setDescripcion("Descripción antigua");

        // Creamos el modelo editable con la descripción antigua
        ContextoEditable contextoEditable = ContextoEditable.builder()
                .contexto(contexto)
                .descripcion("Descripción antigua")
                .build();

        // Configuramos los mocks para devolver la entidad existente y su mapeo
        when(contextoJpaRepository.findByContexto(contexto)).thenReturn(Optional.of(contextoEntity));
        when(contextoMapper.entityToModel(contextoEntity)).thenReturn(contextoEditable);

        // Se recupera el contexto y se modifica su descripción
        contextoEditable = contextosEditableRepository.getContexto(contexto).get();
        contextoEditable.setDescripcion("Descripción nueva");

        // Configuramos el mapper para convertir el modelo editable de vuelta a la entidad
        when(contextoMapper.modelToEntity(contextoEditable)).thenReturn(contextoEntity);

        // ArgumentCaptor para capturar el valor pasado a save()
        ArgumentCaptor<ContextoEntity> captor = ArgumentCaptor.forClass(ContextoEntity.class);

        // Cuando: Se invoca el método para actualizar el contexto
        contextosEditableRepository.updateContexto(contextoEditable);

        // Entonces: Se verifica que se ha realizado la búsqueda y la actualización
        verify(contextoJpaRepository, times(2)).findByContexto(eq(contexto)); // Verifica que se llama a findByContexto dos veces
        verify(contextoJpaRepository).save(captor.capture()); // Captura el argumento pasado a save()

        // Ahora verificamos que el contextoEntity capturado tiene la descripción actualizada
        ContextoEntity capturado = captor.getValue();
        assertEquals("Descripción nueva", capturado.getDescripcion()); // Verifica que la descripción se actualizó correctamente
    }
}
