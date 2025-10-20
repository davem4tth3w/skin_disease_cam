package dmi.developments.skin_disease_cam.activities

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dmi.developments.skin_disease_cam.R
import dmi.developments.skin_disease_cam.data.entity.Result
import dmi.developments.skin_disease_cam.viewmodel.ResultViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PreviewImageActivity : AppCompatActivity() {

    private val resultViewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview_image)

        val imageView = findViewById<ImageView>(R.id.preview_img)
        val cancelButton = findViewById<ImageButton>(R.id.cancel_btn)
        val confirmButton = findViewById<ImageButton>(R.id.check_btn)

        val imageUriString = intent.getStringExtra("capturedImageUri")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        // Display preview image
        imageUri?.let {
            imageView.setImageURI(it)
        }

        cancelButton.setOnClickListener {
            finish() // Cancel preview
        }

        confirmButton.setOnClickListener {
            if (imageUriString != null) {
                // Step 1: Show loading dialog
                showLoadingAndSave(imageUriString)
            }
        }
    }

    private fun showLoadingAndSave(imageUriString: String) {
        // Create loading dialog
        val loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.loading_dialog)
        loadingDialog.setCancelable(false)
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.show()

        // Step 2: Simulate loading for 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Step 3: Save image result in database
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            val result = Result(
                imagePath = imageUriString,
                skindisease = null,
                remedies = null,
                timestamp = timestamp
            )

            resultViewModel.addResult(result)

            loadingDialog.dismiss()

            // Step 4: Navigate to ResultsActivity
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("imagePath", Uri.parse(imageUriString).path)
            intent.putExtra("skindisease", "Unknown") // Placeholder
            intent.putExtra("remedies", "No remedies found") // Placeholder
            startActivity(intent)

            finish() // Close preview activity
        }, 2000)
    }
}
