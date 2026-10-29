// Actividad 4 — Filtro flexible
// Estudiante: Manuel Felipe Castellanos Calderón · Código: 52625

data class Producto(val nombre: String, val precio: Int, val disponible: Boolean)

val catalogo = listOf(
    Producto("Teclado", 120_000, true),
    Producto("Mouse", 45_000, true),
    Producto("Monitor", 850_000, false),
    Producto("Cable USB", 15_000, true),
    Producto("Webcam", 95_000, false)
)

fun filtrarProductos(
    productos: List<Producto>,
    criterio: (Producto) -> Boolean
): List<Producto> = productos.filter(criterio)

fun <T> List<T>.contarSi(criterio: (T) -> Boolean): Int = count(criterio)

fun main() {
    val disponibles = filtrarProductos(catalogo) { it.disponible }
    val economicos = filtrarProductos(catalogo) { it.precio < 100_000 }
    val caros = filtrarProductos(catalogo) { it.precio >= 100_000 }

    println("Disponibles: ${disponibles.map { it.nombre }}")
    println("Económicos:  ${economicos.map { it.nombre }}")
    println("Caros:       ${caros.map { it.nombre }}")

    val ofertas = filtrarProductos(catalogo) { it.disponible && it.precio < 100_000 }
    println("Ofertas: ${ofertas.map { it.nombre }}")

    println("Disponibles: ${catalogo.contarSi { it.disponible }}")
    println("Baratos:     ${catalogo.contarSi { it.precio < 50_000 }}")
}
