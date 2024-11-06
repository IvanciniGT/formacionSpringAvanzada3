
texto = "Hola" # "Hola" es de tipo str, pero la variable texto no tiene tipo
texto = 4

def saluda(nombre):
   print("Hola " + nombre)

saluda("Federico")

miVariable = saluda         # Asignar la variable a la función
miVariable("Federico")      # Ejecuto la función a través de la variable

def imprimir_saludo(funcion_generadora_de_saludos, nombre):
    # Hay veces que cuando creo una función, parte de la lógica no la conozco.
    # Y hago que me la suministren como argumento (mediante una función)
    print(funcion_generadora_de_saludos(nombre))

def generar_saludo_formal(nombre):
    return "Hola Sr/Sra. " + nombre

def generar_saludo_informal(nombre):
    return "Qué pasa " + nombre + "!"

imprimir_saludo(generar_saludo_formal, "Federico")
imprimir_saludo(generar_saludo_informal, "Federico")

#imprimir_saludo(generar_saludo_formal("Federico"))
