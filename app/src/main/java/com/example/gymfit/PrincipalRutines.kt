package com.example.gymfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class PrincipalRutines : AppCompatActivity() {

    // Map to keep track of exercise containers for each day
    private val exerciseContainers: MutableMap<String, LinearLayout> = mutableMapOf()

    // Launchers for each day
    private val launchers: MutableMap<String, androidx.activity.result.ActivityResultLauncher<Intent>> = mutableMapOf()

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_principal_rutines)

        setupDay("Lunes", R.id.lunes_exercise_container, R.id.button_lunes)
        setupDay("Martes", R.id.martes_exercise_container, R.id.button_martes)
        setupDay("Miércoles", R.id.miercoles_exercise_container, R.id.button_miercoles)
        setupDay("Jueves", R.id.jueves_exercise_container, R.id.button_jueves)
        setupDay("Viernes", R.id.viernes_exercise_container, R.id.button_viernes)
        setupDay("Sábado", R.id.sabado_exercise_container, R.id.button_sabado)
        setupDay("Domingo", R.id.domingo_exercise_container, R.id.button_domingo)

        fetchExercises()
    }

    private fun setupDay(day: String, containerId: Int, buttonId: Int) {
        val container: LinearLayout = findViewById(containerId)
        val button: Button = findViewById(buttonId)

        exerciseContainers[day] = container

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedExercise = result.data?.getStringExtra("selectedExercise")
                selectedExercise?.let {
                    addExerciseToDay(day, it)
                    postExercise(day, it) // POST to the API
                }
            }
        }

        launchers[day] = launcher

        button.setOnClickListener {
            val intent = Intent(this, ChooseExercise::class.java)
            launchers[day]?.launch(intent)
        }

        // Add click listener to exercise TextViews for deletion
        container.children.forEach { view ->
            if (view is TextView) {
                view.setOnClickListener {
                    // Remove exercise from the container and update UI
                    container.removeView(view)
                }
            }
        }
    }

    private fun addExerciseToDay(day: String, exercise: String) {
        val exerciseTextView = TextView(this).apply {
            text = exercise
            textSize = 16f
        }
        exerciseContainers[day]?.addView(exerciseTextView)
        // Add click listener to newly added exercise TextView for deletion
        exerciseTextView.setOnClickListener {
            // Remove exercise from the container and update UI
            exerciseContainers[day]?.removeView(exerciseTextView)
        }
    }

    private fun fetchExercises() {
        val request = Request.Builder()
            .url("http://192.168.1.136:4000/routineXexercise")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar errores de la petición
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    val jsonArray = JSONArray(responseBody)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val dayOfWeek = jsonObject.getString("routine_dayOfWeek")
                        val exerciseName = jsonObject.getString("exercise_name")

                        runOnUiThread {
                            when (dayOfWeek) {
                                "Monday" -> addExerciseToDay("Lunes", exerciseName)
                                "Tuesday" -> addExerciseToDay("Martes", exerciseName)
                                "Wednesday" -> addExerciseToDay("Miércoles", exerciseName)
                                "Thursday" -> addExerciseToDay("Jueves", exerciseName)
                                "Friday" -> addExerciseToDay("Viernes", exerciseName)
                                "Saturday" -> addExerciseToDay("Sábado", exerciseName)
                                "Sunday" -> addExerciseToDay("Domingo", exerciseName)
                            }
                        }
                    }
                }
            }
        })
    }


    private fun postExercise(day: String, exercise: String) {
        val requestBody = FormBody.Builder()
            .add("day", day)
            .add("exercise", exercise)
            .build()

        val request = Request.Builder()
            .url("http://192.168.1.136:4000/routineXexercise") // Reemplaza con tu URL de la API
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar errores de la petición
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // Manejar la respuesta del servidor
                if (!response.isSuccessful) {
                    // Manejar respuesta no exitosa
                }
            }
        })
    }
}
