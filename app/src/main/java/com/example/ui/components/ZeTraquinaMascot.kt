package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Emotional states for Zé Traquina character mascot
 */
enum class MascotEmotion {
    HAPPY,          // Standard happy smile
    CELEBRATING,    // Quiz completion / victory celebration
    EXCITED,        // Gaining stars / progression
    PROUD,          // Level up or high achievement
    THINKING        // Quiz in progress / reflection
}

/**
 * Interactive Zé Traquina mascot character component using Coil for loading local image assets
 * with emotional state animations (celebration bounce, rotation, glowing borders, badges, particle bursts & dynamic speech bubbles).
 */
@Composable
fun ZeTraquinaMascot(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    showSpeechBubble: Boolean = true,
    customPhrase: String? = null,
    emotion: MascotEmotion = MascotEmotion.HAPPY,
    triggerEmotionKey: Any? = null,
    onInteract: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Interactive & active emotion state
    var activeEmotion by remember { mutableStateOf(emotion) }
    var isInteracting by remember { mutableStateOf(false) }
    var interactionCount by remember { mutableStateOf(0) }
    var showParticles by remember { mutableStateOf(false) }
    var phraseIndex by remember { mutableStateOf(0) }

    // Sync external emotion parameter or trigger key changes (e.g. quiz completed, stars earned)
    LaunchedEffect(emotion, triggerEmotionKey) {
        if (triggerEmotionKey != null && triggerEmotionKey != false) {
            activeEmotion = MascotEmotion.CELEBRATING
            showParticles = true
            isInteracting = true
            delay(3500)
            showParticles = false
            isInteracting = false
            activeEmotion = emotion
        } else {
            activeEmotion = emotion
        }
    }

    val defaultPhrases = remember {
        listOf(
            "Olá amiguinho! Vamos brincar e aprender hoje? 🎒✨",
            "És um super campeão! Ganhaste uma estrela! ⭐",
            "Adoro aprender contigo! Vamos a isto? 🚀",
            "Sorri e diverte-te no Universo Zé Traquina! 🎉"
        )
    }

    // Dynamic phrase computed efficiently via derivedStateOf
    val emotionPhrase by remember {
        derivedStateOf {
            when (activeEmotion) {
                MascotEmotion.CELEBRATING -> "FANTÁSTICO! Acertaste em cheio! PARABÉNS! 🎉🏆✨"
                MascotEmotion.EXCITED -> "UAU! Ganhaste mais estrelas reluzentes! ⭐🚀"
                MascotEmotion.PROUD -> "Estou super orgulhoso de ti, campeão! 👑🌟"
                MascotEmotion.THINKING -> "Hummm... pensa bem! Tu consegues acertar! 💭🧠"
                MascotEmotion.HAPPY -> defaultPhrases[phraseIndex % defaultPhrases.size]
            }
        }
    }

    val currentSpeech by remember {
        derivedStateOf { customPhrase ?: emotionPhrase }
    }

    // Continuous animation for active emotions (CELEBRATING, EXCITED, THINKING)
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteMascot")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = when (activeEmotion) {
            MascotEmotion.CELEBRATING -> 1.15f
            MascotEmotion.EXCITED -> 1.10f
            MascotEmotion.PROUD -> 1.06f
            MascotEmotion.THINKING -> 1.03f
            MascotEmotion.HAPPY -> 1.02f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (activeEmotion == MascotEmotion.CELEBRATING) 350 else 900,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val wobbleRotation by infiniteTransition.animateFloat(
        initialValue = when (activeEmotion) {
            MascotEmotion.CELEBRATING -> -12f
            MascotEmotion.THINKING -> -8f
            else -> -3f
        },
        targetValue = when (activeEmotion) {
            MascotEmotion.CELEBRATING -> 12f
            MascotEmotion.THINKING -> 8f
            else -> 3f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (activeEmotion == MascotEmotion.CELEBRATING) 300 else 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobbleRotation"
    )

    // Interactive scale spring animation
    val interactiveScale by animateFloatAsState(
        targetValue = if (isInteracting) 1.25f else pulseScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "mascotScale"
    )

    val currentRotation by remember {
        derivedStateOf {
            if (isInteracting) {
                if (interactionCount % 2 == 0) -16f else 16f
            } else {
                wobbleRotation
            }
        }
    }

    // Dynamic theme colors derived efficiently without re-allocating Brushes
    val borderBrush by remember {
        derivedStateOf {
            when (activeEmotion) {
                MascotEmotion.CELEBRATING -> Brush.sweepGradient(
                    listOf(SunshineYellow, Color(0xFFFF4081), MintGreen, SkyBluePrimary, SunshineYellow)
                )
                MascotEmotion.EXCITED -> Brush.linearGradient(
                    listOf(Color(0xFFFF9800), SunshineYellow, Color(0xFFFF4081))
                )
                MascotEmotion.PROUD -> Brush.linearGradient(
                    listOf(SunshineYellow, KidStarOrange, SunshineYellow)
                )
                MascotEmotion.THINKING -> Brush.linearGradient(
                    listOf(SkyBluePrimary, Color(0xFFB39DDB), SkyBluePrimary)
                )
                MascotEmotion.HAPPY -> Brush.sweepGradient(
                    listOf(SunshineYellow, Color(0xFFFF9800), SkyBluePrimary, SunshineYellow)
                )
            }
        }
    }

    val backgroundBrush by remember {
        derivedStateOf {
            Brush.radialGradient(
                colors = listOf(
                    when (activeEmotion) {
                        MascotEmotion.CELEBRATING -> SunshineYellow
                        MascotEmotion.EXCITED -> Color(0xFFFF80AB)
                        MascotEmotion.PROUD -> SunshineYellow
                        MascotEmotion.THINKING -> SkyBluePrimary.copy(alpha = 0.6f)
                        MascotEmotion.HAPPY -> SunshineYellow
                    },
                    SkyBluePrimary.copy(alpha = 0.8f)
                )
            )
        }
    }

    // Cached Coil ImageRequest to prevent re-instantiation and freezing during recomposition
    val mascotImageRequest = remember(context) {
        ImageRequest.Builder(context)
            .data(R.drawable.img_app_icon_1785061192574)
            .crossfade(true)
            .build()
    }

    // Badge icon derived state
    val badgeIcon by remember {
        derivedStateOf {
            when (activeEmotion) {
                MascotEmotion.CELEBRATING -> "🏆"
                MascotEmotion.EXCITED -> "⚡"
                MascotEmotion.PROUD -> "👑"
                MascotEmotion.THINKING -> "💭"
                MascotEmotion.HAPPY -> null
            }
        }
    }

    // Particle visibility derived state
    val isParticlesVisible by remember {
        derivedStateOf {
            showParticles || activeEmotion == MascotEmotion.CELEBRATING || activeEmotion == MascotEmotion.EXCITED
        }
    }

    Row(
        modifier = modifier.testTag("ze_traquina_mascot"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mascot Container with Coil AsyncImage and Particle Overlay
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = interactiveScale
                    scaleY = interactiveScale
                    rotationZ = currentRotation
                }
                .clip(CircleShape)
                .background(backgroundBrush)
                .border(
                    width = if (activeEmotion == MascotEmotion.CELEBRATING) 4.dp else 3.dp,
                    brush = borderBrush,
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    interactionCount++
                    isInteracting = true
                    showParticles = true
                    phraseIndex++

                    if (activeEmotion == MascotEmotion.HAPPY) {
                        activeEmotion = MascotEmotion.EXCITED
                    }

                    onInteract?.invoke()

                    coroutineScope.launch {
                        delay(300)
                        isInteracting = false
                        delay(1500)
                        showParticles = false
                        if (activeEmotion == MascotEmotion.EXCITED && emotion == MascotEmotion.HAPPY) {
                            activeEmotion = MascotEmotion.HAPPY
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Local image asset displayed via cached Coil AsyncImage
            AsyncImage(
                model = mascotImageRequest,
                contentDescription = "Mascote Zé Traquina",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )

            // Emotion Overlay Badge Icon (Top-Right of Mascot)
            badgeIcon?.let { badge ->
                Surface(
                    shape = CircleShape,
                    color = SunshineYellow,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = (size.value * 0.28f).sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }

            // Floating Star/Heart/Sparkle Particles State Animation
            androidx.compose.animation.AnimatedVisibility(
                visible = isParticlesVisible,
                enter = fadeIn() + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                exit = fadeOut() + scaleOut()
            ) {
                Box(modifier = Modifier.size(size)) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = KidStarOrange,
                        modifier = Modifier
                            .size(22.dp)
                            .align(Alignment.TopStart)
                            .offset(x = (-6).dp, y = (-6).dp)
                    )
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = SunshineYellow,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = 6.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFFF4081),
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 6.dp, y = 6.dp)
                    )
                    if (activeEmotion == MascotEmotion.CELEBRATING) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = SunshineYellow,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.BottomStart)
                                .offset(x = (-6).dp, y = 6.dp)
                        )
                    }
                }
            }
        }

        if (showSpeechBubble) {
            Spacer(modifier = Modifier.width(12.dp))

            // Speech Bubble with Dynamic Emotion Styling
            androidx.compose.animation.AnimatedVisibility(
                visible = true,
                enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
                    color = when (activeEmotion) {
                        MascotEmotion.CELEBRATING -> SunshineYellow
                        MascotEmotion.EXCITED -> MintGreen
                        MascotEmotion.PROUD -> Color(0xFFFFF3E0)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    tonalElevation = 6.dp,
                    shadowElevation = 4.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Zé Traquina",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = if (activeEmotion == MascotEmotion.CELEBRATING) Color.Black else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (activeEmotion) {
                                    MascotEmotion.CELEBRATING -> "🎉🥳"
                                    MascotEmotion.EXCITED -> "🚀⭐"
                                    MascotEmotion.PROUD -> "👑🏆"
                                    MascotEmotion.THINKING -> "💭🤔"
                                    MascotEmotion.HAPPY -> "🧢👦"
                                },
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            text = currentSpeech,
                            fontSize = 12.sp,
                            fontWeight = if (activeEmotion == MascotEmotion.CELEBRATING) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (activeEmotion == MascotEmotion.CELEBRATING) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

