# RESPUESTAS — Taller Evaluativo Kotlin (Clase 03)

**Estudiante:** Manuel Felipe Castellanos Calderón · **Código:** 52625

> El cuestionario oficial de 15 preguntas está en `Taller_Evaluativo_Kotlin_ABWC_Unit1-2.docx`, que no estaba entre los archivos de la clase. Respondo las 15 preguntas por las cinco áreas que lista el taller (3 por área); si el docx trae otro enunciado, hay que ajustar la redacción.

## Básicos
1. **¿Diferencia entre `val` y `var`?** `val` es de solo lectura: se asigna una vez y el compilador rechaza reasignarla. `var` se puede reasignar. Se usa `val` por defecto y `var` solo cuando el valor realmente cambia (como `stock` en `Producto`).
2. **¿Qué es la inferencia de tipos y cuándo conviene declarar el tipo?** Kotlin deduce el tipo del valor (`val edad = 21` es `Int`). Conviene el tipo explícito en parámetros y retornos de funciones públicas, y cuando quiero otro tipo (`val x: Long = 1`).
3. **¿Qué son las plantillas de cadena?** Insertar valores dentro de un `String` con `$variable` o `${expresion}`: `"$nombre tiene $edad años"`. Con formato: `"%.2f".format(Locale.US, imc)`.

## Condicionales
4. **¿`if` como expresión?** En Kotlin `if` devuelve un valor: `val envio = if (subtotal >= 150_000) 0 else 12_000`. Por eso exige `else` cuando se usa así.
5. **¿Para qué sirve `when`?** Reemplaza cadenas de `if/else if`. Admite rangos (`in 90..100`), tipos y condiciones libres (`imc < 18.5 ->`). Como expresión debe ser exhaustivo.
6. **¿Por qué el orden de las ramas importa en `clasificarIMC`?** Porque se evalúan de arriba hacia abajo y gana la primera que se cumpla; si pusiera `imc < 30.0` primero, un IMC de 20 caería ahí y nunca llegaría a "Normal".

## Nulabilidad
7. **¿Qué diferencia hay entre `String` y `String?`?** `String` nunca es null; `String?` puede serlo, y el compilador no deja usar sus miembros hasta tratar el null.
8. **¿Qué hacen `?.`, `?:` y `let`?** `?.` llama solo si no es null; `?:` da un valor por defecto si es null; `cupon?.let { ... }` ejecuta el bloque solo cuando hay valor, con `it` ya sin null.
9. **¿Por qué no usar `!!`?** Convierte el chequeo de compilación en un crash en ejecución (`NullPointerException`) si el valor era null. En este taller además penaliza −10 puntos. Alternativas: `?:`, `let`, `requireNotNull`.

## POO
10. **¿Qué genera una `data class`?** `equals`, `hashCode`, `toString`, `copy` y `componentN` automáticamente a partir del constructor primario.
11. **¿Para qué sirve `copy()`?** Crear una copia cambiando solo algunas propiedades sin tocar el original: `original.copy(precio = 95_000)`. El original conserva 120.000.
12. **¿Qué es una `sealed class` y cuándo usarla?** Una jerarquía cerrada: todas las subclases se conocen en compilación. Sirve para modelar estados finitos (`Cargando/Exito/Error`) y que `when` sea exhaustivo sin `else`.
    ```kotlin
    sealed class Estado {
        object Cargando : Estado()
        data class Exito(val datos: List<String>) : Estado()
        data class Error(val mensaje: String) : Estado()
    }
    ```

## Lambdas
13. **¿Qué es `it`?** El nombre implícito del único parámetro de una lambda: `filtrarProductos(catalogo) { it.disponible }`.
14. **¿Qué es una función de orden superior?** Una función que recibe otra función como parámetro o la devuelve, como `filtrarProductos(productos, criterio: (Producto) -> Boolean)`.
15. **¿Qué es una extension function?** Una función que "agrega" un método a una clase existente sin heredarla: `fun <T> List<T>.contarSi(criterio: (T) -> Boolean): Int`, o `Producto.vender(...)` en la Actividad 3.
