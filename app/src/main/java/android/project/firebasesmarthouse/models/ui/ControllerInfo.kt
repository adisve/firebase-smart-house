package android.project.firebasesmarthouse.models.ui

data class ControllerInfo(
    val type: ControllerType,
    val state: Boolean,
    val onStateChanged: (Boolean) -> Unit
)