package com.example.ai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeminiAiViewModel(application: Application) : AndroidViewModel(application) {

    private val voiceManager = VoiceConversationManager(application.applicationContext)

    private val _selectedPersona = MutableStateFlow(AiPersonas.ACADEMIC_TUTOR)
    val selectedPersona: StateFlow<AiPersona> = _selectedPersona.asStateFlow()

    private val _selectedModel = MutableStateFlow(GeminiModel.FLASH)
    val selectedModel: StateFlow<GeminiModel> = _selectedModel.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Voice Conversation state
    val voiceState = voiceManager.voiceState
    val liveTranscript = voiceManager.liveTranscript
    val soundLevel = voiceManager.soundLevel
    val voiceError = voiceManager.errorMessage

    private val _isVoiceModalOpen = MutableStateFlow(false)
    val isVoiceModalOpen: StateFlow<Boolean> = _isVoiceModalOpen.asStateFlow()

    init {
        // Setup default greeting from selected persona
        resetWithPersona(_selectedPersona.value)

        // Wire voice speech callback
        voiceManager.onUserSpoken = { spokenText ->
            handleVoiceSpoken(spokenText)
        }
    }

    fun selectPersona(persona: AiPersona) {
        _selectedPersona.value = persona
        _selectedModel.value = persona.defaultModel
        resetWithPersona(persona)
    }

    fun selectModel(model: GeminiModel) {
        _selectedModel.value = model
    }

    private fun resetWithPersona(persona: AiPersona) {
        val initialGreeting = when (persona.id) {
            AiPersonas.ACADEMIC_TUTOR.id -> "Hello! I am your Hilltop Academic AI Tutor. I can help you solve Mathematics, Physics, Chemistry, English, and WAEC/JAMB past questions. What topic would you like to explore today?"
            AiPersonas.PARENT_ADVISOR.id -> "Welcome! I am the Admissions & Parent Advisor for Hilltop Model College. Ask me anything about term dates, admission applications, boarding facilities, school bus routes, or school fees."
            AiPersonas.TEACHER_ASSISTANT.id -> "Hello Colleague! I am your Teaching & Curriculum Assistant. I can help you draft Ministry-standard lesson plans, generate CBT questions, formulate marking schemes, and write report card remarks."
            AiPersonas.COUNSELOR.id -> "Hello! I am your Guidance and Wellness Counselor. Whether you need study timetable strategies, career guidance, or stress management tips for exams, I'm here for you."
            else -> "Hello! How can I assist you with your academics at Hilltop College today?"
        }

        _messages.value = listOf(
            ChatMessage(
                sender = MessageSender.AI,
                text = initialGreeting,
                modelUsed = _selectedModel.value.displayName
            )
        )
    }

    fun clearChat() {
        voiceManager.stopSpeaking()
        voiceManager.stopListening()
        resetWithPersona(_selectedPersona.value)
        _errorMessage.value = null
    }

    fun sendMessage(userText: String) {
        val text = userText.trim()
        if (text.isBlank() || _isLoading.value) return

        val userMessage = ChatMessage(
            sender = MessageSender.USER,
            text = text
        )

        val updatedList = _messages.value + userMessage
        _messages.value = updatedList
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = GeminiApiService.generateChatResponse(
                model = _selectedModel.value,
                systemPrompt = _selectedPersona.value.systemPrompt,
                messages = updatedList
            )

            _isLoading.value = false

            result.onSuccess { aiReply ->
                _messages.value = _messages.value + ChatMessage(
                    sender = MessageSender.AI,
                    text = aiReply,
                    modelUsed = _selectedModel.value.displayName
                )
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Failed to get AI response"
                _messages.value = _messages.value + ChatMessage(
                    sender = MessageSender.AI,
                    text = "I encountered an error: ${err.message}. Please check your connection or API key.",
                    modelUsed = _selectedModel.value.displayName,
                    isError = true
                )
            }
        }
    }

    private fun handleVoiceSpoken(spokenText: String) {
        val text = spokenText.trim()
        if (text.isBlank()) return

        val userMessage = ChatMessage(
            sender = MessageSender.USER,
            text = text
        )
        val updatedList = _messages.value + userMessage
        _messages.value = updatedList
        _isLoading.value = true

        viewModelScope.launch {
            val result = GeminiApiService.generateChatResponse(
                model = _selectedModel.value,
                systemPrompt = "${_selectedPersona.value.systemPrompt}\nKeep responses concise and spoken-friendly since the user is in a live voice conversation.",
                messages = updatedList
            )

            _isLoading.value = false

            result.onSuccess { aiReply ->
                _messages.value = _messages.value + ChatMessage(
                    sender = MessageSender.AI,
                    text = aiReply,
                    modelUsed = _selectedModel.value.displayName
                )
                // Speak out the reply in voice mode
                voiceManager.speak(aiReply)
            }.onFailure { err ->
                val errorNotice = "Sorry, I could not complete that request: ${err.message}"
                _messages.value = _messages.value + ChatMessage(
                    sender = MessageSender.AI,
                    text = errorNotice,
                    modelUsed = _selectedModel.value.displayName,
                    isError = true
                )
                voiceManager.speak(errorNotice)
            }
        }
    }

    // Voice Modal Controls
    fun openVoiceModal() {
        _isVoiceModalOpen.value = true
        voiceManager.isContinuousMode = true
        voiceManager.startListening()
    }

    fun closeVoiceModal() {
        _isVoiceModalOpen.value = false
        voiceManager.isContinuousMode = false
        voiceManager.stopSpeaking()
        voiceManager.stopListening()
    }

    fun startListening() {
        voiceManager.startListening()
    }

    fun stopListening() {
        voiceManager.stopListening()
    }

    fun speakMessage(text: String) {
        voiceManager.speak(text)
    }

    fun stopSpeaking() {
        voiceManager.stopSpeaking()
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.release()
    }
}
