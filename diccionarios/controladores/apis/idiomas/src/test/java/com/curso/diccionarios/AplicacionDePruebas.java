package com.curso.diccionarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Le pide a Spring que escanee todas las clases de este paquete y los subpaquetes
// que tengan anotación @Component o variantes... para que las cargue al arrancar
public class AplicacionDePruebas {
    public static void main(String[] args) {
        SpringApplication.run(AplicacionDePruebas.class, args); // Inversión de control
    }
}
