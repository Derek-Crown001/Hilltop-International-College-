package com.example.ai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class VoiceState {
    IDLE,
    LISTENING,
    PROCESSING,
    SPEAKING,
    ERROR
}

class VoiceConversationManager(private val context: Context) {
    private val TAG = "VoiceConvManager"

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsInitialized = false

    private val _voiceState = MutableStateFlow(VoiceState.IDLE)
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    private val _liveTranscript = MutableStateFlow("")
    val liveTranscript: StateFlow<String> = _liveTranscript.asStateFlow()

    private val _soundLevel = MutableStateFlow(0f)
    val soundLevel: StateFlow<Float> = _soundLevel.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    var onUserSpoken: ((String) -> Unit)? = null
    var isContinuousMode = false

    init {
        initTts()
    }

    private fun initTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w(TAG, "English TTS language not supported, defaulting to system default")
                    textToSpeech?.setLanguage(Locale.getDefault())
                }
                textToSpeech?.setPitch(1.05f)
                textToSpeech?.setSpeechRate(0.98f)
                isTtsInitialized = true

                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _voiceState.value = VoiceState.SPEAKING
                    }

                    override fun onDone(utteranceId: String?) {
                        _voiceState.value = VoiceState.IDLE
                        if (isContinuousMode) {
                            startListening()
                        }
                    }

                    override fun onError(utteranceId: String?) {
                        _voiceState.value = VoiceState.IDLE
                    }
                })
            }
        }
    }

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _errorMessage.value = "Speech recognition is not available on this device."
            _voiceState.value = VoiceState.ERROR
            return
        }

        stopSpeaking()

        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _voiceState.value = VoiceState.LISTENING
                    _liveTranscript.value = "Listening to your voice..."
                    _errorMessage.value = null
                }

                override fun onBeginningOfSpeech() {
                    _liveTranscript.value = "Hearing speech..."
                }

                override fun onRmsChanged(rmsdB: Float) {
                    // Normalize RMS to 0.0 - 1.0
                    val normalized = ((rmsdB + 2f) / 12f).coerceIn(0.1f, 1f)
                    _soundLevel.value = normalized
                }

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    _voiceState.value = VoiceState.PROCESSING
                    _soundLevel.value = 0f
                }

                override fun onError(error: Int) {
                    val message = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Tap mic to try again."
                        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network error during speech recognition."
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error. Check microphone permission."
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required."
                        else -> "Speech recognition error ($error)"
                    }
                    _soundLevel.value = 0f
                    if (error != SpeechRecognizer.ERROR_NO_MATCH) {
                        _errorMessage.value = message
                    }
                    _voiceState.value = VoiceState.IDLE
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val spokenText = matches?.firstOrNull()?.trim() ?: ""
                    if (spokenText.isNotBlank()) {
                        _liveTranscript.value = spokenText
                        _voiceState.value = VoiceState.PROCESSING
                        onUserSpoken?.invoke(spokenText)
                    } else {
                        _voiceState.value = VoiceState.IDLE
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull() ?: ""
                    if (text.isNotBlank()) {
                        _liveTranscript.value = text
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        try {
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            _errorMessage.value = "Failed to start listening: ${e.message}"
            _voiceState.value = VoiceState.ERROR
        }
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _soundLevel.value = 0f
        if (_voiceState.value == VoiceState.LISTENING) {
            _voiceState.value = VoiceState.IDLE
        }
    }

    fun speak(text: String, utteranceId: String = "gemini_voice_reply") {
        if (!isTtsInitialized || textToSpeech == null) {
            Log.w(TAG, "TTS not ready yet")
            return
        }

        stopListening()
        _voiceState.value = VoiceState.SPEAKING
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stopSpeaking() {
        textToSpeech?.stop()
        if (_voiceState.value == VoiceState.SPEAKING) {
            _voiceState.value = VoiceState.IDLE
        }
    }

    fun release() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
