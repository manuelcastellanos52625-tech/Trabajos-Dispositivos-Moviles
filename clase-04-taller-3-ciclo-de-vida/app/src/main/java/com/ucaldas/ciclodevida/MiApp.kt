package com.ucaldas.ciclodevida

import android.app.Application
import android.util.Log

// Bonus (Parte 5): se ejecuta una sola vez por proceso, antes de cualquier Activity.
class MiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MiApp.onCreate - el PROCESO arrancó")
    }
}
