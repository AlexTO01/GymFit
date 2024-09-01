package com.example.gymfit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.security.KeyStore
import javax.net.ssl.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody
import okhttp3.Response

val client = OkHttpClient()


class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signinButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        signinButton = findViewById(R.id.buttonSignin)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            GlobalScope.launch(Dispatchers.Main) {
                var userExist = withContext(Dispatchers.IO) { getUser(email, password) }

                if (userExist) {
                    // Lógica para el caso en que el usuario existe
                    val intent = Intent(this@MainActivity, MenuActivity::class.java)
                    startActivity(intent)
                } else {
                    // Manejar el caso en que el usuario no existe
                    Toast.makeText(this@MainActivity, "Usuario no encontrado", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        signinButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    fun showAboutMessage(view: View) {
        Toast.makeText(this, "Prueba de concepto de la aplicación, los datos guardados serán guardados para el uso de la aplicación", Toast.LENGTH_SHORT).show()
    }


    private fun getUser(email: String, passwd: String): Boolean {
        var userExists = false

        val url = "http://192.168.1.136:4000/login?email=$email&password=$passwd"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val responseBody = response.body?.string()
            println(responseBody)

            // Aquí deberías analizar la respuesta JSON y establecer userExists en consecuencia
            val jsonResponse = JSONObject(responseBody)
            userExists = jsonResponse.getBoolean("userExists")
        }
        return userExists
    }


}
