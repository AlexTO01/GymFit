package com.example.gymfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.gymfit.R

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_init_menu)

        val buttonRutinas: Button = findViewById(R.id.buttonRutinas)
        val buttonAnalisis: Button = findViewById(R.id.buttonAnalisis)

        buttonRutinas.setOnClickListener {
            // Lógica para el botón "Rutinas"
            // val intent = Intent(this, RutinasActivity)
            // startActivity(intent)
        }

        buttonAnalisis.setOnClickListener {
            // Lógica para el botón "Análisis de ejercicio"
            // val intent = Intent(this, AnalisisActivity)
            // startActivity(intent)
        }
    }
}
