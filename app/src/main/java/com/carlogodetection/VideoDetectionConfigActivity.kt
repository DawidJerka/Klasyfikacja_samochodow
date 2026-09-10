package com.carlogodetection

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carlogodetection.ui.theme.CarLogoDetectionTheme

class VideoDetectionConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CarLogoDetectionTheme {
                VideoDetectionConfigScreen(
                    onStartWithVideo = { stepMs, uri ->
                        val intent = Intent(this, VideoDetectionActivity::class.java).apply {
                            data = uri
                            putExtra("stepMs", stepMs)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(intent)
                        finish()
                    },
                    onCancel = { finish() }
                )
            }
        }
    }
}

@Composable
fun VideoDetectionConfigScreen(
    onStartWithVideo: (Long, Uri) -> Unit,
    onCancel: () -> Unit
) {
    // krok w sekundach (np. 0.1–1.0 s)
    var stepSeconds by remember { mutableStateOf(0.3f) }

    // launcher do wyboru wideo
    val videoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                // zamiana sekund na ms
                val stepMs = (stepSeconds * 1000).toLong()
                onStartWithVideo(stepMs, uri)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ustawienia detekcji wideo", modifier = Modifier.padding(bottom = 24.dp))

        Text("Częstotliwość detekcji:")
        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = stepSeconds,
            onValueChange = { stepSeconds = it },
            valueRange = 0.1f..1.0f, // od 0.1s do 1.0s
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Detekcja co ${"%.1f".format(stepSeconds)} s")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                videoPickerLauncher.launch(arrayOf("video/*"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Wybierz wideo i uruchom detekcję")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Anuluj")
        }
    }
}
