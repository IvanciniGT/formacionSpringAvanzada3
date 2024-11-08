package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.model.ContextoEditable;
import com.curso.diccionarios.jpa.dominio.AplicacionDePrueba;
import com.curso.diccionarios.jpa.dominio.entity.ContextoEntity;
import com.curso.diccionarios.jpa.dominio.jparepository.ContextoJpaRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AplicacionDePrueba.class)// Spring arrancame una app de pruebasen paralelo  para tener beans y cosas de esas.. que pedirte!
@ExtendWith(SpringExtension.class) // JUnit permite indicarle que hay parametros que debe pedirle a otros.
                                   // Y Spring tiene integración con JUnit en este sentido.
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContextosEditableRepositoryImplSystemTest {

    private final ContextosEditableRepositoryImpl miRepoPublico;
    private final ContextoJpaRepository miRepoBBDD;

    @Autowired
    public ContextosEditableRepositoryImplSystemTest(ContextosEditableRepositoryImpl miRepoPublico,
                                                     ContextoJpaRepository miRepoBBDD){
        this.miRepoPublico = miRepoPublico;
        this.miRepoBBDD = miRepoBBDD;
    }

    @Test
    @DisplayName("Probar a dar de alta un nuevo contexto guay")
    //@Order(1)
    void newContexto() throws InvalidArgumentException, AlreadyExistsEntityException {
        String contexto="desuso";
        String descripcion="Palabra que han caído en desuso";
        ContextoEditable contextoEditable = miRepoPublico.newContexto(contexto, descripcion);
        assertEquals(contexto, contextoEditable.getContexto());
        assertEquals(descripcion, contextoEditable.getDescripcion());

        // Comprobamos que se ha guardado en la BBDD
        Optional<ContextoEntity> entidadEnBBDD = miRepoBBDD.findByContexto(contexto);
        assertTrue(entidadEnBBDD.isPresent());
        assertEquals(contexto, entidadEnBBDD.get().getContexto());
        assertEquals(descripcion, entidadEnBBDD.get().getDescripcion());
    }

    //@Test(expected = AlreadyExistsEntityException.class) // JUnit 4
    @Test
    @DisplayName("Probar a dar de alta un nuevo contexto repetido")
    //@Order(2) PRACTICA HORRIBLE: Se carga uno delos principios FIRST de pruebas: I = Independent
    //AlreadyExistsEntityException
    void contextoRepetido() throws InvalidArgumentException, AlreadyExistsEntityException {
        String contexto="repetido";
        String descripcion="Contexto repetido";
        ContextoEditable contextoEditable = miRepoPublico.newContexto(contexto, descripcion);
        /*try {
            contextoEditable = miRepoPublico.newContexto(contexto, descripcion);
        } catch (AlreadyExistsEntityException e) {
            return;
        }
        fail("Debería haber fallado");
        */
        assertThrows(AlreadyExistsEntityException.class, ()-> miRepoPublico.newContexto(contexto, descripcion));
    }
}