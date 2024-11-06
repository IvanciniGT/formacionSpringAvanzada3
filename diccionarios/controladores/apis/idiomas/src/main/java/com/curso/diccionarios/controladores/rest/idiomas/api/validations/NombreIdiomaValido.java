package com.curso.diccionarios.controladores.rest.idiomas.api.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotNull(message = "El nombre de idioma no puede ser nulo")
@Size(min = NombreIdiomaValido.LONGITUD_MINIMA, max = NombreIdiomaValido.LONGITUD_MAXIMA, message = "El nombre de idioma debe tener entre " + NombreIdiomaValido.LONGITUD_MINIMA + " y " + NombreIdiomaValido.LONGITUD_MAXIMA + " caracteres")
@Pattern(regexp = NombreIdiomaValido.PATRON, message = "El nombre de idioma no es válido")

public @interface NombreIdiomaValido {

    int LONGITUD_MINIMA = 2;
    int LONGITUD_MAXIMA = 50;
    String PATRON = "^[A-Za-záéíóúÁÉÍÓÚñÑçÇ ]*$"; // Cualquier carácter que pueda formar parte de una "palabra"

    String message() default "El código de idioma no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
