package com.example.unscramble.ui

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {

    @Test
    fun estadoInicial_empiezaEnPalabra1ConPuntajeCero() {
        val state = GameViewModel().uiState.value
        assertEquals(1, state.currentWordCount)
        assertEquals(0, state.score)
        assertFalse(state.isGameOver)
        assertTrue(state.currentScrambledWord.isNotEmpty())
    }

    @Test
    fun respuestaIncorrecta_marcaErrorYNoCambiaPuntaje() {
        val vm = GameViewModel()
        vm.updateUserGuess("zzzzzz")
        vm.checkUserGuess()
        val state = vm.uiState.value
        assertTrue(state.isGuessedWordWrong)
        assertEquals(0, state.score)
        assertEquals("", vm.userGuess)
    }

    @Test
    fun skip_avanzaDePalabraSinSumarPuntos() {
        val vm = GameViewModel()
        vm.skipWord()
        val state = vm.uiState.value
        assertEquals(2, state.currentWordCount)
        assertEquals(0, state.score)
    }

    @Test
    fun diezPalabras_terminanElJuego() {
        val vm = GameViewModel()
        repeat(MAX_NO_OF_WORDS) { vm.skipWord() }
        assertTrue(vm.uiState.value.isGameOver)
    }

    @Test
    fun resetGame_reiniciaElEstado() {
        val vm = GameViewModel()
        repeat(MAX_NO_OF_WORDS) { vm.skipWord() }
        vm.resetGame()
        val state = vm.uiState.value
        assertFalse(state.isGameOver)
        assertEquals(1, state.currentWordCount)
        assertEquals(0, state.score)
    }

    @Test
    fun scoreIncrease_esVeinte() {
        assertEquals(20, SCORE_INCREASE)
    }
}
