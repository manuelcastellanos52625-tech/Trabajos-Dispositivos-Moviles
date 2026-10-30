# Unscramble — ViewModel y State en Compose

Codelab de Android Basics con Compose (Unidad 4, ruta 1): [ViewModel y State en Compose](https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state).

Juego de una palabra desordenada por ronda: el jugador la adivina (20 puntos por acierto) o la omite. Son 10 palabras por partida y al final aparece un diálogo con el puntaje.

**Estudiante:** Manuel Felipe Castellanos Calderón · **Código:** 52625

## Qué se implementó (por pasos del codelab)

| Paso | Archivo | Qué hace |
|---|---|---|
| Datos | `data/WordsData.kt` | `MAX_NO_OF_WORDS = 10`, `SCORE_INCREASE = 20` y el conjunto `allWords` |
| Estado | `ui/GameUiState.kt` | `data class` inmutable: palabra desordenada, número de palabra, puntaje, `isGuessedWordWrong`, `isGameOver` |
| ViewModel | `ui/GameViewModel.kt` | `StateFlow` con propiedad de respaldo, `userGuess`, elegir y mezclar palabra sin repetir, `checkUserGuess()`, `skipWord()`, `resetGame()` |
| UI | `ui/GameScreen.kt` | `collectAsState()`, flujo unidireccional, error en el campo, `FinalScoreDialog` |
| Pruebas | `GameViewModelTest.kt` | 6 pruebas unitarias del ViewModel (todas pasan) |

## Flujo de datos unidireccional

El estado baja del `ViewModel` a la UI (`uiState`, `userGuess`) y los eventos suben como lambdas (`onUserGuessChanged`, `onKeyboardDone`, `skipWord`). Los Composables no modifican el estado directamente.

## Evidencias

Capturas reales del emulador en [capturas/](capturas); explicación en [ANALISIS.md](ANALISIS.md).

## Nota

El `Log.d("Unscramble", "Palabra correcta: …")` del ViewModel no es parte del codelab: lo agregué para poder probar el acierto en el emulador.
