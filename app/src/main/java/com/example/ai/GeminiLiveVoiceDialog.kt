package com.example.ai

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun GeminiLiveVoiceDialog(
    aiViewModel: GeminiAiViewModel,
    onDismiss: () -> Unit
) {
    val voiceState by aiViewModel.voiceState.collectAsState()
    val liveTranscript by aiViewModel.liveTranscript.collectAsState()
    val soundLevel by aiViewModel.soundLevel.collectAsState()
    val persona by aiViewModel.selectedPersona.collectAsState()
    val voiceError by aiViewModel.voiceError.collectAsState()

    // Pulse animation for the glowing orb
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val orbRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbRotation"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF1E293B),
                            Color(0xFF0A0F1D)
                        )
                    )
                ),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(24.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF10B981),
                                modifier = Modifier.size(10.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gemini Live Voice",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = persona.title,
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Voice Mode",
                            tint = Color.White
                        )
                    }
                }

                // Center Animated Voice Visualizer
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Glowing Orb
                    Box(
                        modifier = Modifier
                            .size(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Dynamic canvas for soundwave ripples
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2, size.height / 2)
                            val baseRadius = size.minDimension / 3.2f
                            val dynamicBoost = soundLevel * 40f

                            // Outer ambient ring
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        when (voiceState) {
                                            VoiceState.LISTENING -> Color(0xFF38BDF8).copy(alpha = 0.35f)
                                            VoiceState.PROCESSING -> Color(0xFFA855F7).copy(alpha = 0.4f)
                                            VoiceState.SPEAKING -> Color(0xFF10B981).copy(alpha = 0.45f)
                                            else -> Color(0xFF64748B).copy(alpha = 0.2f)
                                        },
                                        Color.Transparent
                                    ),
                                    center = center,
                                    radius = baseRadius * 1.8f + dynamicBoost
                                ),
                                center = center,
                                radius = baseRadius * 1.8f + dynamicBoost
                            )

                            // Middle glowing layer
                            drawCircle(
                                color = when (voiceState) {
                                    VoiceState.LISTENING -> Color(0xFF0284C7).copy(alpha = 0.5f)
                                    VoiceState.PROCESSING -> Color(0xFF7E22CE).copy(alpha = 0.5f)
                                    VoiceState.SPEAKING -> Color(0xFF059669).copy(alpha = 0.5f)
                                    else -> Color(0xFF475569).copy(alpha = 0.3f)
                                },
                                center = center,
                                radius = baseRadius + dynamicBoost
                            )
                        }

                        // Core Glowing Button / Visual Icon
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .scale(if (voiceState == VoiceState.LISTENING) 1f + soundLevel * 0.3f else pulseScale * 0.95f)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = when (voiceState) {
                                            VoiceState.LISTENING -> listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8))
                                            VoiceState.PROCESSING -> listOf(Color(0xFF9333EA), Color(0xFFC084FC))
                                            VoiceState.SPEAKING -> listOf(Color(0xFF10B981), Color(0xFF34D399))
                                            else -> listOf(Color(0xFF334155), Color(0xFF475569))
                                        }
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (voiceState) {
                                    VoiceState.LISTENING -> Icons.Default.Mic
                                    VoiceState.PROCESSING -> Icons.Default.AutoAwesome
                                    VoiceState.SPEAKING -> Icons.Default.VolumeUp
                                    else -> Icons.Default.MicNone
                                },
                                contentDescription = "Voice State",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Status Text Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when (voiceState) {
                            VoiceState.LISTENING -> Color(0xFF0284C7).copy(alpha = 0.25f)
                            VoiceState.PROCESSING -> Color(0xFF7E22CE).copy(alpha = 0.25f)
                            VoiceState.SPEAKING -> Color(0xFF059669).copy(alpha = 0.25f)
                            else -> Color.White.copy(alpha = 0.1f)
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            when (voiceState) {
                                VoiceState.LISTENING -> Color(0xFF38BDF8)
                                VoiceState.PROCESSING -> Color(0xFFA855F7)
                                VoiceState.SPEAKING -> Color(0xFF34D399)
                                else -> Color(0xFF64748B)
                            }
                        )
                    ) {
                        Text(
                            text = when (voiceState) {
                                VoiceState.LISTENING -> "Listening to your voice..."
                                VoiceState.PROCESSING -> "Thinking & generating answer..."
                                VoiceState.SPEAKING -> "Hilltop AI is speaking..."
                                else -> "Tap microphone below to talk"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Live Transcript card
                    if (liveTranscript.isNotBlank()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.08f)
                            )
                        ) {
                            Text(
                                text = "\"$liveTranscript\"",
                                color = Color(0xFFE2E8F0),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }

                    if (voiceError != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = voiceError ?: "",
                            color = Color(0xFFF87171),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                // Bottom Control Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stop audio / interrupt button
                    FilledTonalIconButton(
                        onClick = {
                            aiViewModel.stopSpeaking()
                            aiViewModel.stopListening()
                        },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.12f),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop speaking"
                        )
                    }

                    // Main mic toggle button
                    FloatingActionButton(
                        onClick = {
                            if (voiceState == VoiceState.LISTENING) {
                                aiViewModel.stopListening()
                            } else {
                                aiViewModel.startListening()
                            }
                        },
                        containerColor = if (voiceState == VoiceState.LISTENING) Color(0xFFEF4444) else Color(0xFF2563EB),
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Icon(
                            imageVector = if (voiceState == VoiceState.LISTENING) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Toggle Mic",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Done / Exit button
                    FilledTonalIconButton(
                        onClick = onDismiss,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.12f),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done"
                        )
                    }
                }
            }
        }
    }
}
