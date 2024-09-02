package com.example.gymfit

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.InputStreamReader

class analiceExercice : AppCompatActivity() {

    private lateinit var selectedImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var analyzeButton: Button
    private lateinit var analysisResultText: TextView
    private val REQUEST_IMAGE_SELECT = 1
    private var selectedImagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_analice_exercice)

        selectedImage = findViewById(R.id.selected_image)
        selectImageButton = findViewById(R.id.select_image_button)
        analyzeButton = findViewById(R.id.analyze_button)
        analysisResultText = findViewById(R.id.analysis_result_text)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_SELECT)
        }

        analyzeButton.setOnClickListener {
            analyzeImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let {
                selectedImage.setImageURI(it)

                // Convertir URI a la ruta del archivo en el sistema
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(it, filePathColumn, null, null, null)
                cursor?.moveToFirst()
                val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                selectedImagePath = cursor?.getString(columnIndex!!)
                cursor?.close()
            }
        }
    }

    private fun analyzeImage() {
        selectedImagePath?.let { imagePath ->
            try {
                val scriptPath = File(Environment.getExternalStorageDirectory(), "NewPredict.py").absolutePath
                val outputDir = File(Environment.getExternalStorageDirectory(), "output").absolutePath
                val process = ProcessBuilder(
                    "C:/Users/AlexTO/AppData/Local/Programs/Python/Python39/python.exe", //Cambiar por path de python
                    scriptPath,
                    "--model", "movenet_thunder",
                    "--conf_thres", "0.01",
                    "--image_dir", imagePath,
                    "--output_dir", outputDir
                ).redirectErrorStream(true).start()

                // Capturar la salida del script de Python
                val reader = InputStreamReader(process.inputStream)
                val resultText = reader.readText()
                reader.close()

                val exitCode = process.waitFor()
                if (exitCode == 0) {
                    // Cargar la imagen procesada
                    val resultImageFile = File(outputDir, "processed_image.png")
                    if (resultImageFile.exists()) {
                        val bitmap = BitmapFactory.decodeFile(resultImageFile.absolutePath)
                        selectedImage.setImageBitmap(bitmap)
                    } else {
                        Toast.makeText(this, "No se encontró la imagen procesada", Toast.LENGTH_SHORT).show()
                    }

                    // Mostrar el resultado devuelto por el script
                    analysisResultText.text = resultText
                } else {
                    Toast.makeText(this, "Error al analizar la imagen", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error ejecutando el script de Python", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Por favor, selecciona una imagen primero", Toast.LENGTH_SHORT).show()
        }
    }
}
