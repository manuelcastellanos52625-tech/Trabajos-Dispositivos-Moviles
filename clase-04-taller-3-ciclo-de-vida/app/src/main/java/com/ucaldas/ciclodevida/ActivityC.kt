package com.ucaldas.ciclodevida

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class ActivityC : ActivityConLog() {

    private var contadorActual = 0
    private lateinit var tvContador: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c)

        // Paso 3.3: recibe el Int enviado por B (si ya hubo rotación, manda el Bundle guardado)
        contadorActual = savedInstanceState?.getInt("contador")
            ?: intent.getIntExtra("contador", 0)
        Log.d(TAG, "ActivityC recibió contador=$contadorActual")

        tvContador = findViewById(R.id.tvContadorC)
        mostrarContador()

        findViewById<Button>(R.id.btnMas).setOnClickListener {
            contadorActual++
            mostrarContador()
        }
        findViewById<Button>(R.id.btnMenos).setOnClickListener {
            contadorActual--
            mostrarContador()
        }

        // Paso 3.4: devuelve el valor modificado a B
        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val resultado = Intent().apply {
                putExtra("contador_actualizado", contadorActual)
            }
            setResult(RESULT_OK, resultado)
            finish()
        }
    }

    private fun mostrarContador() {
        tvContador.text = getString(R.string.contador_c, contadorActual)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("contador", contadorActual)
    }
}
