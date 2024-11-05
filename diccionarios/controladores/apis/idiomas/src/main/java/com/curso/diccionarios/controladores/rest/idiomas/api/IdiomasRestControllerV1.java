package com.curso.diccionarios.controladores.rest.idiomas.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController // Esto es una variante de @Component
// Pero además, Spring espera que aquí definamos endpoints HTTP
// Y esos los da de alta en automático en un tomcat
// Los endpoints de este controlador todos llevan el prefijo /api
@RequestMapping("/api")
public interface IdiomasRestControllerV1 {

    // En concreto quiero tener un endpoint que permita get en "/api" (lo que venía de arriba, el prefijo) seguido de /v1/idiomas
    @GetMapping("/v1/idiomas")
    @ResponseStatus(HttpStatus.OK)
    void obtenerIdiomas();

}
