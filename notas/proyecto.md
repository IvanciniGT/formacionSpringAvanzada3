
# Lo que vamos a montar es una app tipo web RAE (Solo a nivel de backend)

## Casos de uso

Actores:       Usuario que consulta, Editor de diccionarios, Administrador del sistema
Casos de uso
    Usuario que consulta:
        - Buscar una palabra en un idioma
        - Buscar las palabras de un idioma que comienzan con unas letras
        x Buscar las palabras de un diccionario concreto (YAGNI, KISS)
    Usuario Editor de diccionarios:
        - Dar de alta idioma? o diccionarios? NO
        - Añadir una palabra a un diccionario
        - Añadir una definición a una palabra
        - Añadir un sinónimo a una palabra
        - Añadir un ejemplo a una definición
    Administrador del sistema
        - Dará de alta idiomas/diccionarios


De aquí me salen LOS ROLES.

Español
    Diccionario RAE
    Diccionario Larousse


## Requisitos

### Requisitos funcionales

Req 1: Buscar una palabra en un idioma (Usuario que consulta)
    Req 1.1: Debe dar igual sio la pone en mayúsculas o minúsculas

### Requisitos no funcionales

ReqNF 1: La búsqueda de palabras debe tardar menos de 500ms ....

## Teniendo los requisitos, de ahí inmediatamente nos salen las pruebas

## Diagrama entidad-relación
                          +--------------------------+
                          ^                          |
                         Variantes  Contextos  Tipos gramaticales
                           v          v        v
Idiomas < Diccionarios < Palabras < Definiciones < Sinonimos 
                                        ^
                                        Ejemplos

---

Vamos a montar un microservicio que nos permita hacer esto.... o varios microservicios

Un cambio de paradigma muy grande que tenemos hoy en día es:
- Antiguamente montábamos apps web:
  - El HTML se generaba en el servidor
  - Las apps eran stateful: HttpSession:
    - Carrito
    - Los propios datos del usuario tras logarse
    - Filtro de búsqueda que ha usado antes
- Hoy en día montamos apps web:
  - El HTML se genera en el cliente
  - Las apps son stateless: Hoy en día no hacemos eso ni de coña... Por qué? Es una mierda!
    - Sobrecargo el servidor: NEcesito mucha más RAM... para usuarios que no están activamente haciendo nada (Nos tocaba configurar mierdas: Que si un usuario lleva más de 30 minutos sin hacer nada, lo desconecto)
    - Hago unos programas mucho más complejos
    - En un entorno de producción, con HA.
            Tomcat1 <
                app1
                        Balanceador de carga    <    Proxy reverso      <       Usuarios
            Tomcat2 <
                app2

      Cada app server mantiene en su RAM sus datos de sesión. Configurábamos en el balanceador: Sticky sessions: Si un usuario se conecta a Tomcat1, siempre se conectará a Tomcat1. Si se conecta a Tomcat2, siempre se conectará a Tomcat2. Si no, el balanceador de carga lo manda a uno u otro aleatoriamente.

      El problema es si se cae el tomcat1.. que pasa con su RAM y sus datos de sesión = RUINA ! LOS PIERDO

      Aquí entraban las caches distribuidas de sesiones: Redis, Memcached, Infinispan, etc.

Para este proyecto vamos a adoptar un Clean Architecture = ROMPER CON EL MONOLITO

Necesitaremos Repositorios, Servicios, Controladores... Por donde empezamos? 2 opciones:
- Hacer un desarrollo bottom-top: Empezando por las capas bajas y subir (Empezando por los repos y negocio)
  - Esto exige tener un amplio conocimiento del sistema de partida.
  - Y los del frontal.. qué hacen mientras?
- Hacer un desarrollo top-down: Empezando por los controladores y bajar: API First

  FRONTAL > API < BACKEND

  Aquí estamos aplicando el principio de inversión de dependencias a nivel arquitectónico.


                         Variantes  Contextos  Tipos gramaticales
                           v          v        v
Idiomas < Diccionarios < Palabras < Definiciones < Sinonimos 
                                        ^
                                        Ejemplos


Controladores:
- Idiomas
  - API
  - Impl
- Diccionarios
  - API
  - Impl
- Contextos
  - Alta de contexto                /api/v1/contextos  POST
  - Baja de contexto                /api/v1/contextos/{id}  DELETE
  - Modificación de contexto        /api/v1/contextos/{id}  PUT
  - Recuperación de contexto        /api/v1/contextos/{id}  GET
- Tipos gramaticales
- PalabrasGestión
    InsertarNuevaPalabra // Otro CRUD típicos: Actualizar, Borrar      /api/v1/palabras POST
- PalabrasBusqueda
  - API
  - Impl
    BuscarPalabra                                                      /api/v1/palabras GET
        fuzzy: true/false
        que comience por: String
- Definiciones

---

# Principio de Responsabilidad Única

    Un componente debe asumir una única responsabilidad.
            vvv
    Un componente debe tener una única razón para cambiar.
            vvv
    Un componente debe atender a un único actor.