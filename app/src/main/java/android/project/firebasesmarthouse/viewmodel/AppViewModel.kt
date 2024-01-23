package android.project.firebasesmarthouse.viewmodel

import android.project.firebasesmarthouse.FirebaseSmartHouseApplication
import android.project.firebasesmarthouse.data.FirebaseAPIService
import android.project.firebasesmarthouse.data.GptAPIService
import android.project.firebasesmarthouse.data.SpeechRecognitionService
import android.project.firebasesmarthouse.models.network.GptRequest
import android.project.firebasesmarthouse.models.ui.SpeechRecognitionState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel : ViewModel() {

    private val firebaseManager: FirebaseAPIService = FirebaseAPIService
    private val gptApiManager: GptAPIService = GptAPIService
    private val speechRecognitionService : SpeechRecognitionService

    private val _speechRecognitionState = MutableLiveData<SpeechRecognitionState>(
        SpeechRecognitionState()
    )
    val speechRecognitionState: LiveData<SpeechRecognitionState> = _speechRecognitionState

    private val _currentRmsValue = MutableLiveData<Float>()
    val currentRmsValue: LiveData<Float> = _currentRmsValue

    private val _lightState = MutableLiveData<Boolean>()
    val lightState: LiveData<Boolean> = _lightState

    private val _doorState = MutableLiveData<Boolean>()
    val doorState: LiveData<Boolean> = _doorState

    private val _windowState = MutableLiveData<Boolean>()
    val windowState: LiveData<Boolean> = _windowState

    init {
        initFirebase()
        speechRecognitionService = SpeechRecognitionService(
            FirebaseSmartHouseApplication.getInstance(),
            onResultsReceived = { text -> processSpeechResults(text) },
            onErrorOccurred = { error -> processSpeechError(error) },
        )
    }

    fun startSpeechRecognition() {
        _speechRecognitionState.postValue(SpeechRecognitionState(isSpeaking = true))
        speechRecognitionService.startListening("en-US")
    }

    fun stopSpeechRecognition() {
        _speechRecognitionState.postValue(SpeechRecognitionState(isSpeaking = false))
        speechRecognitionService.stopListening()
    }


    fun setLightState(state: Boolean) {
        firebaseManager.setLightState(state)
    }

    fun setDoorState(state: Boolean) {
        firebaseManager.setDoorState(state)
    }

    fun setWindowState(state: Boolean) {
        firebaseManager.setWindowState(state)
    }

    private fun processWithGptApi(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gptRequest = createGptRequest(text)
                val response = gptApiManager.createCompletion(gptRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val action = response.body()?.choices?.firstOrNull()?.message?.content?.trim()?.lowercase() ?: "Unknown command"
                        processSpeechResults(action, withHelp = false)
                        _speechRecognitionState.postValue(SpeechRecognitionState(isProcessing = false))
                    } else {
                        Log.d("AppViewModel", "processWithGptApi: ${response.errorBody()?.string()}")
                        processSpeechError("GPT-3 API call failed")
                        _speechRecognitionState.postValue(SpeechRecognitionState(error = response.errorBody()?.string(), isProcessing = false))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("AppViewModel", "processWithGptApi: ${e.message}")
                    processSpeechError("Network error: ${e.message}")
                    _speechRecognitionState.postValue(SpeechRecognitionState(error = e.message, isProcessing = false))
                }
            }
        }
    }

    private fun createGptRequest(text: String): GptRequest {
        val content = createGptContent(text)
        val messages = listOf(mapOf("role" to "user", "content" to content))
        return GptRequest(model = "gpt-3.5-turbo", temperature = 0.7, messages = messages)
    }

    private fun createGptContent(text: String): String {
        return "Match the following text to the closest corresponding action, " +
                "and give me only the action as a response. If it matches no particular item in the list of actions, " +
                "reply with 'Unknown command':\n" +
                "Text: $text\n" +
                "Action: light on, light off, open door, close door, open window, close window"
    }

    private fun processSpeechResults(text: String, withHelp: Boolean = true) {
        _speechRecognitionState.postValue(SpeechRecognitionState(isSpeaking = false, isProcessing = true))
        Log.d("AppViewModel", "processSpeechResults: $text")
        when (text) {
            "light on" -> setLightState(true)
            "light off" -> setLightState(false)
            "close door" -> setDoorState(false)
            "open door" -> setDoorState(true)
            "close window" -> setWindowState(false)
            "open window" -> setWindowState(true)
            else -> {
                if (withHelp) {
                    Log.d("AppViewModel", "processSpeechResults: with help")
                    processWithGptApi(text)
                } else {
                    Log.d("AppViewModel", "processSpeechResults: unknown command")
                    processSpeechError("Unknown command")
                    _speechRecognitionState.postValue(SpeechRecognitionState(isProcessing = false))
                }
            }
        }
    }

    private fun processSpeechError(error: String) {
        _speechRecognitionState.postValue(SpeechRecognitionState(isSpeaking = false, error = error))
        Log.d("AppViewModel", "processSpeechError: $error")
    }

    private fun initFirebase() {
        FirebaseAPIService.onLightStateChanged = { state -> _lightState.postValue(state) }
        FirebaseAPIService.onDoorStateChanged = { state -> _doorState.postValue(state) }
        FirebaseAPIService.onWindowStateChanged = { state -> _windowState.postValue(state) }
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAPIService.onLightStateChanged = null
        FirebaseAPIService.onDoorStateChanged = null
        FirebaseAPIService.onWindowStateChanged = null
    }
}
