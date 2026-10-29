# Análisis — Taller 4 (Clase 05): Perfil de usuario en Jetpack Compose

**Estudiante:** Manuel Felipe Castellanos Calderón · **Código:** 52625
Las capturas (`capturas/`) y el log (`capturas/logcat_bug_lista.txt`) son reales, tomadas en el emulador Pixel 9 Pro con la app de este proyecto.

## 1. Predicción inicial (escrita antes de programar)

1. **`remember { mutableStateOf(0) }` y rotar:** se pierde. `remember` guarda el valor en la composición; al rotar la Activity se destruye y se recrea, la composición empieza de cero y el contador vuelve a 0.
2. **`remember { mutableListOf(...) }` + `.add()`:** la pantalla no se actualiza. `mutableListOf` es una lista normal; Compose solo se entera de cambios en objetos de estado (`State`, `SnapshotStateList`).
3. **`remember` vs `rememberSaveable`:** `remember` sobrevive a recomposiciones pero no a la recreación de la Activity; `rememberSaveable` además guarda el valor en el `Bundle` de estado, así que sobrevive a rotación y a muerte de proceso.

## 2. Verificación de la predicción

Las tres se cumplieron:
1. Con `remember`, subí el contador a 5, roté y volvió a **0** (`04a_antes_de_rotar_remember.png` → `04_rotacion_remember.png`).
2. Con `mutableListOf`, pulsé "Agregar logro" tres veces y la pantalla no cambió (`02_bug_antes.png`).
3. Con `rememberSaveable` el contador siguió en **5** tras rotar (`05a_antes_de_rotar_saveable.png` → `05_rotacion_saveable.png`).

Un detalle que no había previsto y que sí vi: tras rotar con la versión corregida, la lista de logros volvió a tener solo "Primer commit" (ver sección 4, limitación).

## 3. Diagnóstico del bug (Parte 2)

### Síntoma
Al presionar "Agregar logro", la lista no se actualiza en pantalla.

### ¿Por qué ocurre?
`mutableListOf` devuelve un `ArrayList` común y corriente. Compose solo recompone un Composable cuando cambia un objeto de estado que ese Composable leyó (`MutableState`, `SnapshotStateList`, …), porque esos objetos registran quién los lee. Un `ArrayList` no avisa a nadie cuando le hago `.add()`. `remember` solo conserva la misma instancia de la lista entre recomposiciones; no la vuelve observable. Resultado: la lista cambia, pero nadie le avisa a Compose y la UI queda congelada.

### Evidencia
Agregué `Log.d("Taller4", "Tamaño de la lista: ${logros.size}")` dentro del `onClick`. Con tres pulsaciones, Logcat mostró (`capturas/logcat_bug_lista.txt`):
```text
Tamaño de la lista: 2
Tamaño de la lista: 3
Tamaño de la lista: 4
```
El tamaño sube pero la pantalla seguía mostrando solo "Primer commit" (`02_bug_antes.png`). El problema no era la lista, sino que Compose no se enteró.

### Corrección aplicada
Opción A: `val logros = remember { mutableStateListOf("Primer commit") }`. La elegí porque permite seguir usando `.add()` y es lo más directo; la Opción B obliga a reasignar la lista en cada cambio.

### ¿Por qué funciona?
`mutableStateListOf` crea un `SnapshotStateList`: una lista observable integrada con el sistema de *snapshots* de Compose. Cuando un Composable la lee dentro de `forEach`, Compose registra esa lectura; cada `add`/`remove` marca la lista como modificada y recompone solo los Composables que la leyeron. Después de la corrección los logros aparecen al instante (`03_bug_despues.png`).

## 4. Experimento de rotación (Parte 3)

### Con `remember`
Contador en 5 → rotar → **0** (`04_rotacion_remember.png`). Pasa porque rotar es un cambio de configuración: la Activity pasa por `onDestroy`/`onCreate`, la composición se descarta y `remember` (que vive solo dentro de ella) se reinicia.

### Con `rememberSaveable`
Contador en 5 → rotar → **5** (`05_rotacion_saveable.png`).

### Explicación técnica
Como vimos en la Clase 04, al rotar el sistema llama `onSaveInstanceState`, destruye la Activity y la recrea con el `Bundle` guardado (`onCreate(savedInstanceState)`). `rememberSaveable` aprovecha ese mecanismo: guarda el valor en el `Bundle` de estado al destruirse y lo lee al recrearse. Solo funciona con tipos que el `Bundle` entiende (números, `String`, `Boolean`, `Parcelable`, …).

### Limitación conocida
`mutableStateListOf` no es un tipo que `rememberSaveable` sepa guardar por defecto, así que la lista de logros sigue con `remember` y **se reinicia al rotar** (tras rotar volvió a mostrar solo "Primer commit", ver `07_animacion_formulario.png`). Se arreglaría con un `Saver` personalizado o, mejor, con un `ViewModel` (Clase 08).

### Validación de nombre vacío
Edité el nombre, borré todo el texto y pulsé "Guardar": el encabezado muestra **"Usuario sin nombre"** y no queda vacío (`06_nombre_vacio.png`; `06a_campo_vacio.png` muestra el campo ya borrado). Efecto secundario: el avatar de iniciales muestra "US" porque toma las iniciales del texto por defecto; sería más limpio guardar el nombre vacío y calcular el texto solo al mostrar.

## 5. Justificación de los Modifier usados

1. **Avatar: `.size(96.dp).clip(CircleShape).background(...)`.** El orden importa porque cada modificador envuelve al siguiente: primero fijo el tamaño, luego recorto el área a un círculo y después pinto el fondo dentro de ese recorte. Si pusiera `background` antes de `clip`, el fondo se pintaría cuadrado y el recorte no se vería.
2. **Columna de la tarjeta: `.fillMaxWidth().padding(20.dp)` dentro de la `Card`.** El padding va adentro para que el color de la `Card` llegue hasta el borde y el espacio quede entre el borde y el texto. Si el `padding` estuviera en el `Modifier` de la `Card`, el color quedaría separado del borde de la pantalla y la tarjeta no usaría todo el ancho.
3. **Contenedor raíz: `.fillMaxSize().safeDrawingPadding().verticalScroll(...).padding(16.dp)`.** `safeDrawingPadding` respeta barra de estado y notch (con `targetSdk` 37 la app dibuja de borde a borde); `verticalScroll` permite desplazarse cuando en horizontal el contenido no cabe. Si el `padding(16.dp)` estuviera antes del `verticalScroll`, el espacio no se desplazaría con el contenido.

## 6. Dificultades encontradas

- Con `targetSdk` 37 la app dibuja de borde a borde, así que el contenido quedaría bajo la barra de estado; usé `safeDrawingPadding()`.
- En horizontal el contenido no cabe en la pantalla; por eso la columna tiene `verticalScroll` (se nota en `04_rotacion_remember.png`, donde la tarjeta queda cortada abajo).
- Para tener las capturas del "antes" compilé una variante temporal con `remember`/`mutableListOf`; el código entregado es el corregido.
- Una prueba de la validación de nombre vacío falló la primera vez (el botón Atrás sacó la app del primer plano) y la repetí.
