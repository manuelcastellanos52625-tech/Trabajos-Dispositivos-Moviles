package com.ucaldas.ciclodevida

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class ActivityA : ActivityConLog() {

    // Estado de la pantalla: vive en la instancia, NO en un companion object
    private var contadorActual = 0
    private lateinit var tvContador: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        // Paso 4.1: restaura el contador si el sistema nos recrea (rotación o proceso muerto)
        contadorActual = savedInstanceState?.getInt(CLAVE_CONTADOR) ?: 0

        tvContador = findViewById(R.id.tvContador)
        mostrarContador()

        findViewById<Button>(R.id.btnSumar).setOnClickListener {
            contadorActual++
            mostrarContador()
        }

        // Paso 3.2: A -> B con un String
        findViewById<Button>(R.id.btnIrB).setOnClickListener {
            val intent = Intent(this, ActivityB::class.java)
            intent.putExtra("nombre_usuario", "Ana")
            startActivity(intent)
        }
    }

    private fun mostrarContador() {
        tvContador.text = getString(R.string.contador_a, contadorActual)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CLAVE_CONTADOR, contadorActual)
        Log.d(TAG, "ActivityA.onSaveInstanceState - guardando contador=$contadorActual")
    }

    private companion object {
        // Solo una constante: no hay estado mutable estático
        const val CLAVE_CONTADOR = "contador"
    }
}
