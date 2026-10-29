// Actividad 1 — Perfil y métricas
// Estudiante: Manuel Felipe Castellanos Calderón · Código: 52625

fun calcularIMC(pesoKg: Double, estaturaM: Double): Double {
    return pesoKg / (estaturaM * estaturaM)
}

// Locale.US fuerza el punto decimal (con el idioma del PC en español Colombia salía "22,14")
fun presentacion(nombre: String, edad: Int, imc: Double): String =
    "$nombre tiene $edad años y un IMC de ${"%.2f".format(java.util.Locale.US, imc)}"

fun clasificarIMC(imc: Double): String = when {
    imc < 18.5 -> "Bajo peso"
    imc < 25.0 -> "Normal"
    imc < 30.0 -> "Sobrepeso"
    else -> "Obesidad"
}

fun main() {
    val nombre = "Ana Gómez"
    val edad = 21
    val estaturaM = 1.68
    val pesoKg = 62.5

    val imc = calcularIMC(pesoKg, estaturaM)
    println(presentacion(nombre, edad, imc))
    println("Categoría: ${clasificarIMC(imc)}")

    // Checkpoint 1.4
    println("IMC 17.0 -> ${clasificarIMC(17.0)}")
    println("IMC 31.0 -> ${clasificarIMC(31.0)}")
}
