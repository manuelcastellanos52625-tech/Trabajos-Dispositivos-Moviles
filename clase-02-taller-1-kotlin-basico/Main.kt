/**
 * TALLER 1: Kotlin Fundamentals I
 * Asignatura: Programación para Dispositivos Móviles
 * 
 * Estudiante: Manuel Felipe Castellanos Calderón
 * Código: 52625
 * Fecha de entrega: 10 de septiembre de 2026
 */

/* Ejercicio 1: pasar de Celsius a Fahrenheit */
fun ejercicio1_Temperatura() {
    val celsius = 25.0                         // temperatura de partida
    val fahrenheit = celsius * 1.8 + 32        // 9/5 es lo mismo que 1.8
    println("$celsius°C equivale a $fahrenheit°F")
}

/* Ejercicio 2: calcular el IMC y decir en qué rango cae */
fun ejercicio2_IMC() {
    val peso = 70.0        // en kilogramos
    val altura = 1.75      // en metros

    val imc = peso / (altura * altura)

    // Reviso de mayor a menor: la primera condición que se cumpla gana
    val clasificacion = when {
        imc >= 30.0 -> "Obesidad"
        imc >= 25.0 -> "Sobrepeso"
        imc >= 18.5 -> "Peso normal"
        else -> "Bajo peso"
    }

    println("Peso: ${peso}kg, Altura: ${altura}m")
    println("IMC: %.2f".format(imc))
    println("Clasificación: $clasificacion")
}

/* Ejercicio 3: saber si un número es par o impar con el módulo */
fun ejercicio3_ParImpar() {
    val numero = 15
    val esPar = numero % 2 == 0                // residuo 0 significa que es par

    val resultado = if (esPar) "Par" else "Impar"

    println("El número $numero es: $resultado")
}

/* Ejercicio 4: revisar si un número es primo buscando divisores */
fun ejercicio4_NumeroPrimo() {
    val numero = 23
    var esPrimo = numero > 1                   // el 1, el 0 y los negativos no cuentan

    // Pruebo divisores desde 2 y me detengo apenas encuentre uno
    var divisor = 2
    while (divisor < numero && esPrimo) {
        if (numero % divisor == 0) {
            esPrimo = false
        }
        divisor++
    }

    val mensaje = if (esPrimo) "ES primo" else "NO es primo"
    println("El número $numero $mensaje")
}

/* Ejercicio 5: sumar los pares entre 1 y 100 */
fun ejercicio5_SumaPares() {
    var suma = 0

    for (i in 1..100) {
        if (i % 2 != 0) {
            continue                            // los impares se saltan
        }
        suma += i
    }

    println("La suma de números pares del 1 al 100 es: $suma")
}

/* Ejercicio 6: factorial multiplicando de arriba hacia abajo */
fun ejercicio6_Factorial() {
    val numero = 6
    var factorial: Long = 1                    // Long por si el número crece mucho

    // 6 * 5 * 4 * 3 * 2 * 1
    for (factor in numero downTo 1) {
        factorial = factorial * factor
    }

    println("El factorial de $numero es: $factorial")
}

/* Ejercicio 7: una palabra es palíndromo si al voltearla queda igual */
fun ejercicio7_Palindromo() {
    val palabra = "reconocer"
    val palabraInvertida = palabra.reversed()

    val esPalindromo = palabra == palabraInvertida

    val resultado = if (esPalindromo) "ES un palíndromo" else "NO es un palíndromo"

    println("La palabra '$palabra' $resultado")
}

/* Función main entregada en el taller - no se modifica */
fun main() {
    println("=" .repeat(50))
    println("TALLER 1: KOTLIN FUNDAMENTALS")
    println("Nombre: [Tu nombre aquí]")
    println("=" .repeat(50))
    println()

    println("--- Ejercicio 1: Conversor de Temperatura ---")
    ejercicio1_Temperatura()
    println()

    println("--- Ejercicio 2: Calculadora de IMC ---")
    ejercicio2_IMC()
    println()

    println("--- Ejercicio 3: Número Par o Impar ---")
    ejercicio3_ParImpar()
    println()

    println("--- Ejercicio 4: Números Primos ---")
    ejercicio4_NumeroPrimo()
    println()

    println("--- Ejercicio 5: Suma de Números Pares ---")
    ejercicio5_SumaPares()
    println()

    println("--- Ejercicio 6: Factorial ---")
    ejercicio6_Factorial()
    println()

    println("--- Ejercicio 7: Palíndromo ---")
    ejercicio7_Palindromo()
    println()

    println("=" .repeat(50))
    println("FIN DEL TALLER")
    println("=" .repeat(50))
}
