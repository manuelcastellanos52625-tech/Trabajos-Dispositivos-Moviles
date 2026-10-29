fun main() {
    val nota = 85
    val letra = when (nota) {
        in 90..100 -> "A"
        in 80..89 -> "B"
    }
    println(letra)
}
