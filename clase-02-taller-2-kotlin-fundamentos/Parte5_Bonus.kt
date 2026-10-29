/**
 * TALLER 2: Fundamentos de Kotlin (Clase 02)
 * Asignatura: Programación para Dispositivos Móviles
 * Estudiante: Manuel Felipe Castellanos Calderón · Código: 52625
 */

fun interpretar(entrada: String?) {
    val edad = entrada?.toIntOrNull()
    if (edad == null) println("'$entrada' -> Entrada inválida: debes escribir un número")
    else println("'$entrada' -> Tu edad es $edad")
}

fun main() {
    // Las 3 pruebas del enunciado, sin depender de teclado:
    interpretar("25")    // número válido
    interpretar("abc")   // texto
    interpretar(null)    // nada (readlnOrNull() devuelve null en EOF)
    interpretar("")      // Enter sin escribir
}
