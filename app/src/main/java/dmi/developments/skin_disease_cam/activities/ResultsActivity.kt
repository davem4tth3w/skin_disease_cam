package dmi.developments.skin_disease_cam.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import dmi.developments.skin_disease_cam.R

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val imagePath = intent.getStringExtra("imagePath")

        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            imageView.setImageBitmap(bitmap)
        }
    }
}
