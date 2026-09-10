package com.carlogodetection

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.*

class VideoDetectionActivity : ComponentActivity() {

    private var player: ExoPlayer? = null
    private var retriever: MediaMetadataRetriever? = null

    private var preprocessingJob: Job? = null
    private var playbackOverlayJob: Job? = null

    // precomputed: czas (ms zaokrąglony do kroku) -> lista detekcji
    private val detectionsByTime = mutableMapOf<Long, List<YoloDetector.Detection>>()

    // co ile ms robimy detekcję podczas preprocessingu
    private var stepMs: Long = 300L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detection)

        stepMs = intent?.getLongExtra("stepMs", 300L) ?: 300L


        val videoUri: Uri? = intent?.data

        if (videoUri == null) {
            Toast.makeText(this, "Brak URI wideo", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        val playerView = findViewById<PlayerView>(R.id.playerView)
        val overlayView = findViewById<OverlayView>(R.id.overlayView)
        val progressContainer = findViewById<LinearLayout>(R.id.progressContainer)
        val progressText = findViewById<TextView>(R.id.progressText)

        // na start pokazujemy overlay z progressem
        progressContainer.visibility = View.VISIBLE

        val detector = YoloDetector(this)

        // ---- PREPROCESSING CAŁEGO WIDEO ----
        preprocessingJob = CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    retriever = MediaMetadataRetriever().apply {
                        setDataSource(this@VideoDetectionActivity, videoUri)
                    }

                    val durationMs = retriever
                        ?.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?.toLongOrNull() ?: 0L

                    // główna pętla po czasie
                    var t = 0L
                    while (t <= durationMs) {
                        // aktualizujemy tekst progresu
                        withContext(Dispatchers.Main) {
                            val percent = if (durationMs > 0) {
                                (t * 100 / durationMs).toInt()
                            } else 0
                            progressText.text = "Analizuję wideo... $percent%"
                        }

                        val frame = retriever?.getFrameAtTime(
                            t * 1000, // ms -> µs
                            MediaMetadataRetriever.OPTION_CLOSEST
                        )

                        if (frame != null) {
                            // detekcja YOLO na tej klatce
                            val detections = detector.detectDetections(frame)

                            // ustawiamy rozmiar wideo na rozmiar klatki (raz)
                            if (overlayView.videoWidth == 0 || overlayView.videoHeight == 0) {
                                withContext(Dispatchers.Main) {
                                    overlayView.videoWidth = frame.width
                                    overlayView.videoHeight = frame.height
                                }
                            }

                            detectionsByTime[t] = detections
                        }

                        t += stepMs
                    }
                }

                // po skończonym preprocessingu chowamy progres i uruchamiamy odtwarzanie
                progressContainer.visibility = View.GONE
                setupPlayerAndStartPlayback(videoUri, overlayView)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@VideoDetectionActivity,
                    "Błąd podczas analizy wideo: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun setupPlayerAndStartPlayback(
        videoUri: Uri,
        overlayView: OverlayView
    ) {
        val playerView = findViewById<PlayerView>(R.id.playerView)

        // ExoPlayer
        player = ExoPlayer.Builder(this).build().apply {
            playerView.player = this
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            play()
        }

        // lekka pętla – tylko odczyt z mapy + rysowanie
        playbackOverlayJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val currentMs = player?.currentPosition ?: 0L
                val isPlaying = player?.isPlaying == true

                if (isPlaying) {
                    // bierzemy najbliższy czas z preprocessingu
                    val key = (currentMs / stepMs) * stepMs
                    val dets = detectionsByTime[key].orEmpty()

                    overlayView.boxes = dets.map { d ->
                        OverlayView.Box(
                            rect = d.box,
                            label = d.label,
                            score = d.score
                        )
                    }
                }

                delay(10L) // odświeżanie overlayu ~12.5 FPS, lekko
            }
        }
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        preprocessingJob?.cancel()
        playbackOverlayJob?.cancel()
        player?.release()
        retriever?.release()
    }
}
