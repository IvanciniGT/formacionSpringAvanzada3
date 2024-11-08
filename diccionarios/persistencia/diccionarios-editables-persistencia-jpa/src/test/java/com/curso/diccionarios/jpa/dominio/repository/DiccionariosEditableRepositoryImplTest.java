package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.model.Diccionario;
import com.curso.diccionarios.dominio.model.Idioma;
import com.curso.diccionarios.jpa.dominio.entity.DiccionarioEntity;
import com.curso.diccionarios.jpa.dominio.entity.IdiomaEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.DiccionarioJpaRepository;
import com.curso.diccionarios.jpa.dominio.mappers.DiccionarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiccionariosEditableRepositoryImplTest {

    @Mock
    private DiccionarioJpaRepository diccionarioJpaRepository;

    @Mock
    private DiccionarioMapper diccionarioMapper;

    @InjectMocks
    private DiccionariosEditableRepositoryImpl diccionariosEditableRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para obtener un diccionario por nombre e idioma
    @Test
    void testGetDiccionario() {
        // Dado: Simulamos una entidad existente en la base de datos
        String nombre = "Oxford";
        IdiomaEntity idiomaEntity = new IdiomaEntity();
        idiomaEntity.setCodigo("Inglés");

        DiccionarioEntity diccionarioEntity = new DiccionarioEntity();
        diccionarioEntity.setNombre(nombre);
        diccionarioEntity.setIdioma(idiomaEntity);

        Diccionario diccionario = Diccionario.builder()
                .nombre(nombre)
                .idioma(Idioma.builder().codigo("Inglés").build())
                .build();

        // Configuramos el mock para que devuelva la entidad simulada
        when(diccionarioJpaRepository.findByNombre(nombre)).thenReturn(Optional.of(diccionarioEntity));
        when(diccionarioMapper.entityToModel(diccionarioEntity)).thenReturn(diccionario);

        // Cuando: Se invoca el método para obtener el diccionario
        Optional<Diccionario> resultado = diccionariosEditableRepository.getDiccionario(Idioma.builder().codigo("Inglés").build(), nombre);

        // Entonces: Se verifica que el diccionario es retornado y tiene el idioma correcto
        assertTrue(resultado.isPresent());
        assertEquals("Oxford", resultado.get().getNombre());
        assertEquals("Inglés", resultado.get().getIdioma().getCodigo());
        verify(diccionarioJpaRepository, times(1)).findByNombre(nombre);
        verify(diccionarioMapper, times(1)).entityToModel(diccionarioEntity);
    }

    // Test cuando el diccionario no existe
    @Test
    void testGetDiccionarioNoExiste() {
        // Dado: Se configura el mock para devolver un Optional.empty() cuando el diccionario no exista
        String nombre = "Oxford";
        when(diccionarioJpaRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        // Cuando: Se invoca el método para obtener el diccionario
        Optional<Diccionario> resultado = diccionariosEditableRepository.getDiccionario(Idioma.builder().codigo("Inglés").build(), nombre);

        // Entonces: Se verifica que el resultado está vacío
        assertFalse(resultado.isPresent());
        verify(diccionarioJpaRepository, times(1)).findByNombre(nombre);
        verify(diccionarioMapper, times(0)).entityToModel(any());
    }

    // Test para crear un nuevo diccionario
    @Test
    void testNewDiccionario() throws AlreadyExistsEntityException, InvalidArgumentException {
        // Dado: Configuramos los mocks para simular la creación de un nuevo diccionario
        String nombre = "Oxford";
        Idioma idioma = Idioma.builder().codigo("Inglés").build();
        IdiomaEntity idiomaEntity = new IdiomaEntity();
        idiomaEntity.setCodigo("Inglés");

        DiccionarioEntity diccionarioEntity = new DiccionarioEntity();
        diccionarioEntity.setNombre(nombre);
        diccionarioEntity.setIdioma(idiomaEntity);

        Diccionario diccionario = Diccionario.builder()
                .nombre(nombre)
                .idioma(idioma)
                .build();

        // Configuramos los mocks
        when(diccionarioJpaRepository.findByNombre(nombre)).thenReturn(Optional.empty());
        when(diccionarioMapper.modelToEntity(any(Diccionario.class))).thenReturn(diccionarioEntity);
        when(diccionarioMapper.entityToModel(diccionarioEntity)).thenReturn(diccionario);
        when(diccionarioJpaRepository.save(any(DiccionarioEntity.class))).thenReturn(diccionarioEntity);

        // Cuando: Se invoca el método para crear un nuevo diccionario
        Diccionario resultado = diccionariosEditableRepository.newDiccionario(nombre, idioma);

        // Entonces: Se verifica que el diccionario se ha creado correctamente
        assertNotNull(resultado);
        assertEquals("Oxford", resultado.getNombre());
        verify(diccionarioJpaRepository, times(1)).findByNombre(nombre);
        verify(diccionarioJpaRepository, times(1)).save(any(DiccionarioEntity.class));
        verify(diccionarioMapper, times(1)).entityToModel(any(DiccionarioEntity.class));
    }


    // Test para obtener todos los diccionarios de un idioma
    @Test
    void testGetDiccionariosByIdioma() {
        // Dado: Simulamos varios diccionarios existentes en la base de datos
        IdiomaEntity idiomaEntity = new IdiomaEntity();
        idiomaEntity.setCodigo("Inglés");

        DiccionarioEntity diccionarioEntity1 = new DiccionarioEntity();
        diccionarioEntity1.setNombre("Oxford");
        diccionarioEntity1.setIdioma(idiomaEntity);

        DiccionarioEntity diccionarioEntity2 = new DiccionarioEntity();
        diccionarioEntity2.setNombre("Cambridge");
        diccionarioEntity2.setIdioma(idiomaEntity);

        Diccionario diccionario1 = Diccionario.builder()
                .nombre("Oxford")
                .idioma(Idioma.builder().codigo("Inglés").build())
                .build();

        Diccionario diccionario2 = Diccionario.builder()
                .nombre("Cambridge")
                .idioma(Idioma.builder().codigo("Inglés").build())
                .build();

        // Configuramos el mock para devolver las entidades simuladas
        when(diccionarioJpaRepository.findAll()).thenReturn(List.of(diccionarioEntity1, diccionarioEntity2));
        when(diccionarioMapper.entityToModel(diccionarioEntity1)).thenReturn(diccionario1);
        when(diccionarioMapper.entityToModel(diccionarioEntity2)).thenReturn(diccionario2);

        // Cuando: Se invoca el método para obtener todos los diccionarios de un idioma
        List<Diccionario> resultado = diccionariosEditableRepository.getDiccionario(Idioma.builder().codigo("Inglés").build());

        // Entonces: Se verifica que se retornan los diccionarios correctos
        assertEquals(2, resultado.size());
        assertEquals("Oxford", resultado.get(0).getNombre());
        assertEquals("Cambridge", resultado.get(1).getNombre());
        verify(diccionarioJpaRepository, times(1)).findAll();
        verify(diccionarioMapper, times(1)).entityToModel(diccionarioEntity1);
        verify(diccionarioMapper, times(1)).entityToModel(diccionarioEntity2);
    }
}
