package com.example.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import com.example.ui.components.AudioPlayerBarComposable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.draw.scale
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.mutableIntStateOf
import coil.request.ImageRequest
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.MascotEmotion
import com.example.ui.components.ZeTraquinaMascot
import com.example.ui.navigation.Screen
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val userProgress by viewModel.userProgress.collectAsState()
    var isAudioPlaying by remember { mutableStateOf(false) }

    // Conditional derived state for mascot emotion to prevent freezing / re-rendering on unneeded state changes
    val mascotEmotion by remember {
        derivedStateOf {
            if (userProgress.starsCount >= 50) MascotEmotion.PROUD else MascotEmotion.HAPPY
        }
    }

    // Floating Zé Traquina tips state
    val zeTips = remember {
        listOf(
            "Sabias que podes jogar o Jogo da Memória na aba Jogos? 🧠",
            "Vem aprender os números e o ABC na aba Aprender! 🅰️1️⃣",
            "Ouve as canções super divertidas na aba Música! 🎵",
            "Ganha estrelas douradas completando os desafios! ⭐🏆",
            "Assiste aos vídeos do Zé Traquina na aba Vídeos! 🎥",
            "Clica no meu avatar para ouvires frases especiais! 🗣️✨"
        )
    }
    var currentTipIndex by remember { mutableIntStateOf(0) }
    var isTipVisible by remember { mutableStateOf(false) }

    // Periodic tip trigger effect
    LaunchedEffect(Unit) {
        delay(2500L) // Wait 2.5 seconds after screen loads
        while (true) {
            isTipVisible = true
            delay(7000L) // Display tip for 7 seconds
            isTipVisible = false
            delay(11000L) // Wait 11 seconds before showing next tip
            currentTipIndex = (currentTipIndex + 1) % zeTips.size
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 100.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .testTag("home_screen"),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // --- Hero Banner Image (YouTube Channel Header Style) ---
        item(key = "hero_banner") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.header_pixar_polo_verde_1784980470585),
                    contentDescription = "Universo Zé Traquina Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // --- 2. Mascot Interactive Card ---
        item(key = "mascot_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mascot_home_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ZeTraquinaMascot(
                        size = 42.dp,
                        showSpeechBubble = false,
                        emotion = mascotEmotion,
                        triggerEmotionKey = userProgress.starsCount,
                        onInteract = {
                            viewModel.speak("Olá amiguinhos! Tens ${userProgress.starsCount} estrelas reluzentes!")
                            viewModel.addStars(1)
                        }
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Olá! Vamos aprender?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Toque no Zé ou converse com a ZéAI!",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFF4081),
                        modifier = Modifier.clickable { onNavigate(Screen.Chat) }
                    ) {
                        Text(
                            text = "ZéAI 💬",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // --- 4. Quick Menu Section (2x2 Grid) ---
        item(key = "quick_menu") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "O que vamos fazer hoje? 🚀",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickMenuCard(
                        title = "Aprender",
                        subtitle = "Letras, Números e Palavras",
                        icon = Icons.Default.School,
                        bgColor = SkyBluePrimary,
                        testTag = "home_quick_learn",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Learn) }
                    )
                    QuickMenuCard(
                        title = "Jogos",
                        subtitle = "Memória, Pintura e Cores",
                        icon = Icons.Default.SportsEsports,
                        bgColor = MintGreen,
                        testTag = "home_quick_games",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Games) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickMenuCard(
                        title = "Vídeos",
                        subtitle = "Músicas, Desenhos e Histórias",
                        icon = Icons.Default.OndemandVideo,
                        bgColor = Color(0xFFF59E0B), // Rich golden yellow
                        testTag = "home_quick_media",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Media) }
                    )
                    QuickMenuCard(
                        title = "ZéAI",
                        subtitle = "Falar com o Zé Traquina",
                        icon = Icons.Default.Face,
                        bgColor = Color(0xFFE53935), // Red
                        testTag = "home_quick_chat",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Chat) }
                    )
                }
            }
        }

        // --- 5. Destaques de Aprendizado do Dia ---
        item(key = "highlights") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Destaques do Dia 🌟",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    Text(
                        text = "Toque para ouvir 🔊",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyBluePrimary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HighlightCard(
                        category = "LETRA",
                        symbol = "A",
                        emoji = "🐝",
                        title = "Abelha",
                        badgeColor = SkyBluePrimary,
                        containerColor = Color(0xFFE0F2FE),
                        testTag = "highlight_card_letter",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.speak("Letra A de Abelha!") }
                    )

                    HighlightCard(
                        category = "NÚMERO",
                        symbol = "5",
                        emoji = "⭐",
                        title = "Estrelas",
                        badgeColor = Color(0xFFFF4081),
                        containerColor = Color(0xFFFCE4EC),
                        testTag = "highlight_card_number",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.speak("Número 5! Cinco estrelinhas reluzentes!") }
                    )
                }


            }
        }

        // --- 3. Música do Dia Compact Audio Player Bar ---
        item(key = "audio_player") {
            AudioPlayerBarComposable(
                isPlayingState = isAudioPlaying,
                onPlayStateChanged = { playing ->
                    isAudioPlaying = playing
                    if (playing) {
                        viewModel.addStars(2)
                        viewModel.speak("A tocar a música oficial: Zé Traquina é fixe!")
                    }
                }
            )
        }

        // --- 6. Bottom Tagline ---
        item(key = "bottom_tagline") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = KidStarOrange,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Aprender, brincar e cantar juntos! ✨",
                    color = Color.Black.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }

        // Floating Tip at the bottom of the screen
        FloatingZeTipBubble(
            tipText = zeTips[currentTipIndex],
            isVisible = isTipVisible,
            onDismiss = { isTipVisible = false },
            onClick = {
                val tip = zeTips[currentTipIndex]
                viewModel.speak(tip)
                viewModel.addStars(1)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .zIndex(10f)
        )
}
}

@Composable
fun QuickMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    bgColor: Color,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 1.06f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "quickMenuCardScale"
    )

    Card(
        modifier = modifier
            .height(102.dp)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 8.dp else 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun FloatingZeTipBubble(
    tipText: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mascotImageRequest = remember(context) {
        ImageRequest.Builder(context)
            .data(R.drawable.img_ze_mascot)
            .crossfade(true)
            .build()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn() + scaleIn(initialScale = 0.8f),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut() + scaleOut(targetScale = 0.8f),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clickable { onClick() }
                .testTag("floating_ze_tip_bubble"),
            shape = RoundedCornerShape(24.dp),
            color = SunshineYellow,
            shadowElevation = 8.dp,
            border = BorderStroke(3.dp, MintGreen)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mascot 3D Avatar Badge
                Surface(
                    shape = CircleShape,
                    color = SkyBluePrimary,
                    modifier = Modifier
                        .size(42.dp)
                        .border(2.dp, Color.White, CircleShape)
                ) {
                    AsyncImage(
                        model = mascotImageRequest,
                        contentDescription = "Dica do Zé Traquina",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Speech Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Dica do Zé!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Ouvir dica",
                            tint = Color(0xFF0D47A1),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = tipText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1),
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Close Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar Dica",
                        tint = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HighlightCard(
    category: String,
    symbol: String,
    emoji: String,
    title: String,
    badgeColor: Color,
    containerColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "highlight_card_scale"
    )

    Card(
        modifier = modifier
            .height(72.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 4.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Framed 3D Badge Rectangle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(width = 46.dp, height = 46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = badgeColor.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = symbol,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = badgeColor,
                        textAlign = TextAlign.Center
                    )
                    if (emoji.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = emoji,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = category,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = badgeColor,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


