package com.carlogodetection

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

class VideoPickerActivity : ComponentActivity() {

    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            // spróbujmy utrwalić uprawnienie (żeby Exo i retriever miały dostęp)
            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
                // jak się nie uda, i tak mamy tymczasowe READ na tę sesję
            }

            val intent = Intent(this, VideoDetectionActivity::class.java).apply {
                data = uri                                  // <-- URI jako data
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoPickerLauncher.launch(arrayOf("video/*"))
        setContent { }
    }
}
