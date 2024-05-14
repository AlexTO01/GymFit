package com.example.gymfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymfit.R
//import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signinButton: Button

    //private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        signinButton = findViewById(R.id.buttonSignin)

        //firebaseAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)


            /*
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa correo electrónico y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            */

            /*
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        // Inicio de sesión exitoso, puedes redirigir a la siguiente actividad

                    } else {
                        // Si el inicio de sesión falla, muestra un mensaje de error
                        Toast.makeText(this, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

             */
        }

        signinButton.setOnClickListener{
            // Lógica para el botón "Registrarse"
            //val intent = Intent(this, Register)
            // startActivity(intent)
            val intent = Intent(this, Register::class.java)
            startActivity(intent)

        }
    }
}
