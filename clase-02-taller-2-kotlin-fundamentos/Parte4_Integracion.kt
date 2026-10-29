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

fun resumen(notas: List<Int>) {
    if (notas.isEmpty()) {
        println("No hay notas registradas")
        return
    }
    for (nota in notas) println("Nota $nota -> ${letraDe(nota)}")

    // toDouble() ANTES de dividir: sum()/size entre Int daría 83
    val promedio = notas.sum().toDouble() / notas.size
    println("Promedio: $promedio")
    println("Estado: ${if (promedio >= 70) "Aprobado" else "Reprobado"}")
}

fun main() {
    resumen(listOf(85, 92, 67, 78, 95))
    println("--- lista vacía ---")
    resumen(emptyList())
}
