package com.example.gymfit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Register : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signinButton: Button
    private lateinit var checkbox: CheckBox

    //private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        signinButton = findViewById(R.id.buttonSignin)
        checkbox = findViewById(R.id.checkBoxRegister)

        signinButton.setOnClickListener {
            // Lógica para el botón "Registrarse"
            // Introducimos usuario nuevo en la base de datos y luego redirigimos al menu principal

            if (checkbox.isChecked) {
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                // Realizamos la petición HTTP POST en un hilo separado para evitar bloquear el hilo principal
                Thread {
                    try {
                        val response = sendRegistrationRequest(name, email, password)
                        runOnUiThread {
                            if (response.isSuccessful) {
                                val intent = Intent(this, MenuActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Error al registrar: ${response.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: IOException) {
                        runOnUiThread {
                            Toast.makeText(this, "Error al conectar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()

            } else {
                Toast.makeText(this, "No se puede registrar sin marcar el checkbox", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun sendRegistrationRequest(name: String, email: String, password: String): Response {
        val url = "https://tu-api.com/register"

        // Crear el JSON con los datos
        val json = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }

        // Crear el cuerpo de la petición
        val requestBody: RequestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        // Crear el cliente HTTP y la solicitud
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        // Ejecutar la solicitud y obtener la respuesta
        return client.newCall(request).execute()
    }

    fun showAboutMessage(view: View) {
        Toast.makeText(this, "Prueba de concepto de la aplicación, los datos guardados serán guardados para el uso de la aplicación", Toast.LENGTH_SHORT).show()
    }
}

