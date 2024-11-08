package com.curso.diccionarios.dominio.repository;

import com.curso.diccionarios.dominio.model.Diccionario;
import com.curso.diccionarios.dominio.model.Idioma;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface DiccionariosRepository {

    // YAGNI: You Aren't Gonna Need It
    Optional<Diccionario> getDiccionario(@NonNull Idioma idioma, @NonNull String nombre);
    List<Diccionario> getDiccionario(@NonNull Idioma idioma);

}
// Esto es clave hacerlo... Si no lo hago (El definir los NonNull... y las Excepciones dejo un sistema ambigüo)
// El que vaya a implementar estas funciones mañana, pasado... tendría libertar para decidir que excepciones lanzar
// o si quiero admitir valores nulos... y eso es un error... porque no se está definiendo un contrato claro

// Y ME ESTARIA CAGANDO EN EL PRINCIPIO DE LISKOV... porque estaría permitiendo que el día de mañana alguien rompa el principio de sustitución de Liskov