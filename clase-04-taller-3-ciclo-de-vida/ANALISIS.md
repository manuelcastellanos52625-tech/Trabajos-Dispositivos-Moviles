# Análisis — Taller 3 (Clase 04): Sobrevive al ciclo de vida

**Estudiante:** Manuel Felipe Castellanos Calderón · **Código:** 52625
**Proyecto:** `CicloDeVidaDemo` (Views XML, `ComponentActivity`). Los logs que cito son reales: salieron del emulador Pixel 9 Pro y están completos en `logcat/`.

## Predicciones iniciales y veredicto

1. **"No conservar actividades" + minimizar: ¿qué callbacks? ¿Igual que rotar?**
   Predicción: la Activity se destruye y se recrea al volver (`onDestroy → onCreate`), pero a diferencia de rotar, el proceso de la app sigue vivo.
   Veredicto: **no pude comprobarlo así**. La opción de desarrollador no se activó desde `adb` (`settings put global always_finish_activities 1` no destruyó la Activity: en el log solo hubo `onStop → onRestart`). Probé el caso más fuerte, la muerte real del proceso con `adb shell am kill` (ver Parte 4), y ahí sí se recreó todo, incluso el proceso.
2. **Contador en `companion object`: ¿sobrevive a rotación? ¿a que maten el proceso?**
   Predicción: sobrevive a la rotación (el proceso sigue y la variable estática vive en la clase, no en la Activity) pero **no** a la muerte del proceso (la memoria se pierde y vuelve a 0).
   Veredicto: razonamiento correcto, pero **no lo ejecuté** (en mi proyecto la variable estática ya está corregida; ver Parte 2).
3. **A → B → C y "Atrás" tres veces: ¿orden de destrucción?**
   Predicción: C, luego B, luego A (la pila es LIFO).
   Veredicto: **confirmado** en el tramo C → B → A (log 05): `ActivityC.onDestroy`, luego `ActivityB.onDestroy`, y A queda viva. La salida de A se ve en el log 04: `ActivityA.onDestroy (isFinishing=true)`.

## Parte 1 — Manifest y configuración

- **Nombre completo de la Activity principal:** `com.ucaldas.ciclodevida.ActivityA`. En el manifest se escribe `.ActivityA` y Android la completa con el `namespace` de Gradle.
- **¿Qué intent-filter la marca como inicio?** `action MAIN` + `category LAUNCHER`. MAIN dice "punto de entrada" y LAUNCHER "muéstrame en el lanzador de apps".
- **¿Dos Activities con LAUNCHER?** Ambas aparecen como iconos separados en el lanzador y cada icono abre su Activity. No es un error, pero la app deja de tener una única pantalla de inicio.
- **`minSdk` / `targetSdk` / `compileSdk`:** `minSdk = 24` es la versión mínima en la que se puede instalar; `compileSdk = 37` es contra qué API compila el código (qué clases y métodos puedo llamar); `targetSdk = 37` es la versión para la que declaro que probé la app: el sistema activa los comportamientos nuevos de esa versión (por eso la app va de borde a borde y necesité `fitsSystemWindows`).
- **Subir el `targetSdk` sin probar:** puede romper comportamientos (permisos, ejecución en segundo plano, edge-to-edge). Con Android 12+ además, toda Activity con `intent-filter` exige `android:exported` explícito o la app ni se instala; por eso A tiene `exported="true"` y B/C `exported="false"`.
- **`applicationId` vs `package`:** `applicationId` (Gradle) es la identidad de la app en el dispositivo y en Play Store; `namespace`/`package` de los `.kt` es solo el espacio de nombres del código y genera la clase `R`. Pueden ser distintos: puedo renombrar paquetes sin cambiar la identidad de una app ya publicada, o sacar varias variantes (`applicationIdSuffix`) del mismo código. Aquí coinciden: `com.ucaldas.ciclodevida`.

## Parte 2 — Instrumentación y cacería de bugs

`ActivityConLog` (clase base) sobrescribe `onCreate, onStart, onResume, onPause, onStop, onRestart, onDestroy` y loguea el nombre de la clase y el `hashCode()`. Las tres Activities heredan de ella, así que las tres registran los 7 callbacks sin repetir código.

### El bug
```kotlin
companion object {
    var instanciaActual: MainActivity? = null
    var contadorGlobal = 0
}
```
- **(a) Cuál es:** guarda una referencia a la Activity y estado mutable en un campo estático.
- **(b) Por qué es peligroso:**
  - *Fuga de memoria:* el campo estático vive tanto como el proceso. Al rotar, el sistema destruye la Activity y crea otra, pero `instanciaActual` sigue apuntando a la vieja y el recolector de basura no puede liberarla (ni su árbol de vistas ni su `Context`).
  - *Estado engañoso:* `contadorGlobal` sobrevive a las rotaciones (el proceso sigue), así que parece que "guarda" el estado, pero vuelve a 0 cuando el sistema mata el proceso. Funciona en las pruebas y falla en producción. Además cuenta creaciones de Activity, no acciones del usuario, y crece con cada rotación.
- **(c) Cómo lo corregí:** en mi `ActivityA` no hay referencias a la Activity ni estado en un `companion object` (solo la constante `CLAVE_CONTADOR`). El estado vive en una propiedad de la instancia y se persiste con `onSaveInstanceState`. Para datos que deban durar más, un `ViewModel` o almacenamiento persistente.
- *Nota honesta:* no ejecuté la versión con el bug; esta parte es razonamiento, no captura.

### Secuencia base (Paso 2.3) — logs reales
| Acción | Callbacks vistos (`logcat/`) | Instancia |
|---|---|---|
| Abrir (arranque en frío) | `MiApp.onCreate → onCreate → onStart → onResume` | 181162150 |
| Home | `onPause → onStop` (+ `onSaveInstanceState`, que en Android moderno se llama tras `onStop`) | misma |
| Volver | `onRestart → onStart → onResume` | **misma** (181162150) |
| Rotar | `onPause → onStop → onSaveInstanceState → onDestroy (isChangingConfigurations=true) → onCreate (con datos) → onStart → onResume` | **cambia**: 181162150 → 250086231 |
| Atrás | `onPause → onStop → onDestroy (isFinishing=true)` | — |

El `hashCode()` cambia en la rotación y se mantiene en Home → volver, como pide el checkpoint.

### "No conservar actividades" vs. rotación (Paso 2.4)
Una rotación destruye y recrea la **Activity**, pero el **proceso** sigue vivo (mismo `MiApp`, sin nuevo `MiApp.onCreate`); es un cambio de configuración. "No conservar actividades" simula que el sistema destruye la Activity al salir de ella por falta de memoria, con el proceso aún vivo. En una muerte real el proceso también desaparece: se pierden las variables estáticas, los singletons, las cachés y los hilos, y al volver el sistema arranca un proceso nuevo (`MiApp.onCreate` otra vez) y reconstruye la Activity con el `Bundle` guardado. Eso es lo que reproduje con `am kill` en la Parte 4.

## Parte 3 — Cadena A → B → C con ida y vuelta

- A → B: `putExtra("nombre_usuario", "Ana")`; B registra `ActivityB recibió nombre_usuario=Ana`.
- B → C: `putExtra("contador", 10)`; C registra `ActivityC recibió contador=10`.
- C: botones +/− (subí a 12) y "Guardar y volver" llama `setResult(RESULT_OK, intent)` + `finish()`.
- B recibe con `registerForActivityResult(StartActivityForResult())`: `ActivityB recibió de vuelta: 12` (captura `captura_B_recibe_12.png`). No hay `!!`: uso `?:` con valores por defecto.

### Secuencia A → B → C → (resultado) → B → (Atrás) → A (`logcat/05_cadena_A_B_C_B_A.txt`)
| Momento | A | B | C |
|---|---|---|---|
| Abro B | `onPause → onStop` → **en pila, no visible** | `onCreate → onStart → onResume` | — |
| Abro C | en pila (detenida) | `onPause → onStop` → **en pila, no visible** | `onCreate → onStart → onResume` |
| Guardar y volver | en pila | `onRestart → onStart → onResume` (recibe 12) | `onPause → onStop → onDestroy` → **destruida** |
| Atrás desde B | `onRestart → onStart → onResume` (visible otra vez) | `onPause → onStop → onDestroy` → **destruida** | — |

A nunca se destruyó durante la cadena: quedó en la pila solo detenida. Una Activity pasa a "destruida" cuando termina (`finish()`/Atrás), no cuando otra la tapa.

## Parte 4 — Estado: rotación y muerte de proceso

- **4.1 Rotación:** subí el contador a 3 y roté: `onSaveInstanceState - guardando contador=3` → `onDestroy` → `onCreate (con datos)`; la pantalla mantiene 3 (`captura_rotado_contador_sigue_en_3.png`).
- **4.2 / 4.3 Muerte del proceso:** como la opción de desarrollador no se activó por `adb`, usé `adb shell am kill` con la app en segundo plano (contador en 5; `pidof` devolvió vacío, proceso muerto). Al reabrirla desde Recientes el log mostró `MiApp.onCreate - el PROCESO arrancó` y `ActivityA.onCreate (savedInstanceState con datos)`, y la pantalla mostró **Contador de A: 5** (`captura_muerte_proceso_despues.png`). El sistema llamó a `onSaveInstanceState` antes de que el proceso muriera, por eso el contador sobrevive. No hubo nada que corregir.
- **4.4 ¿Por qué `onSaveInstanceState` no reemplaza una base de datos o `SharedPreferences`?** El `Bundle` vive en memoria del sistema y sirve para restaurar la *UI transitoria* de una tarea que el sistema interrumpió. Solo dura mientras la tarea siga en Recientes. Cuando el usuario cierra con "Atrás" (`isFinishing=true`) el sistema da por terminada la pantalla y **descarta el `Bundle`**: en mi log 04 `onDestroy` salió con `isFinishing=true`, y la siguiente apertura llega con `savedInstanceState == null` y el contador en 0. Tampoco sobrevive a "Forzar detención" ni a reiniciar el teléfono. Además es pequeño (límite de ~1 MB en transacciones) y no es fuente de verdad. Lo que deba persistir va a una base de datos, `SharedPreferences`/DataStore o archivos.

## Parte 5 — Bonus: `Application` propia

`MiApp.onCreate` está declarada en el manifest (`android:name=".MiApp"`). En el arranque en frío (`logcat/01_abrir_cold_start.txt`) el log `MiApp.onCreate - el PROCESO arrancó` sale **antes** del `onCreate` de la Activity (20:29:38.983 vs 20:29:39.077).

**Cold vs warm start en el Logcat:** en un *cold start* (proceso nuevo) aparece `MiApp.onCreate` seguido de `ActivityA.onCreate`; en un *warm start* (proceso vivo, solo se recrea o reanuda la Activity) **no aparece** `MiApp.onCreate`. Ejemplos reales: tras `am kill` salió `MiApp.onCreate` (cold); al volver desde Home sin matar el proceso (`logcat/07_warm_start.txt`) solo hubo `onRestart → onStart → onResume`. La rotación también es warm start porque el proceso no muere.
