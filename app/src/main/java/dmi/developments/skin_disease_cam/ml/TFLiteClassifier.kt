package dmi.developments.skin_disease_cam.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import kotlin.math.min

data class Prediction(val label: String, val score: Float)

class TFLiteClassifier(
    private val context: Context,
    private val modelAsset: String,
    private val labelsAsset: String = "labels.txt",
    private val numThreads: Int = 4,
    private val tryGpu: Boolean = false,     // set true for FP16 models
    private val tryNnapi: Boolean = false    // optional
) : AutoCloseable {

    private var interpreter: Interpreter
    private var inputH = 0
    private var inputW = 0
    private var inputC = 0
    private val labels: List<String>
    private var delegate: GpuDelegate? = null

    init {
        // 1) load model MappedByteBuffer (uncompressed in assets)
        val modelBuffer = context.assets.openFd(modelAsset).use { fd ->
            FileInputStream(fd.fileDescriptor).channel
                .map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.length)
        }

        val options = Interpreter.Options().apply {
            setNumThreads(numThreads)
            // XNNPACK is on by default; try GPU first if requested
            if (tryGpu) {
                val compat = CompatibilityList()
                if (compat.isDelegateSupportedOnThisDevice) {
                    delegate = GpuDelegate() // Use default GPU delegate options
                    addDelegate(delegate)
                }
            }

            if (tryNnapi) {
                setUseNNAPI(true)
            }
        }

        interpreter = Interpreter(modelBuffer, options)

        // 2) read input tensor shape (auto-detect size; avoids hardcoding)
        val ishape = interpreter.getInputTensor(0).shape() // [1, H, W, 3]
        inputH = ishape[1]
        inputW = ishape[2]
        inputC = ishape[3]

        // 3) load labels
        labels = context.assets.open(labelsAsset).bufferedReader().readLines()
        require(labels.isNotEmpty()) { "labels.txt is empty or missing." }
        require(inputC == 3) { "Model expects $inputC channels; expected 3." }
    }

    /** Classify a Bitmap. Returns top-K sorted predictions (default K=3). */
    fun classify(bm: Bitmap, topK: Int = 3): List<Prediction> {
        val input = bitmapToInput(bm, inputW, inputH)
        val outputs = Array(1) { FloatArray(labels.size) }
        val t0 = System.nanoTime()
        interpreter.run(input, outputs)
        val t1 = System.nanoTime()
        Log.d("TFLiteClassifier", "inference ${(t1 - t0)/1e6} ms")

        val scores = outputs[0]
        return scores
            .mapIndexed { idx, s -> Prediction(labels[idx], s) }
            .sortedByDescending { it.score }
            .take(topK)
    }

    /** Convert Bitmap -> Float32 [1,H,W,3] with values 0..255f (NO extra normalization). */
    private fun bitmapToInput(src: Bitmap, dstW: Int, dstH: Int): ByteBuffer {
        // Center-crop to square, then resize to model size to reduce distortion.
        val size = min(src.width, src.height)
        val xoff = (src.width - size) / 2
        val yoff = (src.height - size) / 2
        val square = Bitmap.createBitmap(src, xoff, yoff, size, size)

        val resized = Bitmap.createBitmap(dstW, dstH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resized)
        val m = Matrix().apply {
            setRectToRect(
                android.graphics.RectF(0f, 0f, square.width.toFloat(), square.height.toFloat()),
                android.graphics.RectF(0f, 0f, dstW.toFloat(), dstH.toFloat()),
                Matrix.ScaleToFit.FILL
            )
        }
        canvas.drawBitmap(square, m, Paint(Paint.FILTER_BITMAP_FLAG))

        val buf = ByteBuffer.allocateDirect(4 * dstW * dstH * 3).order(ByteOrder.nativeOrder())
        val pixels = IntArray(dstW * dstH)
        resized.getPixels(pixels, 0, dstW, 0, 0, dstW, dstH)
        var i = 0
        for (y in 0 until dstH) {
            for (x in 0 until dstW) {
                val p = pixels[i++]
                val r = ((p shr 16) and 0xFF).toFloat()
                val g = ((p shr 8) and 0xFF).toFloat()
                val b = (p and 0xFF).toFloat()
                // IMPORTANT: feed 0..255f (model has preprocessing inside)
                buf.putFloat(r); buf.putFloat(g); buf.putFloat(b)
            }
        }
        buf.rewind()
        return buf
    }

    override fun close() {
        try { interpreter.close() } catch (_: Throwable) {}
        try { delegate?.close() } catch (_: Throwable) {}
    }
}