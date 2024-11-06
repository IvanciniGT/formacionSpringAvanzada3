# Hablar de cómo Spring gestiona la inyección de dependencias

Spring nos ayuda con la gestión de dependencias, es decir, nos ayuda a inyectar en nuestras clases los datos que necesitan para funcionar. 

## Cómo pedir a Spring que inyecte una dependencia

En ocasiones nuestras clases / métodos necesitan de objetos concretos para funcionar. Se los podemos pedir a Spring de dos formas:

### Opción 1: Uso de la anotación `@Autowired`

```java


public interface MiInterfaz {
    void metodo();
}

import org.springframework.beans.factory.annotation.Autowired;
public class MiClase{
    @Autowired
    private MiInterfaz objeto;

    public MiClase() {
        //objeto.metodo(); // Aquí no estaría asignado la dependencia
    }

    public void suMetodo() {
        objeto.metodo();
    }
}

```

Esta opción me imagino que muchos la conocéis y la usáis a menudo. PROHIBIDO porque es una mierda! Por qué?
1. Cuándo se inyecta esa dependencia? Al final Spring escribirá un código como éste:
   ```java
    MiInterfaz objeto = new MiInterfazImpl(); // Eso suponemos que existe.. npi
    MiClase unaClase = new MiClase();
    unaClase.objeto = objeto;
    ```
    Cuándo se asigna el objeto ? Después de llamar al constructor
        - Limitación funcional, que me puede aplicar o no.
2. Parece que Spring no puede hacer ese código: `unaClase.objeto = objeto` por ser la variable privada.
   Entonces qué hace Spring... cómo lo hace? Usando un paquete de java PROSCRITO (de hecho limitado en las últimas versiones de java por considerarse inseguro y una mala práctica) llamado reflection. Ese paquete nos permite atacar directamente a datos en RAM, saltándonos las restricciones de acceso que impone el lenguaje:
   - Inseguro
   - En muchas versiones modernas de Java desactivado por defecto
   - Lento
   SONARQUBE OS TIRA ESE CODIGO A LA CARA! MALA PRÁCTICA! 

Además, nota general:
- Es importante entender que esto SOLO FUNCIONARA si Spring es quien crea la instancia de mi clase (quien hace el new). Si yo hago el new, Spring no podrá inyectar la dependencia.
  Limitación funcional!  HAY QUE SABERLO. ni bueno, ni malo.

### Opción 2: Pedir los datos que necesite en una función invocada por spring

```java

public interface MiInterfaz {
    void metodo();
}

public class MiClase{
    public void suMetodo(MiInterfaz objeto) {
        objeto.metodo();
    }
}

```

Si Spring es quien invoca a `suMetodo`, Spring le pasará el objeto que necesita.
Pregunta? Aquí es necesario usar `reflection`? NO... todo fluye guay! RAPIDO, SEGURO !

Además, nota general:
- Es importante entender que esto SOLO FUNCIONARA si Spring es quien llama a mi función. Si yo quien llama a la función, Spring no podrá inyectar la dependencia.
  Limitación funcional!  HAY QUE SABERLO. ni bueno, ni malo.

### Oye... y mira que... se me acaba de ocurrir...


```java
public interface MiInterfaz {
    void metodo();
}

public class MiClase{

    private final MiInterfaz objeto;

    public MiClase(MiInterfaz objeto) {
        this.objeto = objeto;
        objeto.metodo();
    }
    public void suMetodo1() {
        objeto.metodo();
    }
    public void suMetodo2() {
        objeto.metodo();
    }
}
```

Si Spring es quien crea la clase (Misma limitación funcional que en la Opción 1) eso implica que es Spring quien llama al constructor, que no es sino una función. Por tanto, Spring le pasará el objeto que necesita.
Esta opción, aplicando la opción número 2, me cubre el caso de uso de la opción 1. Y además sin ninguno de los problemas de la Opción 1.

## Cómo indicarle a Spring qué dependencia inyectar cuando se solicita una inyección de dependencias

Una cosa es que sepamos nosotros ya pedirle a Spring cositas... y otra cosa es que Spring sepa que entregarnos cuando le pido una cosita!

Antiguamente en spring esto se declara en ficheros XML... ufff que pereza! Ahora se hace con anotaciones:

### Opción 1: Uso de la anotación `@Component`
    
```java 

public interface MiInterfaz {
    void metodo();
}

public class MiClase{

    private final MiInterfaz objeto;

    public MiClase(MiInterfaz objeto) {
        this.objeto = objeto;
    }
    public void suMetodo() {
        objeto.metodo();
    }
}

import org.springframework.stereotype.Component;

@Component
public class MiInterfazImpl implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola mundo");
    }
}
```

Esa anotación le indica a Spring que cuando alguien pida una instancia de `MiInterfaz`, le entregue una instancia de `MiInterfazImpl`.
Eso funcionará siempre y cuando Spring sea capaz de leer esa clase (MiInterfazImpl) cuando arranca la aplicación. 
(Eso lo trataremos de forma independiente: @ComponentScan / @SpringBootApplication)

Esta anotación tiene muchas variantes (anotaciones que extienden a @Component).
Algunas de ellas solo aportan semántica al desarrollo que lee el código:
    @Repository     Le indica a un desarrollador que esa clase contiene lógica de persistencia
    @Service        Le indica a un desarrollador que esa clase contiene lógica de negocio
    @Controller     Le indica a un desarrollador que esa clase contiene lógica de exposición de servicios
Otras, en cambio, además de aportar semántica, aportan funcionalidad:
    @Configuration  
    @RestController 
    @ControllerAdvice 

Por defecto, cuando Spring arranca, de todas las clases que encuentre en su escaneo inicial (esto parte del flujo de Spring) va a crear una instancia... y la guarda en una cache (contexto de Spring). Cuando alguien pida una instancia de una interfaz que sea implementada por una de esas clases, devolverá la instancia que ya tiene en la cache.

En nuestro ejemplo:
1. Spring arranca, y encuentra la clase MiInterfazImpl
2. Spring crea una instancia de MiInterfazImpl y la guarda en su contexto
3. Cuando alguien pida una instancia de MiInterfaz, devolverá la instancia que ya tiene en su contexto.

NOTA: Si alguien pidiera una instancia de MiInterfazImpl, Spring también se la entregaría? SI
    PERO ESTO SERIA UNA CAGADA DESCOMUNAL... estaríamos rompiendo el ppo de inversión de dependencias... porque estaríamos acoplando la clase que pide la dependencia con la clase que la implementa.

Dicho de otra forma, Spring, por defecto me ofrece un comportamiento para esas clases tipo SINGLETON.
Antiguamente....

```java

public class MiSingleton {

    private static volatile MiSingleton instancia; // En entornos multihilo, JAVA por defecto usa la cache a nivel de core del microprocesador. El problema es que si otro hilo (que se estará ejecutando en otro core) intenta acceder a la variable instancia, no verá el cambio que ha hecho el otro hilo, verá el que tiene él a nivel de cache de su core. 
    // Volatile le indica a JAVA que no use la cache de core, que siempre vaya a la RAM a buscar el valor de la variable.

    private MiSingleton() {
    }

    public static MiSingleton getInstancia() {
        if(instancia == null) {                         // para evitar el sinchronized y que solo se haga la primera vez, que cuesta recursos.
            synchronized (MiSingleton.class) {          // Para evitar condiciones de carrera
                                                        // Es decir, que 2 hilos puedan simultaneamente ejecutar en 2 cores el if
                                                        // que a los 2 les dé null y los 2 creen una instancia de MiSingleton
                if(instancia == null) {                 // Para asegurar que solo 1 instancia de MiSingleton se crea
                    instancia = new MiSingleton();
                }
            }
        }
        return instancia;
    }
}

```

Cuidado... Spring me ofrece un comportamiento de SINGLETON... pero lo que tengo no es un Singleton. De hecho yo podría crearme otra instancia de MiInterfazImpl... y usarla. Simplemente Spring va a crear una sola instancia. Nada más.

Ese comportamiento tipo Singleton puede cambiarse, con la anotación adicional @Scope. Tenemos distintas opciones:
    - @Scope("singleton") (por defecto)
    - @Scope("prototype")               // Cada vez que alguien pida una instancia, Spring creará una nueva
    - @Scope("request")                 // Cada vez que alguien pida una instancia dentro de un request http, Spring creará una nueva
                                        // Que mantendrá viva hasta que acabe el request... Y para otro request (paralelo o posterior) creará otra
    - @Scope("session")                 // Cada vez que alguien pida una instancia dentro de una sesión http, Spring creará una nueva
                                        // Que mantendrá viva hasta que acabe la sesión... Y para otra sesión (paralela o posterior) creará otra

Qué pasa si... tengo 2 clases con anotación @Component que implementan la misma interfaz?
Pues depende... de quién y cómo la pida...

```java
public interface MiInterfaz {
    void metodo();
}
@Component
public class MiInterfazImpl1 implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola mundo");
    }
}
@Component
public class MiInterfazImpl2 implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola mundo");
    }
}
public class MiClase{
    private final List<MiInterfaz> objetos;
    public MiClase(List<MiInterfaz> objeto) {
        this.objeto = objeto;
    }
}

```

En este caso, no ocurre nada. Spring ofrece una lista con 2 instancias de las 2 implementaciones de MiInterfaz. Sin problema !

Otra cosa es que alguien solo pida una única instancia... y en ese caso, Spring no sabe cuál entregar. Directamente NO ARRANCA. Ofrece un error al iniciar la app.
Aquí tenemos varias opciones:
- @Primary. Y entonces Spring siempre entrega esa, aunque haya otras... Y ESTO PA QUE? Si si siempre entrego una, para que quiero más?
            ESTO SE USA principalmente para pruebas.
- Limitando el escaneo de clases... Que Spring solo tenga en cuentas las clases de un paquete concreto al buscar las implementaciones de una interfaz. @ComponentScan(basePackages = "com.mi.paquete")
- Profiles:
  - @ActiveProfiles("nombre")   Esto es a nivel de la aplicación
  - @Profile("nombre")          Y en el application.properties se indica cuál es el perfil activo. Esto es a nivel de un componente
- @Qualifier("nombre")  Eso se hace al declarar el componente (en la misma clase donde tenemos la anotación @Component)
                        Y se usa también al pedir la instancia (en la clase que pide la instancia) 


```java
public interface MiInterfaz {
    void metodo();
}
@Component
@Qualifier("tipoA")
public class MiInterfazImpl1 implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola mundo");
    }
}
@Component
@Qualifier("tipoB")
public class MiInterfazImpl2 implements MiInterfaz {
    public void metodo() {
        System.out.println("Hola mundo");
    }
}
public class MiClase{
    private final MiInterfaz objetos;
    public MiClase( @Qualifier("tipoA") MiInterfaz objeto) {
        this.objeto = objeto;
    }
}
public class MiClase2{
    private final MiInterfaz objetos;
    public MiClase2( @Qualifier("tipoB") MiInterfaz objeto) {
        this.objeto = objeto;
    }
}
```

Si con esta opción me sirve, no sigo leyendo. ESTA ES MI OPCION.

Esto me sirve cuando puedo ir al código de la clase y poner @Component. Si estoy en una lib de terceros.. no puedo hacerlo.

Si esta opción no me vale, entonces tengo:

### Opción 2: Uso de la anotación `@Bean` junto con `@Configuration`

```java

package com.libreria.de.terceros;

public interface SuInterfaz {
    void metodo();
}

public class SuClase implements SuInterfaz {
    public SuClase() {
    }
    public void metodo() {
        System.out.println("Hola mundo");
    }
}

public class MiClase{
    private final SuInterfaz objeto;
    public MiClase(SuInterfaz objeto) {
        this.objeto = objeto;
    }
}

@Configuration               
public class MiConfiguracion {
    @Bean
    public SuInterfaz federico() { // El nombre de esta función da igual
        return new SuClase();       // Soy yo el que le indica a Spring cómo crear la instancia que debe inyectar.
    }
}
```

@Configuration le indica a Spring que esa clase contiene definiciones de beans.
Si Spring lee esa clase anotada con @Configuration al arranque: 
- crea una instancia de ella
- buscará los métodos anotados con @Bean
- y los ejecutará... y guarda el resultado en cache (su contexto).
- Cuando alguien pida un dato del tipo que devuelve ese método, devolverá el resultado de ejecutar ese método.

Notas:
- Aquí aplican TODOS los mismos comentarios que hemos hecho para @Component:
  - @Scope
  - @Qualifier