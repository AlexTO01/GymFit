package com.example.gymfit

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import com.example.gymfit.R

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_init_menu)

        val buttonRutinas: Button = findViewById(R.id.buttonRutinas)
        val buttonAnalisis: Button = findViewById(R.id.buttonAnalisis)
        val imageView: ImageView = findViewById(R.id.pfp)
        imageView.setOnClickListener { v -> showPopupMenu(v) }

        buttonRutinas.setOnClickListener {
            // Lógica para el botón "Rutinas"
            val intent = Intent(this, PrincipalRutines::class.java)
            startActivity(intent)
        }

        buttonAnalisis.setOnClickListener {
            // Lógica para el botón "Análisis de ejercicio"
            // val intent = Intent(this, AnalisisActivity)
            // startActivity(intent)
        }
    }
    fun showPopupMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.menu_drawer)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.nav_option1 -> {
                    // Acción para el primer elemento del menú
                    true
                }
                R.id.nav_option2 -> {
                    // Acción para el segundo elemento del menú
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

}
