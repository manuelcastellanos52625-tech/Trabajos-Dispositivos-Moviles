# Análisis — Unscramble: ViewModel y State en Compose

**Estudiante:** Manuel Felipe Castellanos Calderón · **Código:** 52625
Las capturas de `capturas/` son reales, del emulador Pixel 9 Pro con esta app.

## ¿Qué problema resuelve el ViewModel?
Antes, el estado (palabra, puntaje, contador) viviría en Composables con `remember`. Al rotar la pantalla la Activity se destruye y se recrea, `remember` se reinicia y el juego empezaría de cero. Con el estado en un `ViewModel` este sobrevive a los cambios de configuración.

**Evidencia:** en `04_rotacion_estado_conservado.png` la partida sigue en "2 de 10 palabras" tras rotar. El Logcat confirma que no se eligió una palabra nueva al rotar: se ve `river` (inicio), `friend` (tras acertar) y la siguiente (`pillow`) solo aparece cuando yo pulso Skip.

## Arquitectura usada
- **`GameUiState`:** `data class` inmutable. La UI nunca lo modifica; solo lo lee.
- **`GameViewModel`:** guarda el estado en un `MutableStateFlow` privado (`_uiState`) y lo expone como `StateFlow` de solo lectura (`uiState`): propiedad de respaldo, para que ninguna otra clase pueda cambiarlo.
- **Flujo unidireccional (UDF):** el estado baja del ViewModel a la UI y los eventos suben (`onUserGuessChanged`, `onKeyboardDone`, `skipWord()`), por eso los Composables son "tontos" y fáciles de probar.
- **`collectAsState()`:** convierte el `StateFlow` en un `State` de Compose; cada `update { copy(...) }` provoca una recomposición.

## Lógica del juego
- `pickRandomWordAndShuffle()` elige una palabra no usada (`usedWords`) y la mezcla; `shuffleCurrentWord()` repite la mezcla si queda igual a la original.
- `checkUserGuess()`: si acierta suma `SCORE_INCREASE` (20) y pasa de ronda; si falla pone `isGuessedWordWrong = true` y el campo muestra "Wrong Guess!" en rojo (`02_palabra_incorrecta.png`). En ambos casos limpia el campo.
- `updateGameState()`: cuando `usedWords.size == MAX_NO_OF_WORDS` pone `isGameOver = true` y no elige otra palabra; si no, avanza el contador de palabras.
- `FinalScoreDialog` (`06_fin_del_juego.png`): muestra el puntaje y ofrece "Play Again" (`resetGame()`) o "Exit".

## Pruebas
`GameViewModelTest` (6 pruebas JVM, todas pasan): estado inicial, respuesta incorrecta, skip, fin del juego a las 10 palabras, reinicio y constante de puntaje. Para que corran sin Android se activó `unitTests.isReturnDefaultValues = true` (por el `Log.d` del ViewModel).

## Diferencias con el codelab
- El codelab no trae `MainActivity`, strings ni la lista completa de palabras; usé una lista propia de más de 100 palabras y las cadenas de la app de partida.
- Agregué un `Log.d` con la palabra correcta solo para poder probar el acierto en el emulador.
- El texto "N de 10 palabras" quedó en español; el resto de la interfaz está en inglés como en el codelab.
