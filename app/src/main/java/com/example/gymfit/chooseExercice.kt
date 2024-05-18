package com.example.gymfit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gymfit.AddNewExercise

class ChooseExercise : AppCompatActivity() {

    private val exercises = mutableListOf("Ejercicio 1", "Ejercicio 2", "Ejercicio 3")
    private lateinit var adapter: ArrayAdapter<String>

    private val addExerciseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newExercise = result.data?.getStringExtra("newExercise")
            newExercise?.let {
                exercises.add(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_choose_exercice)

        val listView: ListView = findViewById(R.id.list_view_exercises)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, exercises)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedExercise = exercises[position]
            val resultIntent = Intent().apply {
                putExtra("selectedExercise", selectedExercise)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val buttonAddExercise: Button = findViewById(R.id.button_add_exercise)
        buttonAddExercise.setOnClickListener {
            val intent = Intent(this, AddNewExercise::class.java)
            addExerciseLauncher.launch(intent)
        }
    }
}
