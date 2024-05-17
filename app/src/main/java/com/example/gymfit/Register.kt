package com.example.gymfit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymfit.R
//import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signinButton: Button

    //private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        signinButton = findViewById(R.id.buttonSignin)

        //firebaseAuth = FirebaseAuth.getInstance()
        //Añadir checkbox con privacidad de datos, que no seran promovidos a terceros, y que se pueden borrar los datos si el usuario lo pido

        signinButton.setOnClickListener{
            // Lógica para el botón "Registrarse"
            // Introducimos usuario nuevo en la base de datos y luego redirigimos al menu principal
            // val intent = Intent(this, MenuActivity)
            // startActivity(intent)

        }
    }
}
