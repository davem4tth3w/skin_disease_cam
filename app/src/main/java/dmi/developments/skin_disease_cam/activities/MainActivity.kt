package dmi.developments.skin_disease_cam.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import dmi.developments.skin_disease_cam.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_page)

        // Show disclaimer dialog after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            showDisclaimerDialog()
        }, 2000)
    }

    private fun showDisclaimerDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.disclaimer_dialog)
        bottomSheetDialog.setCancelable(false)

        val understandButton: Button? = bottomSheetDialog.findViewById(R.id.understand_button)
        understandButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }


}
