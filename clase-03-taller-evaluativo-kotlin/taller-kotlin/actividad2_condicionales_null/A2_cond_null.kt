// Actividad 2 — Tarifa de envío segura (sin aserciones de no-nulo)
// Estudiante: Manuel Felipe Castellanos Calderón · Código: 52625

fun costoEnvioBase(subtotal: Int): Int =
    if (subtotal >= 150_000) 0 else 12_000

fun costoEnvioConCupon(subtotal: Int, cupon: String?): Int {
    val base = costoEnvioBase(subtotal)
    val esGratis = cupon?.uppercase() == "ENVIOGRATIS"
    return if (esGratis) 0 else base
}

fun descuentoPremium(subtotal: Int, esPremium: Boolean): Int =
    if (esPremium) (subtotal * 0.10).toInt() else 0

fun resumen(subtotal: Int, cupon: String?, esClientePremium: Boolean) {
    val envio = costoEnvioConCupon(subtotal, cupon)
    val descuento = descuentoPremium(subtotal, esClientePremium)
    val total = subtotal - descuento + envio

    cupon?.let { println("Cupón aplicado: $it") }
    println("Subtotal:  $subtotal")
    println("Descuento: $descuento")
    println("Envío:     $envio")
    println("TOTAL:     $total")
}

fun main() {
    println("--- con cupón ENVIOGRATIS ---")
    resumen(180_000, "ENVIOGRATIS", false)

    println("--- cupón null (no debe fallar) ---")
    resumen(180_000, null, false)

    println("--- checkpoints ---")
    println("envío base con 100000 = ${costoEnvioBase(100_000)}")           // 12000
    println("envío sin cupón, 100000 = ${costoEnvioConCupon(100_000, null)}") // 12000
    println("envío con cupón, 100000 = ${costoEnvioConCupon(100_000, "enviogratis")}") // 0
    println("descuento premium 180000 = ${descuentoPremium(180_000, true)}")  // 18000
}
