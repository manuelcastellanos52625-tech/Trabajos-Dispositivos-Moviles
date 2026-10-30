package com.example.unscramble.ui

/**
 * Estado inmutable de la pantalla del juego. Se actualiza solo desde el [GameViewModel]
 * con copy(), así la UI siempre refleja un estado coherente.
 */
data class GameUiState(
    val currentScrambledWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)
