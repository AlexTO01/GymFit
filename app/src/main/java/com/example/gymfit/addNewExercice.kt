package com.example.gymfit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AddNewExercise : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_new_exercice)

        val editTextExerciseName: EditText = findViewById(R.id.edit_text_exercise_name)
        val editTextExerciseSeries: EditText = findViewById(R.id.edit_text_exercise_series)
        val editTextExerciseReps: EditText = findViewById(R.id.edit_text_exercise_reps)
        val buttonAddExercise: Button = findViewById(R.id.button_add_exercise)

        buttonAddExercise.setOnClickListener {
            val exerciseName = editTextExerciseName.text.toString()
            val exerciseSeries = editTextExerciseSeries.text.toString()
            val exerciseReps = editTextExerciseReps.text.toString()

            if (exerciseName.isNotEmpty() && exerciseSeries.isNotEmpty() && exerciseReps.isNotEmpty()) {
                // Realiza la solicitud POST en un hilo separado
                Thread {
                    try {
                        val response = sendPostRequest(exerciseName, exerciseSeries, exerciseReps)
                        runOnUiThread {
                            if (response.isSuccessful) {
                                val resultIntent = Intent().apply {
                                    putExtra("newExercise", exerciseName)
                                }
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            } else {
                                Toast.makeText(this, "Error al añadir el ejercicio: ${response.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: IOException) {
                        runOnUiThread {
                            Toast.makeText(this, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendPostRequest(name: String, series: String, reps: String): okhttp3.Response {
        val client = OkHttpClient()

        // Reemplaza con la URL de tu API
        val url = "http://192.168.1.136:4000/exercise"

        // Crear el JSON con los datos
        val json = JSONObject().apply {
            put("exerciseName", name)
            put("numSeries", series)
            put("numReps", reps)
        }

        // Crear el cuerpo de la solicitud
        val requestBody: RequestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        // Crear la solicitud
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        // Ejecutar la solicitud y devolver la respuesta
        return client.newCall(request).execute()
    }
}
