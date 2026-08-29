package com.example.ai

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiChatModal(
    aiViewModel: GeminiAiViewModel,
    onDismiss: () -> Unit
) {
    val messages by aiViewModel.messages.collectAsState()
    val selectedPersona by aiViewModel.selectedPersona.collectAsState()
    val selectedModel by aiViewModel.selectedModel.collectAsState()
    val isLoading by aiViewModel.isLoading.collectAsState()
    val isVoiceModalOpen by aiViewModel.isVoiceModalOpen.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var showModelMenu by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Auto scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    if (isVoiceModalOpen) {
        GeminiLiveVoiceDialog(
            aiViewModel = aiViewModel,
            onDismiss = { aiViewModel.closeVoiceModal() }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Hilltop Gemini AI",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = selectedModel.badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = selectedPersona.title,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Close Gemini AI"
                            )
                        }
                    },
                    actions = {
                        // Launch Live Voice Mode button
                        FilledTonalButton(
                            onClick = { aiViewModel.openVoiceModal() },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AudioFile,
                                contentDescription = "Live Voice",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Voice", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Model Selector dropdown
                        Box {
                            IconButton(onClick = { showModelMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Select Gemini Model"
                                )
                            }

                            DropdownMenu(
                                expanded = showModelMenu,
                                onDismissRequest = { showModelMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text("Gemini 3.5 Flash", fontWeight = FontWeight.Bold)
                                            Text("General Q&A, lesson assistance", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                        }
                                    },
                                    onClick = {
                                        aiViewModel.selectModel(GeminiModel.FLASH)
                                        showModelMenu = false
                                    },
                                    leadingIcon = {
                                        RadioButton(
                                            selected = selectedModel == GeminiModel.FLASH,
                                            onClick = null
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text("Gemini 3.1 Pro", fontWeight = FontWeight.Bold)
                                            Text("Complex STEM, WAEC past questions", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                        }
                                    },
                                    onClick = {
                                        aiViewModel.selectModel(GeminiModel.PRO)
                                        showModelMenu = false
                                    },
                                    leadingIcon = {
                                        RadioButton(
                                            selected = selectedModel == GeminiModel.PRO,
                                            onClick = null
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text("Gemini 3.1 Flash Lite", fontWeight = FontWeight.Bold)
                                            Text("Ultra fast short responses", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                        }
                                    },
                                    onClick = {
                                        aiViewModel.selectModel(GeminiModel.LITE)
                                        showModelMenu = false
                                    },
                                    leadingIcon = {
                                        RadioButton(
                                            selected = selectedModel == GeminiModel.LITE,
                                            onClick = null
                                        )
                                    }
                                )
                            }
                        }

                        // Clear Chat button
                        IconButton(onClick = { aiViewModel.clearChat() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Clear Chat"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Horizontal Persona selector chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AiPersonas.all) { persona ->
                        val isSelected = selectedPersona.id == persona.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { aiViewModel.selectPersona(persona) },
                            label = {
                                Text(
                                    text = persona.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = when (persona.id) {
                                        AiPersonas.ACADEMIC_TUTOR.id -> Icons.Default.School
                                        AiPersonas.PARENT_ADVISOR.id -> Icons.Default.FamilyRestroom
                                        AiPersonas.TEACHER_ASSISTANT.id -> Icons.Default.MenuBook
                                        else -> Icons.Default.Psychology
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }

                // Chat Messages Thread
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 760.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(messages, key = { it.id }) { message ->
                            ChatBubbleItem(
                                message = message,
                                onCopy = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Gemini Message", message.text)
                                    clipboard.setPrimaryClip(clip)
                                },
                                onSpeak = {
                                    aiViewModel.speakMessage(message.text)
                                }
                            )
                        }

                        if (isLoading) {
                            item {
                                Row(
                                    modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${selectedPersona.title} is thinking...",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Quick Prompt Starter Chips (when chat has few messages)
                if (messages.size <= 2) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(selectedPersona.suggestedPrompts) { prompt ->
                            SuggestionChip(
                                onClick = {
                                    aiViewModel.sendMessage(prompt)
                                },
                                label = {
                                    Text(text = prompt, fontSize = 12.sp)
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }

                // Input Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 3.dp,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 760.dp)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Quick Voice Assistant Button
                        IconButton(
                            onClick = { aiViewModel.openVoiceModal() },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Conversation",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Text Field Input
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Ask ${selectedPersona.title}...", fontSize = 14.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp, max = 120.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            ),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Send Button
                        FilledIconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    val text = inputText
                                    inputText = ""
                                    aiViewModel.sendMessage(text)
                                }
                            },
                            enabled = inputText.isNotBlank() && !isLoading,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send Message"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(
    message: ChatMessage,
    onCopy: () -> Unit,
    onSpeak: () -> Unit
) {
    val isUser = message.sender == MessageSender.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Top)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            modifier = Modifier.widthIn(max = 300.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) {
                    MaterialTheme.colorScheme.primary
                } else if (message.isError) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    color = if (isUser) {
                        MaterialTheme.colorScheme.onPrimary
                    } else if (message.isError) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isUser && message.modelUsed != null) {
                        Text(
                            text = message.modelUsed,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Row {
                        if (!isUser && !message.isError) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Read aloud",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onSpeak() },
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }

                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy message",
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onCopy() },
                            tint = if (isUser) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}
