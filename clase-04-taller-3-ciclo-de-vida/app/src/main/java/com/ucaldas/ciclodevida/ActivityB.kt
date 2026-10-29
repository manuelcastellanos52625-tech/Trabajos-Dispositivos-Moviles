package com.ucaldas.ciclodevida

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class ActivityB : ActivityConLog() {

    private var contadorActual = 10
    private lateinit var tvResultado: TextView

    // Paso 3.4: API moderna. Se registra en la creación de la Activity, no dentro de un click.
    private val lanzadorC = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        if (resultado.resultCode == RESULT_OK) {
            val nuevo = resultado.data?.getIntExtra("contador_actualizado", 0) ?: 0
            contadorActual = nuevo
            mostrarContador()
            Log.d(TAG, "ActivityB recibió de vuelta: $nuevo")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        // Paso 3.2: recibe el String enviado por A (con valor por defecto, sin !!)
        val nombre = intent.getStringExtra("nombre_usuario") ?: "(sin nombre)"
        Log.d(TAG, "ActivityB recibió nombre_usuario=$nombre")
        findViewById<TextView>(R.id.tvNombre).text = getString(R.string.hola_usuario, nombre)

        tvResultado = findViewById(R.id.tvResultado)
        contadorActual = savedInstanceState?.getInt("contador") ?: contadorActual
        mostrarContador()

        // Paso 3.3: B -> C con un Int
        findViewById<Button>(R.id.btnIrC).setOnClickListener {
            val intent = Intent(this, ActivityC::class.java)
            intent.putExtra("contador", contadorActual)
            lanzadorC.launch(intent)
        }
    }

    private fun mostrarContador() {
        tvResultado.text = getString(R.string.contador_b, contadorActual)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("contador", contadorActual)
    }
}
