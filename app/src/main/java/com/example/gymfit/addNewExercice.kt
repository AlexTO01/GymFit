package com.example.gymfit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddNewExercise : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_new_exercice)

        val editTextExerciseName: EditText = findViewById(R.id.edit_text_exercise_name)
        val buttonAddExercise: Button = findViewById(R.id.button_add_exercise)

        buttonAddExercise.setOnClickListener {
            val exerciseName = editTextExerciseName.text.toString()
            if (exerciseName.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra("newExercise", exerciseName)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
