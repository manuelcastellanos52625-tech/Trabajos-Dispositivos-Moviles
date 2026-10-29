# Análisis — Taller 2 (Clase 02): Fundamentos de Kotlin

**Estudiante:** Manuel Felipe Castellanos Calderón · **Código:** 52625

Todo el código está en los archivos `Parte*.kt`. Las salidas y los errores de compilador que se citan abajo son reales: los generé compilando con `kotlinc` y están guardados en `salidas/`.

## Parte 1 — Justificación de val/var

- `nombre`: `val`, porque el nombre no cambia durante la ejecución.
- `edad`: `var`, porque en el Paso 1.3 se incrementa en 1.
- `estatura`: `val`, se lee pero nunca se reasigna.
- `esEstudiante`: `val`, es un dato fijo (`true`).
- `inicial`: `val`, se calcula una vez a partir del nombre.

### Error al reasignar un `val` (Paso 1.3)
```text
reasignar_val.kt:3:5: error: 'val' cannot be reassigned.
    nombre = "Otro"
```
(`salidas/error_reasignar_val.txt`)

### Tipos inferidos (Paso 1.4)
| Línea | Tipo inferido |
|---|---|
| `val a = 10` | `Int` |
| `val b = 10L` | `Long` |
| `val c = 10.0` | `Double` |
| `val d = 10.0f` | `Float` |
| `val e = "10"` | `String` |

### Conversiones (Paso 1.5)
```text
Estatura en cm: 175
Edad como texto: 21
NumberFormatException: For input string: "abc"
```
Ojo: `(1.75 * 100).toInt()` dio 175. Con otros valores (por ejemplo 1.13 * 100 = 112.99999999999999) `toInt()` trunca y puede dar 112; para redondear se usa `roundToInt()`.

## Parte 2 — Null safety

### Error de compilación (Paso 2.1)
```text
nullable.kt:3:25: error: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?'.
    val longitud = apodo.length
```

### ¿Por qué `apodo.length` no compila?
`String` y `String?` son tipos distintos. Una variable `String` garantiza que nunca es null, así que se puede llamar `.length` sin riesgo. `String?` puede ser null, y llamar `.length` sobre null sería un NullPointerException. Kotlin mueve ese riesgo del momento de ejecución al de compilación: no me deja llamar el miembro hasta que yo trate el caso null.

### ¿Qué hace `?.`?
Es la llamada segura: si el objeto es null, toda la expresión da null sin ejecutar la llamada; si no es null, ejecuta la llamada normal. `apodo?.length` es `Int?`.

### ¿Qué hace `?:`?
Es el operador Elvis: si lo de la izquierda es null, usa el valor de la derecha. `apodo?.length ?: 0` convierte el `Int?` en un `Int` con 0 como respaldo.

### ¿Por qué `!!` es peligroso?
`!!` le dice al compilador "confía en mí, no es null" y se salta la verificación. Si me equivoco, el programa lanza `NullPointerException` en ejecución (salida real: `Crash con !!: NullPointerException`). Es justo el crash que Kotlin intenta evitar con el sistema de tipos, y en Android significa que la app se cierra delante del usuario.

### ¿Cuándo usaría `!!` legítimamente?
Cuando el valor es null solo por una limitación de la API y yo ya lo comprobé: por ejemplo, justo después de `if (x == null) error(...)` en una propiedad `var` que el compilador no puede promover a no-null (smart cast imposible). Aun así preferiría `requireNotNull(x) { "mensaje" }` porque deja un mensaje claro.

### Patrón `let` (Paso 2.5)
```text
No hay correo registrado
El correo 'ana@ucaldas.edu.co' tiene 18 caracteres
```
Con `null` entra al `?: println(...)`; con el valor real ejecuta el bloque del `let`, donde `it` ya es `String` no nulo.

## Parte 3 — Control de flujo

### Sin `else` en `when` (Paso 3.1)
```text
when_sin_else.kt:3:17: error: 'when' expression must be exhaustive. Add an 'else' branch.
```

### ¿Diferencia entre `while` y `do-while`?
`while` evalúa la condición **antes** de cada vuelta, así que puede no ejecutarse nunca. `do-while` ejecuta el cuerpo primero y evalúa después, así que se ejecuta **al menos una vez**. La diferencia importa cuando necesito una primera acción obligatoria, por ejemplo pedir un dato al usuario y repetir mientras sea inválido: con `var x = 10; while (x < 5) {…}` no entra nunca, con `do {…} while (x < 5)` entra una vez.

### ¿Por qué `when` exige `else` como expresión?
Una expresión siempre tiene que producir un valor del tipo declarado. Si ningún caso coincide y no hay `else`, la variable quedaría sin valor, lo cual rompe la seguridad de tipos. Por eso el compilador obliga a cubrir todos los casos (o a usar un `else`; con `enum` o `sealed class` basta con listar todos los casos).

### ¿`1..5` vs `1 until 5`?
`1..5` incluye el 5 (1,2,3,4,5). `1 until 5` excluye el final (1,2,3,4). `until` es útil para recorrer índices: `for (i in 0 until lista.size)`.

Resultados reales: tabla del 7 (10 líneas), conteo regresivo terminando en `¡Despegue!` y suma de 1 a 100 = **5050** (`salidas/Parte3_ControlFlujo.txt`).

## Parte 4 — Integración

Salida real:
```text
Promedio: 83.4
Estado: Aprobado
--- lista vacía ---
No hay notas registradas
```
El promedio es 83.4 y no 83 porque convierto `notas.sum()` a `Double` **antes** de dividir; entre dos `Int` la división es entera y trunca los decimales. La lista vacía se maneja con un `return` temprano (evita dividir entre cero).

## Parte 5 — Bonus: entrada segura

Salidas de las 3 pruebas del enunciado (simuladas llamando a la función con cada valor, porque `readlnOrNull()` depende del teclado):
```text
'25'   -> Tu edad es 25
'abc'  -> Entrada inválida: debes escribir un número
'null' -> Entrada inválida: debes escribir un número   (Enter sin escribir en consola/EOF)
```

### ¿Por qué `toIntOrNull()` es mejor que `toInt()` para datos del usuario?
`toInt()` lanza `NumberFormatException` con cualquier texto no numérico y tumba el programa (lo vimos con `"abc".toInt()`). `toIntOrNull()` devuelve `null`, que puedo manejar con `?:` o un `if`, sin excepciones.

### ¿Qué pasa si el usuario no escribe nada?
`readlnOrNull()` puede devolver null (fin de entrada) o un string vacío; en ambos casos `toIntOrNull()` da null y el programa muestra el mensaje de entrada inválida, sin fallar.
