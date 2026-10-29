/**
 * TALLER 2: Fundamentos de Kotlin (Clase 02)
 * Asignatura: Programación para Dispositivos Móviles
 * Estudiante: Manuel Felipe Castellanos Calderón · Código: 52625
 */

fun letraDe(nota: Int): String = when (nota) {
    in 90..100 -> "A"
    in 80..89 -> "B"
    in 70..79 -> "C"
    in 60..69 -> "D"
    else -> "F"
}

fun main() {
    // Paso 3.1: clasificador
    for (nota in listOf(95, 85, 72, 65, 40)) println("Nota $nota -> ${letraDe(nota)}")

    // Paso 3.2: tabla del 7
    val numero = 7
    for (i in 1..10) println("$numero x $i = ${numero * i}")

    // Paso 3.3: conteo regresivo
    for (i in 10 downTo 1) println(i)
    println("¡Despegue!")

    // Paso 3.4: suma con while
    var suma = 0
    var i = 1
    while (i <= 100) {
        suma += i
        i++
    }
    println("La suma de 1 a 100 es $suma")
}
