package com.empresa.serviciosandroidboundservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread


class MainActivity : AppCompatActivity() {

    //-----------------------------------------------
    private var myService: MyCustomBoundService? = null
    //Es una variable booleana que se usa para saber si la Activity está actualmente vinculada (bound) al servicio o no.
    //Cuando usas un Bound Service (bindService(...)), estás estableciendo una conexión entre tu Activity y el Service. Esa conexión:
    //Te permite llamar directamente a métodos del servicio (como getRandomNumber()).
    //Solo dura mientras el cliente (la Activity) esté conectado.
    private var isBound = false

    // Este objeto maneja la conexión con el servicio
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyCustomBoundService.LocalBinder
            myService = binder.getService()
            isBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }
    //-----------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Instancias de los objetos del XML
        val btnStartBoundService = findViewById<Button>(R.id.btnStartForegroundService)
        val btnStopBoundService = findViewById<Button>(R.id.btnStopForegroundService)

        // Se declara el servicio
        val intent = Intent(this, MyCustomBoundService::class.java)

        // Cuando se da click se conecta al servicio
        btnStartBoundService.setOnClickListener {
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
            val random = myService?.getRandomNumber()
            println("Numero desde el servicio es: ${random}")

        }

        // Cuando se da click se desconecta del servicio
        btnStopBoundService.setOnClickListener {
            println("boton para desconectar presionado****")
            if (isBound) {
                unbindService(connection)
                isBound = false
            }
        }

    }// Termina el onCreate

    // Si esta conectado y se cierra la app se desconecta del servivio
    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
            println("Desconecion  realizada****")
        }
    }
}