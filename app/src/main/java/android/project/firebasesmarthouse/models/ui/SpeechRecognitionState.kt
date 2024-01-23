package android.project.firebasesmarthouse.models.ui

data class SpeechRecognitionState(
    val isSpeaking: Boolean = false,
    val isProcessing: Boolean = false,
    val spokenText: String = "",
    val error: String? = null
)