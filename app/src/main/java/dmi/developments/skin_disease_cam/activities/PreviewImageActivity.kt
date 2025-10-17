package dmi.developments.skin_disease_cam.activities

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import dmi.developments.skin_disease_cam.R

class PreviewImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview_image)

        val imageView = findViewById<ImageView>(R.id.preview_img)
        val cancelButton = findViewById<ImageButton>(R.id.imageButton)
        val confirmButton = findViewById<ImageButton>(R.id.imageButton3)

        val imageUri = intent.getStringExtra("capturedImageUri")

        imageUri?.let {
            imageView.setImageURI(Uri.parse(it))
        }

        cancelButton.setOnClickListener {
            finish() // Close preview
        }

        confirmButton.setOnClickListener {

            finish()
        }
    }
}
