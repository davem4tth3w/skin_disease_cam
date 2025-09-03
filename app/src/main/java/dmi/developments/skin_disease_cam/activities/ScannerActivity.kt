package dmi.developments.skin_disease_cam.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import dmi.developments.skin_disease_cam.R
import dmi.developments.skin_disease_cam.data.database.AppDatabase
import dmi.developments.skin_disease_cam.data.entity.ScanResult
import dmi.developments.skin_disease_cam.data.repository.ScanRepository
import dmi.developments.skin_disease_cam.viewmodel.ScanViewModel
import dmi.developments.skin_disease_cam.viewmodel.ScanViewModelFactory
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ScannerActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null
    private lateinit var scanViewModel: ScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner)

        previewView = findViewById(R.id.previewView)

        // Initialize ViewModel
        val dao = AppDatabase.getDatabase(application).scanResultDao()
        val repository = ScanRepository(dao)
        val factory = ScanViewModelFactory(repository)
        scanViewModel = ViewModelProvider(this, factory)[ScanViewModel::class.java]

        // Ask camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Handle camera permission result
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

                // Wait 5 seconds then capture image
                GlobalScope.launch(Dispatchers.Main) {
                    delay(5000)
                    captureImage()
                }

            } catch (exc: Exception) {
                Log.e("ScannerActivity", "Camera binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "scan_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val imagePath = photoFile.absolutePath
                    Log.d("ScannerActivity", "Photo saved: $imagePath")

                    // Save record to Room database
                    val scan = ScanResult(
                        imagePath = imagePath,
                        diagnosis = null, // null for now
                        timestamp = System.currentTimeMillis()
                    )
                    scanViewModel.addScan(scan)

                    Toast.makeText(
                        this@ScannerActivity,
                        "Image captured and saved!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e("ScannerActivity", "Image capture failed: ${exc.message}", exc)
                }
            }
        )
    }
}
