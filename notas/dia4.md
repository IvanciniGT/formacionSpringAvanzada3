Mañana:
- Definiremos los roles de nuestra app
- Crearemos una politica de seguridad para la app
- Definiremos una app de pruebas que implemente esa política, de forma que cuando la gente (desarrolladores) quieran hacer pruebas de sus recursos (endpoints) hagan uso de esa app de pruebas.
- Eso si... cuando yo (Iván el desarrollador del controlador de Idiomas) esté centrado en la funcionaldiad (API) de mi controlador, me interesa saber cómo se gestiona la seguridad? quien hace la autenticación? Voy a tener un sistema montado de autenticación?
  Y entonoces necesitaré poder avanzar... y hacer mis pruebas, con seguridad implementada... pero sin tener que estar pendiente de la gestión de la seguridad. Y nos saldrá otra librería de spring
  - spring-security
  - spring-security-test <- Esta se basa en mockito

Y lo documentaremos en el swagger (openAPI) de la app.


Una vez hecho eso, comenzaremos a implementar el controlador de idiomas. > Servicio > Repositorio > Base de datos
