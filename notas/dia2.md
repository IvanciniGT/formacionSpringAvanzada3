
# Spring

Framework de desarrollo que ofrece INVERSION DE CONTROL

## Inversión de control

Delegamos en un framework la responsabilidad de aportar el flujo a nuestra aplicación.
Impacto:
- Me quito del problema de escribir el flujo de la aplicación
- Vamos a poder usar un lenguaje declarativo (basado en anotaciones)
- Nos simplifica aplicar un patrón de inyección de dependencias

Y como consecuencia, conseguimos montar un código más fácil de mantener, de testear y de evolucionar.

## Inyección de dependencias

Patrón por el cual las clases no crean instancias de los objetos que necesitan... Sino que le son suministradas.
Esto nos ayuda a cumplir con el principio de inversión de la dependencia

## Principio de inversión de la dependencia

Los componentes de alto nivel no pueden depender de implementaciones de componentes de bajo nivel... Ambos deben depender de abstracciones.
Uno de los 5 grandes principios SOLID.
Gracias a respetar este principio se pueden hacer PRUEBAS UNITARIAS, que en otro caso serían imposibles.

## Principios que usamos hoy en día en el desarrollo de software

- SOLID
  - Single Responsability
  - Open/Closed
  - Liskov Substitution
  - Interface Segregation
  - Dependency Inversion
- DRY
- KISS
- YAGNI
- SoC

# Hoy en día, tenemos problemas distintos, y para ellos necesitamos soluciones distintas

- Metodologías ágiles (Nos ayudan a obtener feedback rápido mediante continuos despliegues de software en producción)
- Devops (nos ayuda a automatizar despliegues y pruebas... y en general todo el flujo de trabajo del ciclo de vida de una aplicación)
- No queremos arquitecturas MONOLITICAS... Queremos arquitecturas MODULARES (con componentes desacoplados - básicamente aplicar el ppo de inversión de la dependencia a nivel arquitectónico)
- Seguimos ciertas prácticas/patrones y principios de diseño (como los SOLID)
- Buscaremos formas de organizar el código de forma que cambios pequeños no impliquen grandes cambios en el código: Arquitecturas limpias, hexagonales, onion, etc.
- Seguir ciertas metodologías de desarrollo (TDD, BDD, DDD, ApiFirst.)

## Mantenibilidad y Evolucionabilidad del SOFTWARE

El software es un producto sujeto a mantenimiento y cambios. POR DEFINICION !
Desde el día 1, en el diseño del producto hay que tener esto en cuenta.

---

# Hablando de pruebas

## Vocabulario en el mundo del testing

- Causa raíz    Los motivos por el cuál los humanos cometemos errores
- Error         Los humanos cometemos errores (errar es de humano). Las máquinas no cometen errores.
                Esos errores los podemos cometer debido a una variedad de causas raíces: falta de conocimiento, falta de atención, falta de experiencia, cansancio, etc.
- Defecto       Al cometer un error, los humanos podemos introducir un defecto en el producto.
- Fallo         Ese defecto, cuando uso el producto, puede manifestarse en forma de fallo.

## Para qué sirven?

- Para asegurar el cumplimiento de unos requisitos
- Para tratar de identificar la mayor cantidad de defectos antes del uso del sistema.
  Para identificar defectos puedo:
  - Buscar defectos: revisión
  - Intentar provocar fallos. Una vez provocado un fallo (identificado un fallo) debemos proceder a identificar el defecto que lo ha provocado, que es lo que en última instancia queremos arreglar: DEPURACION/DEBUGGING. Esta es la estrategia más habitual para identificar defectos.
- Aportar toda la cantidad de información posible para la rápida identificación y corrección de defectos.
- Para saber cómo va mi proyecto (en met. ágiles)
- Para aprender de mi producto (know-how)
- Para hacer un análisis de causa raíz de los defectos y diseñar un plan de acciones preventivas.
- ...

Las pruebas, que tradicionalmente han sido tomadas en los proyectos de SOFTWARE como un mal necesario, como un gasto... hoy en día son vistas como una parte fundamental del desarrollo de software.
Cada vez más, la primera persona que entra en un proyecto de software es el tester, no el desarrollador.
Por qué? Para validar/revisar/probar los requisitos.

Adicionalmente, debido a las nuevas formas de trabajo y a las nuevas necesidades, necesitamos automatizar las pruebas. NO ES OPCIONAL. Es una necesidad.

## Tipos de pruebas

Las pruebas se clasifican según varias taxonomías paralelas entre si.

### Según en objeto de prueba

- Pruebas funcionales
- Pruebas no funcionales
  - Pruebas de rendimiento
  - Pruebas de carga
  - Pruebas de seguridad
  - Pruebas de usabilidad
  - Pruebas de UX
  - Pruebas de estrés
  - Pruebas de HA

### Según el alcance de la prueba (nivel de la prueba) - Scope

Soy una empresa de fabricación de bicicletas: BTWIN - Decathlon

    Sillín
        Prueba unitaria?    Voy a montar el sillín en un bastidor (4 hierros mal soldaos-algo en quién confío) y empiezo a probarlo:
            Carga? Aguanta a un tio de 140kgs o se desmonta?
            Seguridad? Se escurre una persona al sentarse? Le sujeta bien?
            Estrés? Después de 1 mes de uso, el cuero aguanta? o se ha desgasta'o?

    Cuadro
    Ruedas
    Sistema de frenos
        Prueba unitaria?    Voy a montar el sistema de frenos en 4 hierros mal soldaos, monto un sensor de presión entre las pinzas de freno y empiezo a probarlo:

            public void apretarPalancaDeFreno();
                    vvv
            Cuando aprieto la palanca de freno -> Que la pinza de freno se cierre y lo haga con una cantidad de presión adecuada.

    Sistema con una dinamo/batería/luces

- Unitarias             Se centran en un aspecto de un componente AISLADO
            Si se superan todas estas pruebas, tengo garantías de que la bici funcionará? NO
            Entonces que gano? CONFIANZA +1    Voy bien... vamos dando pasos en firme.

- Integración           Se centran en la COMUNICACION entre 2 componentes

    Cojo el sistema de frenos y las ruedas, que han llega'o. Quiero ver que funcionan bien juntos:

        Voy a montar el sistema de frenos en 4 hierros mal soldaos, monto la rueda en medio entre las pinzas de freno y empiezo a probarlo:
            public void apretarPalancaDeFreno();
                    vvv
            Cuando aprieto la palanca de freno -> Lo que quiero probar es que la rueda pare...

            Y mira que no! Resulta que la rueda es estrecha y la pinza no llega a cerrar tanto como para detenerla (para transmitir la presión-fuerza de rozamiento).

            Si se superan todas estas pruebas, tengo garantías de que la bici funcionará? NO
            Entonces que gano? CONFIANZA +1    Voy bien... vamos dando pasos en firme.

- Sistema(end-to-end) Se centra en el comportamiento del sistema en su conjunto

    Cojo la bici y me voy de Valladolid a Cuenca. y a ver si llego y no muele el culo, y no se ha desgastao...

    Si se superan todas estas pruebas, tengo garantías de que la bici funcionará? SI
    Si se superan todas estas pruebas, necesitaría hacer pruebas unitarias y de integración? NO

    Entonces, donde esta el truco?
    - Y si no se superan? Dónde está el problema? y que impacta?
    - Y cuando puedo hacer estas pruebas? Cuando tengo el sistema completo... y hasta entonces? Voy a ciegas???

  - Aceptación

### Según el procedimiento de ejecución

- Pruebas estáticas: las que no requieren ejecutar/usar el producto       -> DEFECTOS
- Pruebas dinámicas: las que requieren ejecutar/usar el producto          -> FALLOS

### Según el conocimiento del objeto de prueba

- Caja negra: Cuando no conozco o no tengo en cuenta el diseño/interiores del objeto de prueba
- Caja blanca: Cuando conozco y tengo en cuenta el diseño/interiores del objeto de prueba

### Otras clasificaciones

- Regresión: Cualquier prueba que repito para ver que no he roto nada
- Humo: Pruebas básicas para ver que el sistema arranca/ está instalado correctamente

~~> REQ-001: El sistema debe ofrecer unos tiempos de respuesta adecuados a los usuarios.~~
~~> REQ-001: El sistema debe ofrecer responder en menos de 500 ms a las peticiones de los usuarios.~~
> REQ-001: El sistema, instalado en un entorno con TALES características, debe responder el 95% de las peticiones en menos de 500 ms cuando tengo X usuarios conectados que están ejecutando TALES PROCEDIMIENTOS.
    REQUISITO NO FUNCIONAL DE RENDIMIENTO -> Prueba de aceptación de rendimiento
                                          -> Prueba de sistema de rendimiento

Voy a hacer pruebas unitarias de rendimiento? PUES CLARO.
Voy a medir la latencia de comunicaciones a la BBDD (prueba unitaria de rendimiento): 200 ms
-> Necesito implementar una estrategia fortísima de cache en local para evitar la latencia de la BBDD.

## En el mundo del testing, también existen PRINCIPIOS: FIRST
F - Fast
I - Independent
R - Repeatable
S - Self-validating
T - Timely (OPORTUNAS)

---

    --------------------- Backend ---------------------------------------
    BBDD < RepositorioAnimalitos < ServicioDeAnimalitos < ControladorRest 

                                    Animalito altaDeAnimalito(DatosDeAnimalito datos);
                                         validar los datos del animalito
                                         solicitar la persistencia de esos datos en la BBDD
                                         enviar un email a una lista de subscriptores


                                    ServicioDeEmilios
                                        void mandarEmilio(String destinatario, String asunto, String cuerpo);

> FUNCION OBJETIVO: altaDeAnimalito

Puedo probarla? SI
Quiero probarla? SI

Qué pruebas puedo definir para ella?
- Pruebas unitarias? SI, y quiero hacerlas
    Para poder hacer las pruebas unitarias necesito montar:
        - 4 hierros mal soldaos: Stub \
        - Sensor de presión      Spy  / TestDouble (muy mal llamados mocks)

    Vamos a probar lo que llamamos el happy-path?
        DADO
            un servicio de animalitos GUAYS con
            unos datos GUAYs de un animalito
            Y un repositorio de animalitos de mentirijilla, que cuando le pidan que de de alta un animalito, 
                devuelve los datos del animalito con un campo nuevo ID con valor 33 (STUB)
            Y un servicio de emilios de mentirijilla, que cuando le pidan que mande un emilio, no hace nada (DUMMY).
        CUANDO
            llamo a la función altaDeAnimalito con esos datos
        ENTONCES
            Me devuelve un animalito con el campo ID con valor 33



- Pruebas de integración? SI y quiero:
  - Prueba del Servicio de Animalitos con el Repositorio de Animalitos
  - Prueba del ControladorRest con el Servicio de Animalitos
  - Prueba del Servicio de Animalitos con el Servicio de Emilios
- Prueba de sistema? SI y quiero:
  - BBDD
  - RepositorioAnimalitos
  - ServicioDeAnimalitos
  - Servicio de emilios

# Test doubles

- Dummy
- Stub
- Fake
- Spy
- Mock

## Definición de pruebas

Una prueba se divide en 3 cosas:
- Contexto          Given       Dado
- Acción            When        Cuando
- Resultado         Then        Entonces

---


Plantear un proyecto y comenzar a escribirlo con El entorno de desarrollo

---

1º Plantear el proyecto
2º Buscar una buena arquitectura
3º Configurar ciertas herramientas que nos asistan en el desarrollo