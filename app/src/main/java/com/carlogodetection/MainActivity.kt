package com.carlogodetection

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlogodetection.ui.theme.CarLogoDetectionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CarLogoDetectionTheme {
                MainMenuScreen(
                    onVideoDetection = {
                        startActivity(
                            Intent(this, VideoDetectionConfigActivity::class.java)
                        )
                    },
                    onCameraDetection = {
                        // TODO: uruchom aktywność CameraX
                        // startActivity(Intent(this, CameraDetectionActivity::class.java))
                    },
                    onExit = {
                        finishAffinity()  // zamyka aplikację
                    }
                )
            }
        }
    }
}

@Composable
fun MainMenuScreen(
    onVideoDetection: () -> Unit,
    onCameraDetection: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---- LOGO ----
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 40.dp)
        )

        // ---- Przycisk: Detekcja wideo ----
        MenuButton(text = "Detekcja wideo", onClick = onVideoDetection)

        Spacer(modifier = Modifier.height(16.dp))

        // ---- Przycisk: Detekcja z kamery ----
        MenuButton(text = "Detekcja z kamery", onClick = onCameraDetection)

        Spacer(modifier = Modifier.height(16.dp))

        // ---- Przycisk: Wyjście ----
        MenuButton(text = "Wyjście", onClick = onExit, color = Color.Red)
    }
}

@Composable
fun MenuButton(
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
