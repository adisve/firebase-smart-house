package android.project.firebasesmarthouse.interfaces

import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

interface SpeechRecognitionAPI: RecognitionListener {
    fun startListening(languageCode: String)

    fun stopListening()
}