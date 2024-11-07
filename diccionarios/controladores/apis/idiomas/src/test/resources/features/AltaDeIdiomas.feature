#language:es
Requisito: Alta de Idiomas

  Escenario: Debo poder dar de alta Idiomas si tengo los permisos adecuados
    Dado        que tengo un endpoint para dar de alta Idiomas
    Y           que tengo un objeto JSON
    Y           ese objeto JSON tiene un campo "codigo" con un valor "ES"
    Y           ese objeto JSON tiene un campo "nombre" con un valor "Español"
    Y           tengo el role "Editor"

    Cuando      hago una petición "POST" al endpoint "/api/v1/idiomas"
    Y           mando en el cuerpo el objeto JSON que tengo

    Entonces    recibo un código de respuesta "CREATED"
    Y           en el cuerpo de la petición recibo un objeto JSON
    Y           el Objeto JSON recibido tiene un campo "codigo" con un valor "ES"
    Y           el Objeto JSON recibido tiene un campo "nombre" con un valor "Español"

  Escenario: No debo poder dar de alta Idiomas si no tengo los permisos adecuados
    Dado        que tengo un endpoint para dar de alta Idiomas
    Y           que tengo un objeto JSON
    Y           ese objeto JSON tiene un campo "codigo" con un valor "ES"
    Y           ese objeto JSON tiene un campo "nombre" con un valor "Español"
    Y           no tengo el role "Editor"

    Cuando      hago una petición "POST" al endpoint "/api/v1/idiomas"
    Y           mando en el cuerpo el objeto JSON que tengo

    Entonces    recibo un código de respuesta "FORBIDDEN"

  Esquema del escenario: No debo poder dar de alta Idiomas si el código no es adecuado
    Dado        que tengo un endpoint para dar de alta Idiomas
    Y           que tengo un objeto JSON
    Y           ese objeto JSON tiene un campo "codigo" con un valor "<codigo>"
    Y           ese objeto JSON tiene un campo "nombre" con un valor "<nombre>"
    Y           no tengo el role "Editor"

    Cuando      hago una petición "POST" al endpoint "/api/v1/idiomas"
    Y           mando en el cuerpo el objeto JSON que tengo

    Entonces    recibo un código de respuesta "BAD REQUEST"
    Y           en el cuerpo de la petición recibo un objeto JSON
    Y           el Objeto JSON recibido tiene un campo "campo" con un valor "codigo"
    Y           el Objeto JSON recibido tiene un campo "mensaje" que contiene el texto "<error>"

    Ejemplos:
      | codigo | nombre  | error |
      | E      | Español | El código de idioma debe tener entre 2 y 5 caracteres |
      | ESLLSJ | Español | El código de idioma debe tener entre 2 y 5 caracteres |
      | Epaña  | Español | El código de idioma no es válido                      |
