package android.project.firebasesmarthouse.models.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.SensorDoor
import androidx.compose.material.icons.filled.SensorWindow
import androidx.compose.ui.graphics.vector.ImageVector

enum class ControllerType {
    Light,
    Door,
    Window;

    fun getControllerIcon(state: Boolean): ImageVector {
        return when (this) {
            Light -> Icons.Default.Lightbulb
            Door -> Icons.Default.SensorDoor
            Window -> Icons.Default.SensorWindow
        }
    }

    fun getStateName(state: Boolean): String {
        return when (this) {
            Light -> "Light: ${if (state) "On" else "Off"}"
            Door -> "Door: ${if (state) "Open" else "Closed"}"
            Window -> "Window: ${if (state) "Open" else "Closed"}"
        }
    }
}