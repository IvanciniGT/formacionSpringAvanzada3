package com.curso.diccionarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Spring debe mirar todos lso componentes que existan en el classpath dentro de este paquete y subpaquetes
// Y ponerlos en marcha
public class MicroservicioDiccionarios {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioDiccionarios.class, args);
    }

}
