package android.project.firebasesmarthouse.ui.composables

import android.project.firebasesmarthouse.models.ui.ControllerInfo
import android.project.firebasesmarthouse.models.ui.ControllerType
import android.project.firebasesmarthouse.viewmodel.AppViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun App(
    viewModel: AppViewModel = viewModel(),
) {
    val lightState = viewModel.lightState.observeAsState(initial = false)
    val doorState = viewModel.doorState.observeAsState(initial = false)
    val windowState = viewModel.windowState.observeAsState(initial = false)
    val speechRecognitionState = viewModel.speechRecognitionState.observeAsState(initial = null)

    val controllers = listOf(
        ControllerInfo(ControllerType.Light, lightState.value) { viewModel.setLightState(it) },
        ControllerInfo(ControllerType.Door, doorState.value) { viewModel.setDoorState(it) },
        ControllerInfo(ControllerType.Window, windowState.value) { viewModel.setWindowState(it) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        speechRecognitionState.value?.let {
            SpeechRecognitionButton(
                state = it,
                startListening = { viewModel.startSpeechRecognition() },
                stopListening = { viewModel.stopSpeechRecognition() },
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        controllers.forEach { controllerInfo ->
            Controller(
                controllerType = controllerInfo.type,
                state = controllerInfo.state,
                onStateChanged = controllerInfo.onStateChanged
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}
