# El software

El software es un producto MANTENIBLE y EVOLUCIONABLE.

    Escribo código <> Pruebas -> OK -> Refactorizar <> Pruebas -> OK -> Entregar
    -------------------------------    -----------------------------
       Es solo 50% del trabajo          Es el otro 50% del trabajo
          20 horas                          20 horas

# Principios de desarrollo de software

- Principios SOLID de desarrollo de software
  5 principio recopilados por Robert C. Martin (El tío Bob). Cada letra de SOLID es un principio:
    - S: Single Responsibility Principle (SRP) - Cohesión
    - O: Open/Closed Principle (OCP)
    - L: Liskov Substitution Principle (LSP)
    - I: Interface Segregation Principle (ISP)
    - D: Dependency Inversion Principle (DIP)
  Estos 5 principios nos ayudan a escribir código fácil de:
    - Mantener
    - Escalar
    - Reutilizar
    - Probar
- KISS (Keep It Simple, Stupid)
- DRY (Don't Repeat Yourself)
- YAGNI (You Aren't Gonna Need It)
- SoC (Separation of Concerns) - Cohesión

# Principio de inversión de la dependencia

Un componente de alto nivel no debe depender de implementaciones de un componente de bajo nivel. En su lugar, ambos deben depender de abstracciones.

> Queremos montar una app de consola que permita a un usuario introducir un idioma y una palabra y me ofrezca los significados de la palabra en ese idioma (los sacará de un diccionario... la app tendrá configurados muchos diccionarios).

    $ buscarPalabra ES manzana
        La palabra manzana existe en idioma español y significa:
        - Fruto del manzano
  
  Cuántos proyectos MAVEN (pom.xml) necesitamos? Cuántos repos de git?
   - Frontal (Consola)
   - Backend (Gestión de los Diccionarios): Ficheros

```java

package com.curso.diccionarios.api; // --> diccionarios-api-1.0.0.jar

public interface Diccionario { //MODELO (Algo que representa un objeto que existe en la realidad)

    public String getIdioma();

    public boolean existe(String palabra) ;

    public Optional<List<String>> getSignificados(String palabra);
}

public interface SuministradorDeDiccionarios {

    public boolean tienesDiccionarioDe(String idioma);

    public Optional<Diccionario> getDiccionario(String idioma) ;

}

package com.curso.diccionarios.ficheros; // --> diccionarios-ficheros-1.0.0.jar

import com.curso.diccionarios.api.Diccionario;                     // Y esto está guay... es una interfaz

public class DiccionarioDesdeFicheros implements Diccionario { //MODELO (Algo que representa un objeto que existe en la realidad)

    public String getIdioma();

    public boolean existe(String palabra) {
        // CODIGO QUE DICE SI LA PALABRA EXISTE O NO en ese diccionario
    }

    public Optional<List<String>> getSignificados(String palabra) { // ESTO ES UNA PUTA MIERDA! El SONARQUBE me tira esto a la cara!
        // CODIGO QUE DEVUELVE LOS SIGNIFICADOS DE LA PALABRA
    }
    // Tengo una función definida con la que no me se comunicar!
    // Diccionario, dame los significados de la palabra ARCHILOCOCO en ES?
    // - Lista vacía             Es ambigua \ Miro la docu? Miro el código? Estamos en 2024!!!!
    // - null                    Es ambigua /
    // - NoSuchWordException     Es una mierda porque hacemos un uso ILEGITIMO de las Excepciones.
    //                           Una exception es CARA de generar (lo primero es hacer un volcado de la pila de llamadas / StackTrace)
    //                           Nunca debería generar una Excepción para algo que a priori puedo saber si va a generar la excepción.
    //                           Hay gente que aún así decide usarlo porque es EXPLICITA y no hay lugar a dudas:  
    //                                  throws NoSuchWordException
    // Y por eso, desde Java 1.8 SOLO SOLO SOLO HAY UNA OPCION VALIDA PARA ESTA FUNCION: Uso de Optional
    // Desde Java 1.8 POR CONVENIO, una función NUNCA PUEDE devolver null.
}

import com.curso.diccionarios.api.SuministradorDeDiccionarios;     // Y esto está guay... es una interfaz

public class SuministradorDeDiccionariosDesdeFicheros implements SuministradorDeDiccionarios {

    public SuministradorDeDiccionarios(String ruta){
        // CODIGO QUE CARGA LOS DICCIONARIOS DE LA CARPETA
    }

    public boolean tienesDiccionarioDe(String idioma){
        // CODIGO QUE DICE SI TENGO UN DICCIONARIO DE ESE IDIOMA
    }

    public Optional<Diccionario> getDiccionario(String idioma) {
        // CODIGO QUE DEVUELVE EL DICCIONARIO DE ESE IDIOMA
    }

}

package com.curso.app;  // --> app-1.0.0.jar

import com.curso.diccionarios.api.Diccionario;                   
import com.curso.diccionarios.api.SuministradorDeDiccionarios;   
//import com.curso.diccionarios.ficheros.DiccionarioDesdeFicheros; // LA CAGO DE NUEVO .. Mejor dicho:
        // Me vuelvo a cagar en el principio de inversión de dependencias

        // Y ahora busco estrategias para evitar ese import.... hay muchos PATRONES DE DESARROLLO DE SOFTWARE que me ayuda a evitarlo:
        // - Patrón Factory
        // - Patrón de inyección de dependencias

public class AppConsola {

    public static void main(String[] args) {
        // CODIGO QUE SEA
    }

    private static void procesarPeticion(String idioma, String palabra, SuministradorDeDiccionarios suministrador) { // Inyección de dependencias
        //SuministradorDeDiccionarios suministrador = new SuministradorDeDiccionariosDesdeFicheros("path"); // No puedo hacer new de una interfaz
        if(suministrador.tienesDiccionarioDe(idioma)){
            Diccionario diccionario = suministrador.getDiccionario(idioma).getOrThrow(new RuntimeException("Bug en la app"));
            if(diccionario.existe(palabra)){
                List<String> significados = diccionario.get().getSignificados(palabra).getOrThrow(new RuntimeException("Bug en la app"));
                System.out.println("La palabra " + palabra + " existe en idioma " + idioma + " y significa:");
                for(String significado : significados){
                    System.out.println("- " + significado);
                }
            } else {
                System.out.println("La palabra " + palabra + " no existe en idioma " + idioma);
            }
        } else {
            System.out.println("No tengo diccionario de idioma " + idioma);
        }
    }

}
```
DEPENDENCIAS:
    AppConsola -> SuministradorDeDiccionarios -> Diccionario
               -> Diccionario
              ^^^^
    En el caso de clases JAVA, esa dependencia la marca: Los IMPORTs

    app-1.0.0.jar -> diccionarios-1.0.0.jar

    En el caso de Empaquetados JAVA (.jar) esas dependencias quién la marca?: MAVEN en el pom, dentro del apartado <dependencies>

    Lo que queremos, al aplicar el ppo de inversión de dependencias es que las dependencias (flechas d diagrama UML) no apunten hacia clases... sino que SALGAN de las clases. Queremos INVERTIR las flechas que llegan a las clases... para que en lugar de llegar a clases, salgan de las clases.

    AppConsola -> SuministradorDeDiccionarios <- SuministradorDeDiccionariosDesdeFicheros -> Diccionario <- DiccionarioDesdeFicheros
               -> Diccionario <- DiccionarioDesdeFicheros

    app-1.0.0.jar -> diccionarios-api-1.0.0.jar <- diccionarios-ficheros-1.0.0.jar

Eso si... hay truco... Alguien podría decir.. Iván, lo único que has hecho es trasladar el problema a otro sitio... y es cierto... 
Al final alguien tiene que escribir este código (ESTAMOS EN JAVA): `new SuministradorDeDiccionariosDesdeFicheros("path")`

Bueno... en nuestro caso, ese problema me lo resuelve Spring, gracias a que es un contenedor de inversión de control.


# Inyección de dependencias

Patrón de desarrollo de software que nos ayuda a respetar el principio de inversión de dependencias, y dice:
- Una clase no debe crear instancias de los objetos que necesita. En su lugar, le deben ser suministrados.

---

# TDD (Test Driven Development)

Esto es una metodología de desarrollo de software que aplicamos mucho hoy en día.

Lo primero que hago son las pruebas <> Escribo código para superar las pruebas -> Refactorizar 


---

# Paradigma declarativo vs imperativo

En el mundo del desarrollo usamos muchos paradigmas de programación. 
Paradigma??? Es un nombre hortera que le damos en el mundo del software a una forma de usar un lenguaje. En los lenguajes naturales también lo tenemos.. no lo llamamos paradigmas, sino estilos de escritura.

- Imperativo            Cuando damos órdenes al ordenador que se deben procesar secuencialmente. 
                        En ocasiones queremos romper esa secuencialidad, y para ello usamos estructuras de control (if, for, while, switch, etc)
- Procedural            Cuando el lenguaje me permite agrupar órdenes en lo que llamamos funciones, procedimientos, subrutinas, 
                        métodos, etc. Y posteriormente invocarlas. Por qué hacemos esto?
                            - Estructurar mejor el código, para hacerlo más mantenible y evolucionable, legible
                            - Para reutilizar código
                            - Si tengo programación funcional, para poder pasarlas como argumentos a otras funciones que los requieran
                              (Si no necesito reutilizar ese código ni me aporta legibilidad el tenerlo declarado de forma independiente, puedo usar funciones anónimas (lambda))
- Funcional             Cuando el lenguaje me permite que una variable referencia a una función y posteriormente ejecutar la 
                        función desde la variable. El punto no es lo que es la programación funcional... que es una gilipollez.
                        El punto es lo que puedo llegar a hacer cuando el lenguaje me permite que una variable referencia a una función:
                        - Crear funciones que aceptan funciones como parámetros
                        - Crear funciones que devuelven funciones
- Orientado a objetos   Cuando el lenguaje me permite definir mis propios tipos de datos, con sus características y sus
                        funciones propias.
                        Todo lenguaje permite trabajar con algunos tipos de datos, ya definidos por el lenguaje.
                                         Características                Operaciones
                            String       Secuencia de caracteres        Concatenar, ponte en mayúsculas, etc
                            LocalDate    dia, mes, año                  Caes en finde? Suma 3 días
                            List<?>      Secuencia de elementos         Añadir, quitar, ordenar, etc
                            Usuario      Nombre, apellidos, edad        Cambiar contraseña, etc
                            Diccionario  Idioma, palabras               Buscar significado, existe una palabra, etc

                        Aquí luego salen conceptos más avanzados: Herencia, polimorfismo, encapsulación, etc.
- Declarativo           El paradigma imperativo, al que estamos muy acostumbrados es un PUTA MIERDA! LO ODIAMOS. CADA DIA MAS
                        De hecho, todas las herramientas actuales que están triunfando, lo hacen por hacer uso de paradigma declarativos:
                         - Angular
                         - Spring
                         - Ansible
                         - Terraform
                         - Kubernetes
                         - Docker compose 

> Felipe, SI (IF) hay algo que no sea una silla, 
    > Felipe, QUITALO!    IMPERATIVO
> Felipe, SI (IF) no hay una silla, 
    > Felipe, si no hay silla: Vete al Ikea y compra una silla  IMPERATIVO
    > Felipe, pon una silla debajo de la ventana.   IMPERATIVO

Y esta es la mierda de los lenguajes IMPERATIVOS, me olvido de lo que quiero conseguir para centrarme en explicar al computador COMO CONSEGUIR AQUELLO QUE QUIERO CONSEGUIR.

> Felipe, debajo de la ventana tiene que haber una silla.  DECLARATIVO
>
> Con el paradigma declarativo me centro en lo que quiero conseguir, y no en cómo conseguirlo. Eso lo delego en la herramienta que estoy usando (FELIPE).

Esto es lo que nos ofrece en general JAVA con las ANOTACIONES. Las anotaciones son PURO PARADIGMA DECLARATIVO. Y hoy en día TODO SPRING se basa en anotaciones.

Imaginaros que quiero montar un programa: Una ETL (script: programa Extract, Transform, Load) que cargue datos de un fichero en una BBDD.
Voy a establecer los requisitos (DECLARATIVO):
- Debe leer los datos de un fichero de texto, donde cada linea es un registro
- Ah! y que los datos se carguen en una BBDD MySQL
- Ah!... y quiero que cuando acabe el programa me mande un email con el resultado de la carga
- Ah! y Cuando empiece que también me mande un email
- Ah! y que valide también la edad de la persona... si es menor de 18 años, que no se cargue el registro en la BBDD
- Ah!... y quiero que se validen en los datos el DNI de la persona, que sea correcto, sino que no se cargue el registro en la BBDD
- Ah! y que cuando acabe, si hay errores o menores, que me lo mande en un email

Vamos a montar el programa:

```pseudolenguaje
# IMPERATIVO
Mandar un emilio de inicio!
Creear una variable para ir guardando datos incorrectos y menores
Ir leyendo el fichero... y para cada línea:     // Esto es un bucle
    Validar el DNI
    Validar la edad
    if(DNI y edad correctos){
        Cargar el registro en la BBDD
    } else {
        Guardar el registro en la variable de datos incorrectos y menores
    }
Mandar un emilio de fin!
if(hay datos incorrectos o menores){
    Mandar un email con los datos incorrectos y menores
}
```


# Qué es eso de la inversión de control?

Cuando delego a un framework (un framework de inversión de control) la responsabilidad de crear el flujo de ejecución de mi programa.
Yo dejo de escribir el flujo de ejecución de mi programa, y en su lugar, le digo al framework qué quiero que haga mi programa (flujo incluido).

Cómo es un programa Spring(la función main?):

```java

public class MiAppString {

    public static void main(String[] args) {
        SpringApplication.run(MiAppString.class, args);     // Aquí estoy haciendo la inversión de control
                                                            // Delego a Spring la responsabilidad de aportar el flujo de ejecución de mi programa
    }

}
```

Y es uno de los grandes problemas de usar spring.. cuando no estamos acostumbrados.. Enseguida buscamos el flujo de ejecución de nuestro programa, y no lo encontramos... porque no está en mi código. Lo pone Spring. Una de las primeras cosas que hemos de aprender es el flujo de ejecución de Spring.

# Qué es Spring?

Framework de desarrollo para Java, que ofrece INVERSION DE CONTROL.
Spring, el framework más aberrante (por grande, más de 200 librerías) que hay en el mundo de la programación (solo equiparable a .NET de Microsoft), se creo (SPRING CORE -  1 librería, la base y objetivo fundamental de Spring) con 2 objetivos:
- Permitir el uso de paradigmas DECLARATIVOS en lugar de IMPERATIVOS (Anotaciones)
- Como Spring (por ser un framework de inversión de control) es quien hila mi programa (el que aporta el flujo, el que llama a mis clases y mis funciones), puedo pedirle que cuando llame a una función le entregue los objetos que necesite mi función (Inyección de dependencias). Facilitar el uso del patrón de inyección de dependencias, para poder respetar el principio de inversión de dependencias, para tener software más mantenible y evolucionable.

Como tiene tantas librerías, y es necesario preconfigurar muchas cosas, en un momento dado surge SPRING BOOT, que nos ayuda a rápidamente arrancar un proyecto con Spring, sin tener que configurar muchas cosas.

El objetivo: Montar un sistema con componentes DESACOPLADOS, que se comuniquen entre sí a través de interfaces. Esto me ayudará a crear sistemas fáciles de mantener, evolucionar, reutilizar y probar.

---

Spring no es una herramienta aislada. Para montar un proyecto de software hacen falta muchas más cosas que un framework de desarrollo.
- Lenguaje de programación (que soporte más o menos funcionalidades)
- Herramienta que me ayude a empaquetar, probar... mi software (Maven, Gradle)
- Voy a usar ciertas metodologías de desarrollo de software (TDD, DDD, BDD, ApiFirst, etc)
- Voy a emplear ciertas arquitecturas de software (MVC, Microservicios, etc)
- Voy a seguir una serie de prácticas de desarrollo de software (SOLID, KISS, DRY, YAGNI, etc)
- Voy a llevar una determinada metodología de trabajo (Scrum, Kanban, etc)
TODAS ESAS COSAS HAN EVOLUCIONADO A LO LARGO DE LOS AÑOS EN PARALELO ENTRE SI, para resolver los problemas que íbamos teniendo en cada momento del tiempo.

Si intento usar Spring con una:
- Metodología tradicional de desarrollo de software (cascada)
- Arquitectura de software tradicional (monolítica)
- Apache Ant (esto se usaba hace 25 años) (en lugar de maven)
- Si paso de TDD
- Si no sigo principios de desarrollo de software (SOLID, KISS, DRY, YAGNI)
NO ENCAJA

No se trata de entender Spring. Spring evoluciona junto con el resto de herramientas y metodologías de desarrollo de software, para enfrentarnos a los problemas que tengo hoy en día, que son distintos de los que tenía hace 10 años, cuando salió java 1.8, o hace 20 años, cuando salió java 1.4.

Java 1.8 - Sistema monolítico - Metodología cascada - Apache Ant - No TDD - No SOLID = GUAY !
Si quieres eso, montar un sistema monolítico, con metodología cascada, con Apache Ant, sin TDD, sin SOLID... NO NECESITAS SPRING. NO NECESITAS NADA DE LO QUE HA EVOLUCIONADO EN EL MUNDO DEL DESARROLLO DE SOFTWARE EN LOS ÚLTIMOS 20 AÑOS.

---

# Metodologías ágiles

Las metodologías ágiles son la evolución de las metodologías tradicionales de desarrollo de software (cascada, espiral, v,  etc), para resolver los problemas que nos encontrábamos al aplicar esas metodologías.

Uno de los objetivos de las metodologías ágiles es crear software que cuando lleguen cambios en los requisitos (Y LLEGARAN) no me suponga un problema, y pueda adaptar mi software a esos cambios de forma rápida y sencilla.

La principal característica de una metodología ágil es entregar el producto de forma incremental a mi cliente, para obtener feedback rápido.

Esto ha resuelto muchos problemas... pero viene con sus propios problemas:

Iteración 1: a los 20 días de iniciar proyecto -> PASO A PRODUCCION
    +5% de funcionalidades
    INSTALACIONES EN PRE Y PRODUCCION
    Pruebas a nivel de pro! 5%

Iteración 2: a los 15 días de la anterior -> PASO A PRODUCCION
    +10% de funcionalidades
    INSTALACIONES EN PRE Y PRODUCCION
    Pruebas a nivel de pro! 10% + 5%

Iteración 3: a los 10 días de la anterior -> PASO A PRODUCCION
    +5% de funcionalidades
    INSTALACIONES EN PRE Y PRODUCCION
    Pruebas a nivel de pro! 15% + 5%

Las pruebas y las instalaciones en pre y producción se multiplican... y 
De donde cojones sale la pasta, el tiempo y los recursos para eso? NI HAY PASTA, NI TIEMPO, NI RECURSOS
¿Qué hago? AUTOMATIZAR

> Extraído del manifiesto ágil: 

El software funcionando es la MEDIDA principal de progreso: ESTA FRASE DEFINE UN INDICADOR PARA UN CUADRO DE MANDO

La MEDIDA principal de progreso es "El software funcionando".
------------------------------- ----------------------------
    sujeto                              predicado

   ------
   Medida

Cómo voy a medir el grado de avance de mi proyecto: El concepto "SOFTWARE FUNCIONANDO"

Software funcionando: Software que funciona... que hace lo que tiene que hacer!
Quién dice que el software está listo, que hace lo que tiene que hacer? 
 - EL CLIENTE. NI DE COÑA!!! Como le voy a responsabilizar al cliente de QUE YO le entregue un producto ACABADO?
   - El cliente establece REQUISITOS. Esa es parte su responsabilidad
 - LAS PRUEBAS !

---

# Vamos a montar la tienda de Animalitos Fermín

Monolito, con JSPs... como hace 20 años. Pero esto hoy en día no sirve... al menos para Fermín.

Frontal                                                     Backend
--------------------------------------------------------    ------------------------------------------------------------------
- Web                             v.1.1.0             
    JS(Angular, React, VueJS...)
    
    Lógica de captura
    FORMULARIO  >    Servicio
                     Lógica de comunicaciones con Bend
        HTML <--- JSON                                          Lógica de exposición    Lógica de negocio  Lógica de persistencia
                                                                 v.1.2.0                v.1.3.0             v.2.1.0
- App Mobile Android  v.1.2.0                                   Controlador          >  Servicio         > Repositorio > BBDD
                                                                                        AnimalitoDTO        Animalito
                                                                                        - Nombre            - Nombre    
                                                                                        - Tipo              - Tipo
                                                                                        - Edad  < Mapper >  - Fecha de nacimiento
                                                                                        - Multimedia?             - Multimedia?
                                                                                        - Fecha de Nacimiento?
                                                                - Controlador REST v1                         
                                                                   AnimatoRestDTO
                                                                     - Nombre
                                                                     - Tipo
                                                                     - Edad 
                                                                     - Foto
                                                                     - Fecha de nacimiento?
                                                                - Controlador REST v2                         
                                                                   AnimatoRestDTO
                                                                     - Nombre
                                                                     - Tipo
                                                                     - Multimedia
                                                                     - Fecha de nacimiento
                                                                - Controlador SOAP
                                                                - Controlador WebSockets
- App Mobile iOS  v.1.1.0                     <--JSON---        detallesDelAnimalito
- App Desktop para los empleados v.1.0.0
- IVR: Interactive Voice Response v.1.1.0
- Siri, OkGoogle, Alexa v2.0.0


Dia 1 -> v1.0.0

De un animalito guardo:
  - Nombre
  - Tipo
  - Edad

De un animalito guardo:
  - Nombre
  - Tipo
  - Fecha nacimiento

De un animalito guardo:
  - Nombre
  - Tipo
  - Fecha nacimiento
  - Multimedia:
    - Fotos
    - Videos
    - Audios



> Quieren validar el dato "DNI de una persona" que se introduce en el formulario
> Me dicen que solo SOLO (es un requisito) puedo poner la validación en un sitio / componente: CUAL?

    Lógica de captura
    VALIDACION DE CORTESIA DEL DNI
    FORMULARIO  >    Servicio                                                                                             SQL
                     Lógica de comunicaciones con Bend                                  VALIDACION DE CORTESIA             v
        HTML <--- JSON                                          Lógica de exposición    Lógica de negocio  Lógica de persistencia
- App Mobile Android                                            Controlador          >  Servicio         > Repositorio > BBDD
                                                                                                                        ******
                                                                                                                         PL/SQL


HTML es un lenguaje de dominio específico (para páginas web... muy orientado a renderización por pantalla)
Necesito lenguajes de dominio general, para la transmisión de datos: ~~XML~~, JSON

Esos frameworks JS nos ayudan a montar COMPONENTES WEB <- Nuevo estandar del W3C, igual que HTML o CSS
Me permite crear mis propias etiquetas html
<animalito id="91736">
-> Lleva asociada una lógica de representación
-> Lleva asociada una lógica de comportamiento

Y los navegadores (TODOS) hoy en día soportan el trabajo NATIVAMENTE con WebComponents:
- Me permiten reutilizar código
- Me permiten crear SPA (Single Page Application): Páginas web que mutan dinámicamente, sin necesidad de recargar la página


---

# Devops

Devops no es una herramienta (No es Jenkins, ni Kubernetes), ni un perfil.
Es una filosofía, una cultura, un movimiento en pro de la AUTOMATIZACION. Qué quiero automatizar? TODO lo que hay entre el DEV-> OPS

            Automatizables?     Herramientas
PLAN            NO
CODE            NO
BUILD           SI              
                                JAVA: mavem, gradle, sbt
                                JS: npm, yarn, webpack
                                Python: pip, setuptools
                                C#: nuget, dotnet, msbuild
                                C, C++, ADA: make, gnat
---------------------------------------------------------> Desarrollo ágil
TEST            
    Definición  NO
    Ejecución   SI
                                Frameworks de pruebas: JUnit, TestNG, Cucumber, JBehave, MSTest, NUnit, xUnit, etc
                                Pruebas de UI Web: Selenium, Katalon, Karma
                                Pruebas de UI Desktop: WinAppDriver, Katalon, UFT
                                Pruebas de UI Mobile: Appium, Katalon
                                Pruebas de servicios web: Postman, soapui, readyapi, karate
                                Pruebas de rendimiento: JMETER, Gatling, LoadRunner
                                Pruebas de calidad de código: SonarQube, CheckStyle, PMD, FindBugs
    Y esas pruebas dónde se hacen?
      - En la máquina del desarrollador? NO... su máquina está malea'
      - En la máquina del tester?        NO... su máquina está malea'
      - En un entorno independiente precreado de pruebas? NO... esa máquina, después de 10 instalaciones, está malea'
      - Hoy en día, queremos entornos de pruebas de usar y tirar. Los monto cuando hacen falta, limpios, hago pruebas y a la basura: SI!
            ^^^^
            Contenedores (Docker, Podman, Containerd, LXC, LXD) (De hecho, el plugin oficial de spring para maven genera en automático imágenes de contenedor del producto)
---------------------------------------------------------> INTEGRACION CONTINUA -> Informe de pruebas en tiempo real!
Tener CONTINUAMENTE en el entorno de INTEGRACION la ultima version del software sometida a pruebas automatizadas?
RELEASE         SI
    Poner en manos de mi cliente la versión del producto (LA RELEASE)
    Si estoy montando una app mobile Android -> Dejar el apk en el google play
    Si estoy montando una app mobile iOS -> Dejar el ipa en la app store
    Si estoy montando un war (JAVA - web app)  -> Dejarlo en un artifactory o nexus
---------------------------------------------------------> ENTREGA CONTINUA (CONTINUOUS DELIVERY)
DEPLOY          SI  \
---------------------------------------------------------> DEPLOY CONTINUO (CONTINUOUS DEPLOYMENT)
OPERATE         SI   > KUBERNETES (Openshift)
MONITOR         SI  /
---------------------------------------------------------> DEVOPS COMPLETO


---

# Versiones de software

1.2.3

            ¿Cuánto se incrementan?
1 - MAYOR   Breaking changes (Campos que rompen compatibilidad con versiones anteriores)

2 - MINOR   Nueva funcionalidad
            Funcionalidad marcada como deprecated
                + Arreglos de bugs
3 - PATCH   Arreglo de bug (bugfix)


----


   MAD   Guadalajara ---------------> Barna
            Coche A2 ..1 hora .... 5 horas      = 5 horas
                        Zaragoza    Barna
    <------ Coche
    1 hora .......... avion....   1.5 horas     = 2.5 horas


---

