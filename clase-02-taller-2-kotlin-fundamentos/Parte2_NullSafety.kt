/**
 * TALLER 2: Fundamentos de Kotlin (Clase 02)
 * Asignatura: Programación para Dispositivos Móviles
 * Estudiante: Manuel Felipe Castellanos Calderón · Código: 52625
 */

fun main() {
    // Paso 2.1: val longitud = apodo.length no compila (ver salidas/error_nullable.txt)
    val apodo: String? = null

    // Paso 2.2: safe call + Elvis
    val longitud = apodo?.length ?: 0
    println("Longitud: $longitud")

    // Paso 2.3: !! con null lanza NullPointerException
    try {
        val boom = apodo!!.length
        println(boom)
    } catch (ex: NullPointerException) {
        println("Crash con !!: ${ex::class.simpleName}")
    }

    // Paso 2.5: patrón let, con null y con valor real
    for (correo in listOf<String?>(null, "ana@ucaldas.edu.co")) {
        correo?.let {
            println("El correo '$it' tiene ${it.length} caracteres")
        } ?: println("No hay correo registrado")
    }
}
