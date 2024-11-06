
Antiguamente teníamos J2EE: Java Enterprise Edition
Es una colección de estándares que permiten desarrollar aplicaciones empresariales en Jav:
- JDBC: Java Database Connectivity
- JMS: Java Messaging
- JPA: Java Persistence API
- ...

En un momento dado se le cambió el nombre -> JEE: Java Enterprise Edition
Más adelante Oracle donó esa colección de estandares a la fundacién Jakarta y se le cambió el nombre a JEE: Jakarta EE.

Antiguamente teníamos muchas importaciones que se hacian mediante:
import javax.***;

Esas, desde Java 17, han cambiado a 
import jakarta.***;

---

Cuando montamos una app web / microservicio, etc... Hay un factor crítico, que debe estar definido desde el día 0.

---

# Seguridad en Spring!

Cómo funciona en Spring web?

En Spring cuando activamos el modulo de seguridad para web, se activan una serie de filtros de seguridad,
que permitirán o no el acceso a los endpoints de nuestra aplicación.

Hay 2 tipos de filtros:
- Uno global
- Otro específico para cada controlador/endpoints
   - Spring tiene su propia forma de descentralizar la seguridad @PreAuthorize, @PostAuthorize
   - JEE tiene sus propias anotaciones de seguridad JSR-250: @RolesAllowed, @PermitAll, @DenyAll, etc.
   - Luego tenemos las anotaciones @Secured que siguen otro estándar.
   Y en el proyecto me dirán cual usar... y que roles hay disponibles.

Es una decisión a nivel de proyecto el usar filtros específicos para los controladores o no.

## Como se gestiona la Autenticación y la Autorización en Spring?

- Identificación: Que el usuario diga quien es
- Autenticación:  Comprobar que el usuario es quien dice ser
- Autorización:   Sabiendo que eres quien dices ser, determinar si tienes permisos para acceder a un recurso

Tradicionalmente la Autenticación se realizaba a nivel de cada app. Cada app tenía su propio sistema de autenticación... con sus tablas de usuarios, contraseñas, roles, etc.
Sus propias pantallas de gestión de usuarios, contraseñas, roles, etc.

Esto nos daba muchos dolores de cabeza:
- Información replicada en muchos sitios
- Sobrecarga en la gestión de usuarios
- Sobrecarga en desarrollo
- Los desarrollares no solemos tener ni idea de seguridad... hay expertos en seguridad.

Spring me permite montar mi propio sistema de seguridad, pero también me permite integrar sistemas de seguridad de terceros, que es la práctica habitual.

Además, esto es especialmente importante con el cambio de paradigma que ya comentamos en el que las apps web son stateless. Donde se guardan antes los datos del usuario al logearse (en la session) pero si ya no hay sesión, ya no hay donde guardarlos.
Spring me va a permitir montar apps stateful y stateless. Será de nuevo una decisión a nivel de proyecto.

Esas decisiones se deben tomar el día 1 y aplicar a TODO el proyecto.

Antiguamente, cuando trabajábamos con apps stateful (y sesiones), cuando el usuario se conectaba la primera vez con el app server (tomcat, jboss, weblogic, etc), se le asignaba un ID de sesión (JSESSIONID). 
OJO: ANTES DE HACER LOGIN!
Ese ID de sesión se guardaba en una cookie en el navegador del usuario. Cada vez que el usuario hacía una petición al servidor, se enviaba esa cookie con el ID de sesión. El servidor recuperaba los datos de sesión del usuario y los usaba para atender la petición.
Y podemos seguir haciéndolo... Aunque como esto da muchos dolores de cabeza (algunos los hemos comentado, otros no):
- El usar esto da lugar a un tipo de ataque muy sencillo: Cross Site Scripting (XSS), CRoss Site Request Forgery (CSRF), etc.
  Un usuario ha hecho login en una app. Y tiene en su navegador la cookie de sesión.
  Abre un correo malicioso con un enlace PULSA AQUI QUE TE HA TOCADO LA LOTERIA...
  Y el enlace me llega a esa app (la del banco) al servicio de transferencias, para dejarme sin billetes en la cuenta. Y como estoy logeado me deja hacer lo que sea.

Hoy en día vamos a por apps stateless basadas en tokens de sesión.
Cada vez que un usuario quiere hacer una petición a un backend, manda TODOS SUS DATOS (todo lo que sea necesario para poder hacer esa petición). Tampoco es tanto... antes siempre mandaba en cada petición el ID de sesión (solo que estaba en una cookie). 

Lo habitual hoy en día es que cuando un usuario se autentica, se le genera un token de sesión, que puede expirar o no. Cada vez que quiere hacer una petición debe mandarlo. Y en ese token se incluyen todos los datos del usuario.

El estándar hoy en día es JWT (JSON Web Token).

En cualquiera de los casos, Spring tiene lo que llama un CONTEXTO DE SEGURIDAD, que se asocia a cada petición HTTP que se recibe. En ese contexto se guarda toda la información de seguridad que se puede necesitar para atender la petición. Ahí esta guardado el nombre del usuario, si ha hecho login o no, los roles que tienes.

Usamos filtros para rellenar ese contexto de seguridad:
- Por ejemplo, en apps stateless desde la info que viene en el token JWT
- O en apps stateful desde la info que tengo en la sesión, a la que accedo mediante el id de sesión (lo que me viene en la cookie)
- A nivel de Spring, existe un conecto llamado UserDetailsService. Eso es una interfaz, que yo implementaré para mi app concreta, y que me permite recuperar la información de seguridad de un usuario a partir de su nombre de usuario. Puedo tener mi BBDD de usuario propia y mi propia implementación de UserDetailsService, o puedo integrar mi app con un sistema de seguridad de terceros, que me proporcionará su propia implementación de UserDetailsService.

Esos sitemas de seguridad de terceros suelen ser herramientas como:
- Keycloak
- LDAP
- Active Directory
- ...

---

# Como guardo una contraseña en BBDD?

Encriptada! Ejemplo claro de que no sabemos de seguridad.

No, nunca! Si me ganan la BBDD y me ganan la clave de encripción, tienen todas las contraseñas!

Lo que se guarda en una BBDD es una huella (HASH) de la contraseña.

Bueno... de hecho habría una forma de que dadas las huellas, pudieran conocer las contraseñas originales que dieron lugar a esas huellas: FUERZA BRUTA

Y por eso, un buen sistema de almacenamiento guarda no el hash de la contraseña... sino el hash del hash del hash del hash  y así hasta miles de veces de la contraseña.
---

# Algoritmo de HASH (de huella)

Esto es muy fácil. La letra del DNI es una huella (HASH) del dni.

    1233482 | 23
            +----------
         12  182312 < ESTO ME DA IGUAL!
         ^
         RESTO -> 0-22 El ministerio da una letra a cada número (a cada resto)

Un algoritmo de hash es una función que recibe un dato y produce otro de forma que:
- El mismo dato original siempre produce el mismo resultado
- Hay "poca" probabilidad de que 2 datos de entrada diferentes produzcan el mismo resultado
    1/24 = 0.0416666666666667 4%... para el ministerio es suficientemente "poco"
    En informática solemos usar algoritmos como SHA-256, SHA-512, etc... que tienen una probabilidades de colisión incluso de 1 entre trillones (de los españoles)
- Desde el resultado es impossible recuperar/regenerar el dato original (el resultado es un resumen del dato original)