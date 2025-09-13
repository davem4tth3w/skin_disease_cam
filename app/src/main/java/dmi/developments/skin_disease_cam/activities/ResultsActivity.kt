package dmi.developments.skin_disease_cam.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
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

        val scanAgainBtn = findViewById<Button>(R.id.scan_again_btn)
        scanAgainBtn.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
