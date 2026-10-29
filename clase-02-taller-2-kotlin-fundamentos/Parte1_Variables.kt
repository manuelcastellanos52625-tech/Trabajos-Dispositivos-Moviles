/**
 * TALLER 2: Fundamentos de Kotlin (Clase 02)
 * Asignatura: Programación para Dispositivos Móviles
 * Estudiante: Manuel Felipe Castellanos Calderón · Código: 52625
 */

fun main() {
    // Paso 1.1: val para lo que no cambia, var solo para edad (se incrementa en 1.3)
    val nombre: String = "Manuel Felipe Castellanos Calderón"
    var edad: Int = 20
    val estatura: Double = 1.75
    val esEstudiante: Boolean = true
    val inicial: Char = nombre.first()

    println("$nombre ($inicial), $edad años, $estatura m, estudiante: $esEstudiante")

    // Paso 1.3: modificar y observar
    edad += 1
    println("Edad después de incrementar: $edad")
    // nombre = "Otro"   // <- descomentar: "Val cannot be reassigned"

    // Paso 1.4: inferencia de tipos
    val a = 10
    val b = 10L
    val c = 10.0
    val d = 10.0f
    val e = "10"
    println("a=${a::class.simpleName} b=${b::class.simpleName} c=${c::class.simpleName} d=${d::class.simpleName} e=${e::class.simpleName}")

    // Paso 1.5: conversiones
    val estaturaCm: Int = (estatura * 100).toInt()
    println("Estatura en cm: $estaturaCm")
    println("Edad como texto: " + edad.toString())

    try {
        "abc".toInt()
    } catch (ex: NumberFormatException) {
        println("NumberFormatException: ${ex.message}")
    }
}
