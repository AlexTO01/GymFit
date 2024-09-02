package com.example.gymfit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ChooseExercise : AppCompatActivity() {

    private val exercises = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listView: ListView

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

        listView = findViewById(R.id.list_view_exercises)
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

        registerForContextMenu(listView)

        val buttonAddExercise: Button = findViewById(R.id.button_add_exercise)
        buttonAddExercise.setOnClickListener {
            val intent = Intent(this, AddNewExercise::class.java)
            addExerciseLauncher.launch(intent)
        }

        // Cargar los ejercicios desde la API
        loadExercisesFromApi()
    }

    private fun loadExercisesFromApi() {
        val client = OkHttpClient()

        // Reemplaza con tu URL real
        val url = "https://tu-api.com/getExercises"

        val request = Request.Builder()
            .url(url)
            .build()

        Thread {
            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        parseAndLoadExercises(it)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error al obtener los ejercicios: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                runOnUiThread {
                    Toast.makeText(this, "Error al conectar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun parseAndLoadExercises(jsonResponse: String) {
        val jsonObject = JSONObject(jsonResponse)
        val exercisesExist = jsonObject.getBoolean("exercisesExist")

        if (exercisesExist) {
            val exercisesArray: JSONArray = jsonObject.getJSONArray("exercises")
            for (i in 0 until exercisesArray.length()) {
                val exerciseObject = exercisesArray.getJSONObject(i)
                val exerciseName = exerciseObject.getString("excerciseName")
                exercises.add(exerciseName)
            }

            // Actualizar la lista en el hilo principal
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                exercises.removeAt(info.position)
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
