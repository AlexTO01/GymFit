package com.example.gymfit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class analiceExercice : AppCompatActivity() {

    private lateinit var selectedImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var analyzeButton: Button
    private val REQUEST_IMAGE_SELECT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_analice_exercice)

        selectedImage = findViewById(R.id.selected_image)
        selectImageButton = findViewById(R.id.select_image_button)
        analyzeButton = findViewById(R.id.analyze_button)

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
            selectedImage.setImageURI(selectedImageUri)
        }
    }

    private fun analyzeImage() {
        // Implementa la lógica para analizar la imagen aquí
        Toast.makeText(this, "Analizando imagen...", Toast.LENGTH_SHORT).show()
    }
}
