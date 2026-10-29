package com.ucaldas.ciclodevida

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

const val TAG = "CicloDeVida"

/**
 * Base de las tres Activities: sobrescribe los 7 callbacks del ciclo de vida y registra cada uno
 * con el nombre de la clase concreta y el hashCode() de la instancia. El hashCode es lo único que
 * permite saber si tras un evento sigue siendo la MISMA Activity o una NUEVA.
 *
 * Nota: no hay ninguna referencia estática a la Activity ni contadores en companion object
 * (ese es el bug de la Parte 2; ver ANALISIS.md).
 */
abstract class ActivityConLog : ComponentActivity() {

    private fun log(callback: String) {
        Log.d(TAG, "${this::class.simpleName}.$callback - instancia: ${this.hashCode()}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate (savedInstanceState ${if (savedInstanceState == null) "null" else "con datos"})")
    }

    override fun onStart() {
        super.onStart()
        log("onStart")
    }

    override fun onResume() {
        super.onResume()
        log("onResume")
    }

    override fun onPause() {
        super.onPause()
        log("onPause")
    }

    override fun onStop() {
        super.onStop()
        log("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        log("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy (isChangingConfigurations=$isChangingConfigurations, isFinishing=$isFinishing)")
    }
}
