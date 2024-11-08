package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.model.ContextoEditable;
import com.curso.diccionarios.jpa.dominio.mappers.ContextoMapper;
import com.curso.diccionarios.jpa.dominio.repository.testdoubles.ContextoJpaRepositoryDeMentijilla;
import com.curso.diccionarios.jpa.dominio.repository.testdoubles.ContextoMapperDeMentirijilla;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class ContextosConMocksPropiosTest {

    private ContextoJpaRepositoryDeMentijilla miRepoJPADeMentirijilla = new ContextoJpaRepositoryDeMentijilla();
    private ContextoMapper mapeador = new ContextoMapperDeMentirijilla();
    private ContextosEditableRepositoryImpl miRepoPublico = new ContextosEditableRepositoryImpl(miRepoJPADeMentirijilla, mapeador);
    // QUE GUAY... YA NO NECESITO LOS DE VERDAD... Estoy trabajando con unos de mentirijilla!
    // Que además se que no va a fallar!

    @Test
    void pruebaDeRecuperarUnContexto() {
        miRepoJPADeMentirijilla.teVanALlamarConElContexto("contexto_prueba");
        Optional<ContextoEditable> potencialContexto = miRepoPublico.getContexto("contexto_prueba");
        Assertions.assertTrue(potencialContexto.isPresent()); // Por que se que el repo de BBDD le va a contestar QUE LO TIENE.
        ContextoEditable contexto = potencialContexto.get();
        Assertions.assertEquals("contexto_prueba", contexto.getContexto());
        Assertions.assertEquals("Esta es la descripcion del contexto", contexto.getDescripcion());
        Assertions.assertTrue(miRepoJPADeMentirijilla.teHanLlamado());
        //Assertions.assertTrue(miRepoJPADeMentirijilla.teHanLlamadoALaFuncionFindByContext());
        //Assertions.assertEquals("contexto_prueba", miRepoJPADeMentirijilla.getContextoConElQueTeHanLlamado());
        // QUE QUIERO COMPROBAR?
        // Que en la clase: ContextosEditableRepositoryImpl
        // en el méto do: getContexto
        // Se está llamando al mét odo findByContexto del contextoJpaRepository
        // Y que el resultado se está transformando adecuadamente en un ContextoEditable
        // Y con esta prueba me estoy asegurando?
        // Y solo querria asegurarme de eso??? o querria asegurarme que le ha pasado los datos adecuados en la llamada?
    }

}
