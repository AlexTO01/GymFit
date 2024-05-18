package com.example.gymfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children

class PrincipalRutines : AppCompatActivity() {

    // Map to keep track of exercise containers for each day
    private val exerciseContainers: MutableMap<String, LinearLayout> = mutableMapOf()

    // Launchers for each day
    private val launchers: MutableMap<String, androidx.activity.result.ActivityResultLauncher<Intent>> = mutableMapOf()

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
}
