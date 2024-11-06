
# Maven

Herramienta de automatización de tareas para proyectos software (principalmente Java).

Se encarga de :
- Compilar el código fuente
- Compilar pruebas
- Ejectuar pruebas
- Empaquetar el código fuente
- Generar una imagen de contenedor
- Preparar informes de cobertura de código para sonar
- De enviar el proyecto a sonar
- Gestionar dependencias

Un proyecto típico:

proyecto/
    src/
        main/
            java/
            resources/
        test/
            java/
            resources/
    pom.xml

Toda tarea de maven se realiza con plugins.

# Archivo pom.xml

La configuración de maven para mi proyecto:
- Coordenadas: Datos identificativos del proyecto:
  - groupId: Identificador del grupo al que pertenece el proyecto
  - artifactId: Identificador del proyecto
  - version: Versión del proyecto
- Otros metadatos:
    - Nombre del proyecto
    - Descripción
    - URL
    - Licencia
    - Desarrollador
- Parámetros de configuración (variables):
    - Plugins
    - Para mi
- Plugins
- Dependencias


Maven tiene asociado un ciclo de vida:
- Clean: Limpia el proyecto
- Validate: Valida que el proyecto es correcto
- Compile: Compila el código fuente
- Test: Ejecuta las pruebas
- Package: Empaqueta el código fuente
- Verify: Verifica que el paquete es correcto
- Install: Instala el paquete en el repositorio local
- Deploy: Copia el paquete en el repositorio remoto

Luego hay tareas que se asocian a esas fases... y por defecto ya vienen muchas.

Por defecto el comportamiento de maven en las fases es:
- compile           Compila lo que hay src/main/java generando los .class que deja en target/classes
                    Copia lo que hay en src/main/resources en target/classes
- test-compile      Compila lo que hay en src/test/java generando los .class que deja en target/test-classes
                    Copia lo que hay en src/test/resources en target/test-classes
- test              Le pide al plugin surefire que ejecute los test JUNIT que hay en la carpeta target/test-classes 
                    Depende de la versión del plugin(que suele venir preconfigurada por la versión de maven) ejecutará test de JUnit 4 o 5 /Jupiter
- package           Genera el jar, war, ear o nada se genera en target/
- install           Copia el artefacto a mi carpeta .m2, para poder usar mi artefacto en otros proyectos de MI MAQUINA
- clean             Borra la carpeta target

# Carpeta .m2

Una carpeta que mavben crea dentro mi carpeta de usuario.
Cada vez que necesito una dependencia en un proyecto, maven la busca en esta carpeta.
Si no la encuentra la descarga a esta carpeta desde un repositorio remoto (por defecto maven usa maven central). Las empresas suelen configurar sus propios repositorios de alojamiento de artefactos: nexus, artifactory, etc.

