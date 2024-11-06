import java.util.function.*;
// En java 1,8 aparece el paquete java.util.function que contiene interfaces
// Que sirven para representar funciones, por eso se denominan interfaces funcionales
// En ese paquete hay muchas interfaces:
// - Supplier<R>        Esto es una función que no recibe datos y devuelve un dato de tipo R
//                             Cualquier getter
//                             La invocamos con la función get()
// - Consumer<T>        Esto es una función que recibe un dato de tipo T y no devuelve nada (void)
//                             Cualquier setter
//                             La invocamos con la función accept()
// - Function<T,R>      Esto es una función que recibe un dato de tipo T y devuelve un dato de tipo R
//                             La invocamos con la función apply()
// - Predicate<T>       Esto es una función que recibe un dato de tipo T y devuelve un booleano
//                             Funciones de tipo isXXX? hasXXX? canXXX?
//                             La invocamos con la función test()

public class ProgramacionFuncional {

    public static void main(String[] args){
        String texto = "Hola"; // Asignamos la variable texto al valor "Hola"
        // Qué es una variable?
        // Un espacio en memoria donde guardamos algo.. eso sería una buena definición del concepto de 
        // variable en lenguaje C, o C++, o en ADA... Ni de coña en Java, ni python, ni en JS.
        // Una variable en JAVA no es una cajita donde pongo cosas!!!
        // En Python, JS, Java, una variable es una referencia a un dato almacenado en memoria.
        // Tiene más que ver con el concepto en C de puntero.
        // Esa linea hace 3 cosas:
        // - "Hola"             Crear un objeto de tipo String con valor "Hola" en RAM (en algún sitio.. npi)
        // - String texto       Defino una variable de Tipo String (es decir, que puede referenciar, apuntar a un objeto de tipo String)
        //                      Java es un lenguaje de tipado fuerte, mientras que JS o python son de tipado dinámico (o debil)
        //                      En Java, las variables tiene tipo, en python o js no (los datos si tienen tipo, pero las variable no)
        // - =                  Asigna la variable al dato (y no al revés)
        texto = "Adios";
        // - "Adios"            Crear un objeto de tipo String con valor "Adios" en RAM (en algún sitio.. npi)
        //                      Donde? En mismo sitio donde estaba el Hola o en otro? En otro. Y en este momento tengo 2 cosas en RAM
        // texto =              Desasigno la variable texto del objeto "Hola" y la asigno al objeto "Adios"
        //                      En ese momento el dato "Hola" queda huerfano, sin referencias, y se convierte en GARBAGE (Basura)
        //                      Y quizás o quizás no (npi) el recolector de basura de Java lo elimina de la memoria eventualmente.
        var otroTexto = "Hola"; // Java 11
        //otroTexto = 4;          // Esto no vale en JAVA., otroTexto es una variable de tipo String, ya que al declararla con var
                                // infiere el tipo de variable del primer dato que se asigna.
        saluda("Pepe");

        Consumer<String> miVariable = ProgramacionFuncional::saluda; // Y en java 1.8 aparece el operado nuevo ::, que me permite referenciar a un método
        miVariable.accept("Juan");

        imprimirSaludo(ProgramacionFuncional::saludaFormal, "Luis");
        imprimirSaludo(ProgramacionFuncional::saludaInformal, "Ana");

        // Expresiones lambda: Son ante todo EXPRESIONES
        // Qué es una expresión en un lenguaje de programación?
        String hola = "Texto"; //Statement (declaración, sentencia = FRASE, ORACION)
        int numero = 5+6;      // Otro statement
                     /// Expresion = Un trozo de código que devuelve un valor
        // Una lambda, por ser una expresión es un trozo de código que devuelve algo.. Qué algo? Una referencia a una función anónima creada dentro de la expresión
        // Y en java se crean con el operado nuevo (de java 1.8) FLECHA ->
        Function<String, String> funcionGeneradoraDeSaludos = ProgramacionFuncional::saludaFormal;
        imprimirSaludo(funcionGeneradoraDeSaludos, "Luis");
        Function<String, String> funcionGeneradoraDeSaludos2 = (String nombre) -> {
            return "Estimado " + nombre;
        };
        imprimirSaludo(funcionGeneradoraDeSaludos2, "Luis");
        // Las lambdas son uan alternativa a la forma tradicional de crear declarar funciones, cuando no necesito reutilizarlas 
        // y no me aporta legibilidad el tenerlas declaradas en un sitio distinto

        Function<String, String> funcionGeneradoraDeSaludos3 = (String nombre) -> {
            return "Estimado " + nombre;
        }; // Esta función devuelve un String.. ya que ? String + String = String

        Function<String, String> funcionGeneradoraDeSaludos4 = (nombre) -> {
            return "Estimado " + nombre;
        }; // y que recibe? Un String.. y lo sabe por qué? En este caso si se infiere de la declaración de la variable que es una Function<String, String>
        // Esta función devuelve un String.. ya que ? String + String = String... 
        Function<String, String> funcionGeneradoraDeSaludos5 = nombre -> {
            return "Estimado " + nombre;
        }; // y que recibe? Un String.. y lo sabe por qué? En este caso si se infiere de la declaración de la variable que es una Function<String, String>

        Function<String, String> funcionGeneradoraDeSaludos6 = nombre ->  "Estimado " + nombre; // y que recibe? Un String.. y lo sabe por qué? En este caso si se infiere de la declaración de la variable que es una Function<String, String>

        imprimirSaludo(nombre ->  "Estimado " + nombre, "Luis");

    }

    public static void saluda(String nombre) {
        System.out.println("Hola " + nombre);
    }
    
    public static String saludaFormal(String nombre) {
        return "Hola Sr/Sra. " + nombre;
    }

    public static String saludaInformal(String nombre) {
        return "Qué pasa " + nombre + "!";
    }

    public static void imprimirSaludo(Function<String, String> funcionGeneradoraDeSaludos, String nombre) {
        System.out.println(funcionGeneradoraDeSaludos.apply(nombre));
    }
}
