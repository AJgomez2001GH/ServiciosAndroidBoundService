package com.empresa.serviciosandroidboundservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlin.random.Random

class MyCustomBoundService : Service() {

    // Binder personalizado para exponer métodos
    //Un Binder es una clase que actúa como puente (conector) entre un servicio vinculado (bound service) y el componente cliente (como una Activity).
    //Básicamente, es el objeto que te permite acceder directamente a los métodos del servicio desde otra parte de tu app, como si el servicio fuera una clase normal
    private val binder = LocalBinder()

    //Es una clase interna que extiende la clase Binder.
    //Su objetivo es darle a la Activity acceso directo al servicio.
    //Esta función devuelve una referencia del servicio actual, para que la Activity pueda llamarle métodos directamente (por ejemplo: getRandomNumber()).
    inner class LocalBinder : Binder() {
        fun getService(): MyCustomBoundService = this@MyCustomBoundService
    }

    // Android lo llama cuando una actividad se vincula
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    // Metodo público accesible desde la Activity
    fun getRandomNumber(): Int {
        return Random.nextInt(0, 100)
    }
}