package com.carlogodetection

import android.content.Context
import android.graphics.*
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.max
import kotlin.math.min

class YoloDetector(private val context: Context) {

    private val interpreter: Interpreter

    private val inputSize = 640
    private val numBoxes = 8400         // YOLOv8 anchor count

    private val labels = listOf(
        "audi",
        "citroen",
        "fiat",
        "ford",
        "hyundai",
        "opel",
        "peugeot",
        "renault",
        "seat",
        "skoda",
        "toyota",
        "volkswagen"
    )

    private val numClasses = labels.size

    init {
        interpreter = Interpreter(loadModel("model.tflite"))
    }

    private fun loadModel(path: String): ByteBuffer {
        val file = context.assets.openFd(path)
        val input = FileInputStream(file.fileDescriptor)
        val channel = input.channel
        return channel.map(FileChannel.MapMode.READ_ONLY, file.startOffset, file.declaredLength)
    }

    /**
     * Wygodna funkcja do detekcji na pojedynczym obrazku:
     * zwraca bitmapę z narysowanymi ramkami i labelami.
     */
    fun detect(bitmap: Bitmap): Bitmap {
        val detections = detectDetections(bitmap)
        return drawDetections(bitmap, detections)
    }

    /**
     * Główna logika YOLO – zwraca listę detekcji (label, score, box)
     * w współrzędnych pikselowych oryginalnej bitmapy.
     */
    fun detectDetections(bitmap: Bitmap): List<Detection> {
        // --- 1) PREPROCESS ---
        val tensor = TensorImage.fromBitmap(bitmap)
        val processor = org.tensorflow.lite.support.image.ImageProcessor.Builder()
            .add(ResizeOp(inputSize, inputSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f)) // jeśli sprawia problemy, można usunąć
            .build()

        val input = processor.process(tensor)

        val outputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, 16, numBoxes),
            org.tensorflow.lite.DataType.FLOAT32
        )
        interpreter.run(input.buffer, outputBuffer.buffer.rewind())

        // --- 2) PARSING YOLO OUTPUT ---
        val output = outputBuffer.floatArray
        val results = ArrayList<Detection>()

        // YOLO format: [16, 8400]
        // Flattenowanie: idx = dim * numBoxes + i
        for (i in 0 until numBoxes) {
            val cx = output[0 * numBoxes + i]
            val cy = output[1 * numBoxes + i]
            val w  = output[2 * numBoxes + i]
            val h  = output[3 * numBoxes + i]

            var bestClass = -1
            var bestScore = 0f

            for (c in 0 until numClasses) {
                val score = output[(4 + c) * numBoxes + i]
                if (score > bestScore) {
                    bestScore = score
                    bestClass = c
                }
            }

            if (bestScore > 0.4f && bestClass >= 0) {
                val left = (cx - w / 2f) * bitmap.width
                val top = (cy - h / 2f) * bitmap.height
                val right = (cx + w / 2f) * bitmap.width
                val bottom = (cy + h / 2f) * bitmap.height

                val label = labels[bestClass]

                results.add(
                    Detection(
                        label = label,
                        score = bestScore,
                        box = RectF(left, top, right, bottom)
                    )
                )
            }
        }

        // --- 3) NMS ---
        return nonMaxSuppression(results, 0.5f)
    }

    /**
     * Detekcja z nazwą klasy (string) + score + bounding box.
     */
    data class Detection(
        val label: String,
        val score: Float,
        val box: RectF
    )

    private fun nonMaxSuppression(dets: List<Detection>, iou: Float): List<Detection> {
        val sorted = dets.sortedByDescending { it.score }.toMutableList()
        val final = ArrayList<Detection>()

        while (sorted.isNotEmpty()) {
            val best = sorted.removeAt(0)
            final.add(best)

            sorted.removeAll { iou(best.box, it.box) > iou }
        }
        return final
    }

    private fun iou(a: RectF, b: RectF): Float {
        val x1 = max(a.left, b.left)
        val y1 = max(a.top, b.top)
        val x2 = min(a.right, b.right)
        val y2 = min(a.bottom, b.bottom)

        val inter = max(0f, x2 - x1) * max(0f, y2 - y1)
        val union = a.width() * a.height() + b.width() * b.height() - inter
        return if (union <= 0f) 0f else inter / union
    }

    private fun drawDetections(orig: Bitmap, dets: List<Detection>): Bitmap {
        val output = orig.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(output)

        val boxPaint = Paint().apply {
            color = Color.RED
            strokeWidth = 4f
            style = Paint.Style.STROKE
        }

        val textPaint = Paint().apply {
            color = Color.RED
            textSize = 70f
            isFakeBoldText = true
        }

        dets.forEach { det ->
            canvas.drawRect(det.box, boxPaint)

            val text = "${det.label} ${"%.2f".format(det.score)}"

            canvas.drawText(
                text,
                det.box.left,
                det.box.top - 10f,
                textPaint
            )
        }

        return output
    }
}
