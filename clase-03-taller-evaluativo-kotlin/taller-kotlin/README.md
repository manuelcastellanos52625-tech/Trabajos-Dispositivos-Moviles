# Mini-retro — Taller Evaluativo Kotlin (Clase 03)

**Estudiante:** Manuel Felipe Castellanos Calderón · **Código:** 52625

Las salidas reales de cada actividad están en `salidas/`.

## 1. Dificultades
Lo que más me costó fue un detalle que no estaba en el enunciado: al correr la Actividad 1, `"%.2f".format(imc)` imprimió `22,14` en vez de `22.14`, porque el computador está configurado en español de Colombia y `format` usa la coma decimal de ese idioma. Lo resolví pasando `Locale.US` como primer argumento. También tuve que cuidar que `vender` validara la cantidad antes de tocar el stock, para que una venta rechazada no dejara el inventario a medias (el Mouse tenía que seguir en 30).

## 2. Aplicación en Android real
Usaría `sealed class` para el estado de una pantalla que carga datos: `Cargando`, `Exito(datos)` y `Error(mensaje)`. Con un `when` sin `else` el compilador me obliga a pintar los tres casos en la UI, y si mañana agrego un cuarto estado el código no compila hasta que lo maneje. Las lambdas de la Actividad 4 son el mismo patrón de `onClick = { ... }` de Compose.

## 3. Tres aprendizajes clave
1. `?.`, `?:` y `let` resuelven la nulabilidad sin `!!`, y el programa sigue funcionando aunque el cupón sea `null`.
2. Una `data class` con `copy()` permite crear variantes sin modificar el original (el precio del Teclado original siguió en 120.000).
3. Una función de orden superior recibe el criterio como lambda, así un solo `filtrarProductos` sirve para cualquier filtro.
