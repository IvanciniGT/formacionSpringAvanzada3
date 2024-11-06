
# Como pedir dependencias a spring

## Opcion 1: @Autowired = RUINA
## Opcion 2: Pedir un dato en cualquier función que requiera un dato

Notas: 
- Eso solo funciona si: Si es Spring el que crea la clase (poción1) o llama a la función (opcion2)
- Para evitar el uso de autowired, podemos pedir los datos en el constructor

---
# Como indicar a Spring lo que debe ofrecer cuando alguien pide una dependencia

## Opcion 1: @Component (o derivados)
## Opcion 2: @Bean y @Configuration

Notas:
- Qué ocurre cuando hay varias posibilidades y se pide solo 1?
  - @Primary
  - @Qualifier
  - @ComponentScan
  - Profiles


---

# Proyecto para una app de búsqueda de palabras y gestión de diccionarios

  FRONTAL(es) -> HTTP REST <- BACKEND
                    API
                               ControladorRest > Servicio > Repositorio       (Como este puede haber varios)
                                   - Idiomas
                                   - Diccionarios
                                   - Palabras
                                   - Busquedas
                                   - Tipos Morfologicos
                                   - Contextos

## Spring tiene una librería llamada : Spring Web

Nos ofrece anotaciones adicionales a las estándar de Spring para trabajar con servicios REST

- @RestController (Extiende de @Component, mapea automáticamente las funciones a endpoints que se definan en un Serv. de apps, Las peticiones que se reciban por HTTP tendrán un body, donde se espera un JSON que se convertirá en automático en un objeto java... y lo mismo con el response. Las funciones JAVA que hagamos podrán devolver objetos java y se convertirán automáticamente en JSON)
- @RequestMapping 
  - A nivel de una función: Para mapear una función a un endpoint concreto
  - A nivel de la clase:    Para mapear un prefijo a todos los endpoints definidos para cada función
- @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
  - Asignar un endpoint a una función concreta (pudiendo especificar el método HTTP que se espera en la petición) y el endpoint
- @ResponseStatus
  - Para indicar el código de respuesta HTTP que se espera en la respuesta
- Clase ResponseEntity<T> // Es el equivalente a lo que antiguamente era un HttpServletResponse
  - Para devolver una respuesta HTTP con un código y un objeto java

---

En servlets:
```java
public class MiServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    // Código
  }
}
```


---

# Programación orientada a aspectos (AOP)

Esto de las gracias de Spring!

Esto se basa en una clase que hay en java que tiene más años que la tana... llamada Proxy.
Me permiten montar clases (no objetos) dinámicamente, que actuen de proxy sobre los objetos de otra clase.
Básicamente lo que hacen es usar un patrón Decorator para añadir funcionalidades a los objetos de otra clase.

Un uso muy cómodo y habitual de la AOP es el uso de advices (consejos) que se ejecutan antes o después de una función de una clase.
@Advice es una anotación que ofrece Spring CORE! que nos permite definir Proxies de forma muy sencilla.

Hay anotaciones más avanzadas que extienden a @Advice:
- @RestControllerAdvice: esta anotación me permite definir una clase que se encargue de gestionar las excepciones que se produzcan en los controladores REST de forma centralizada = GUAY ! GUAY QUE TE CAGAS!!!!

---

# Patron decorador en POO

```java 
public interface Componente {
  void operacion();
}

public class ComponenteConcreto implements Componente {
  @Override
  public void operacion() {
    System.out.println("Operación de componente concreto");
  }
}

public class Decorador implements Componente { // Un Proxy de Java nos permite montar estas clases (CODIGO) de forma dinámica
  private Componente componente;
  
  public Decorador(Componente componente) {
    this.componente = componente;
  }
  
  @Override
  public void operacion() {
    System.out.println("Antes de la operación");
    try{
      componente.operacion();
    } catch (Exception e) {
      System.out.println("Ha ocurrido un error");
    }
    System.out.println("Después de la operación");
  }
  // Casos de uso:
  // - Añadir funcionalidades a una clase sin modificarla
  // - Errores
  // - Logging
  // - Auditoria
  // - Seguridad
}

public class ClaseQueUsaComponente{

  public void hacerAlgoConUnComponente(Componente componente) { // Esto lo puedo aplicar gracias a estar aplicando a su vez un patrón de inyección de dependencias
    componente.operacion();
  }

}
```

De cara a quien lo use (ClaseQueUsaComponente), no cambia nada.
De cara a quien lo implementa (ComponenteConcreto), no cambia nada.

---

El API del servicio REST lo estamos definiendo en código JAVA (interfaces + ...)
A mi (desarrollo backend) me interesa así.

Pero al tio(a) de frontal se la pela! De JAVA ni flores!

PAra eso hay un estándar que permite definir APIS web: OPENAPI (antiguamente llamado Swagger)
Swagger era la v1 y 2 de la especificación. La versión 3 paso a denominarse OpenAPI

Eso lo monta la gente de SmartBear, que también montan:
- SOAP_UI
- PEPINO! y su lenguaje PEPINILLO! Para las pruebas
  - Cucumber / Gherkin 