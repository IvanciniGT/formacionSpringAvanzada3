package com.curso.diccionarios.controladores.rest.idiomas.v1.advices;

import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.ErrorRestV1DTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

// Alquien que monte PROXIES a TODAS LAS CLASES QUE IMPLEMENTEN RestController
// dentro del paquete com.curso.diccionarios.controladores.rest.idiomas.v1 -> En nuestro caso es 1: IdiomasRestControllerV1Impl
@RestControllerAdvice(basePackages = "com.curso.diccionarios.controladores.rest.idiomas.v1")
@Slf4j
public class IdiomasRestControllerV1ExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRestV1DTO argumentoNoValido(Exception ex){
        log.info("Error de validación de datos", ex);
        return new ErrorRestV1DTO(ErrorRestV1DTO.TipoError.ERROR_DE_DATOS, ex.getMessage(), "codigo");
    }
}