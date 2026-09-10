package com.carlogodetection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    data class Box(
        val rect: RectF,   // współrzędne w pikselach KLATKI wideo
        val label: String,
        val score: Float
    )

    var boxes: List<Box> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    /**
     * Rozmiar oryginalnej klatki (bitmapy z retrievera),
     * NIE rozmiar PlayerView.
     */
    var videoWidth: Int = 0
    var videoHeight: Int = 0

    private val boxPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val textPaint = Paint().apply {
        color = Color.RED
        textSize = 48f
        isFakeBoldText = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (videoWidth <= 0 || videoHeight <= 0) return

        // aspekt widoku i wideo
        val viewW = width.toFloat()
        val viewH = height.toFloat()
        val viewAR = viewW / viewH
        val videoAR = videoWidth.toFloat() / videoHeight.toFloat()

        // prostokąt, w którym PlayerView faktycznie rysuje wideo
        val contentRect: RectF = if (videoAR > viewAR) {
            // wideo szersze – pełna szerokość, paski u góry i dołu
            val drawW = viewW
            val drawH = viewW / videoAR
            val top = (viewH - drawH) / 2f
            RectF(0f, top, drawW, top + drawH)
        } else {
            // wideo wyższe – pełna wysokość, paski po bokach
            val drawH = viewH
            val drawW = viewH * videoAR
            val left = (viewW - drawW) / 2f
            RectF(left, 0f, left + drawW, drawH)
        }

        val sx = contentRect.width() / videoWidth.toFloat()
        val sy = contentRect.height() / videoHeight.toFloat()

        for (b in boxes) {
            // przeskalowanie do prostokąta wyświetlania
            val scaled = RectF(
                contentRect.left + b.rect.left * sx,
                contentRect.top + b.rect.top * sy,
                contentRect.left + b.rect.right * sx,
                contentRect.top + b.rect.bottom * sy
            )

            canvas.drawRect(scaled, boxPaint)

            val text = "${b.label} ${"%.2f".format(b.score)}"
            canvas.drawText(
                text,
                scaled.left,
                scaled.top - 8f,
                textPaint
            )
        }
    }
}
