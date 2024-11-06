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

// Sobre qué cosas podemos aplicar la anotación
@Target({ElementType.FIELD, ElementType.PARAMETER})
// Si la anotación debe mantenerse en tiempo de ejecución
@Retention(RetentionPolicy.RUNTIME)
// Aquí decimos que esta anmotación es una validación
@Constraint(validatedBy = {}) // Si quisiera que mi validación se hiciese mediante una función java
                              // me crearía una clase que implementase la interfaz ConstraintValidator
                              // Y ahi dentro definiría la lógica de validación
// Estas son mis restricciones adicionales que deben cumplir los datos
@NotNull(message = "El código de idioma no puede ser nulo")
@Size(min = CodigoIdiomaValido.LONGITUD_MINIMA, max = CodigoIdiomaValido.LONGITUD_MAXIMA, message = "El código de idioma debe tener entre " + CodigoIdiomaValido.LONGITUD_MINIMA + " y " + CodigoIdiomaValido.LONGITUD_MAXIMA + " caracteres")
@Pattern(regexp = CodigoIdiomaValido.PATRON, message = "El código de idioma no es válido")
public @interface CodigoIdiomaValido {

    int LONGITUD_MINIMA = 2;
    int LONGITUD_MAXIMA = 5;
    String PATRON = "^[A-Za-z_]*$";

    String message() default "El código de idioma no es válido";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload() default {};

}
