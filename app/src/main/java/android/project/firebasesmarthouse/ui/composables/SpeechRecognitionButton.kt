package android.project.firebasesmarthouse.ui.composables

import android.content.pm.PackageManager
import android.project.firebasesmarthouse.models.ui.SpeechRecognitionState
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun SpeechRecognitionButton(
    state: SpeechRecognitionState,
    startListening: () -> Unit,
    stopListening: () -> Unit,
) {
    val context = LocalContext.current
    val initialPermissionGranted = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    val result = remember { mutableStateOf<Boolean?>(initialPermissionGranted) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        result.value = it
    }
    val infiniteTransition = rememberInfiniteTransition(label = "transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (state.isSpeaking) 1.5f else 1f,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    val shineSize = 180.dp
    val buttonSize = 160.dp

    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(shineSize)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    alpha = 1f - (scale - 1f) * (if (state.isSpeaking) 1f else 0f)
                }
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape)
        )

        if (state.isProcessing) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Button(
                onClick = {
                    if (result.value == null) {
                        launcher.launch(android.Manifest.permission.RECORD_AUDIO)
                    } else if (result.value == true) {
                        if (state.isSpeaking) {
                            Log.d("SpeechRecognitionButton", "Stopping speech recognition")
                            stopListening()
                        } else {
                            Log.d("SpeechRecognitionButton", "Starting speech recognition")
                            startListening()
                        }
                    } else {
                        launcher.launch(android.Manifest.permission.RECORD_AUDIO)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = CircleShape,
                modifier = Modifier.size(buttonSize)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Mic,
                    contentDescription = "microphone",
                    modifier = Modifier.padding(8.dp).size(64.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
