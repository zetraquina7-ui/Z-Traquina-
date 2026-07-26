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

import androidx.compose.ui.res.painterResource
import androidx.compose.material3.MaterialTheme
import com.example.ui.components.MascotEmotion
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.components.ZeTraquinaMascot
import com.example.ui.navigation.Screen
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.delay



import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.filled.Refresh
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.filled.Help

data class ZeBalloonTip(
    val category: String,
    val badgeLabel: String,
    val icon: ImageVector,
    val badgeBgColor: Color,
    val badgeTextColor: Color,
    val text: String,
    val answer: String? = null,
    val emotion: MascotEmotion = MascotEmotion.HAPPY
)

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val userProgress by viewModel.userProgress.collectAsState()
    var isAudioPlaying by remember { mutableStateOf(false) }

    // Rich categorized full-time Zé Traquina balloon tips (Interleaved round-robin: Dica -> Conselho -> Sabias que -> Adivinha -> Anedota -> Desafio)
    val allZeTips = remember {
        listOf(
            // --- ROUND 1 ---
            ZeBalloonTip(
                category = "Dicas",
                badgeLabel = "DICA DO ZÉ 💡",
                icon = Icons.Default.Lightbulb,
                badgeBgColor = Color(0xFFFFECE0),
                badgeTextColor = Color(0xFFE65100),
                text = "Lava bem as mãos com água e sabão antes de comer para afastar os micróbios! 🧼",
                emotion = MascotEmotion.HAPPY
            ),
            ZeBalloonTip(
                category = "Conselhos",
                badgeLabel = "CONSELHO DO ZÉ 🌟",
                icon = Icons.Default.Star,
                badgeBgColor = Color(0xFFF3E8FF),
                badgeTextColor = Color(0xFF7C4DFF),
                text = "Partilha os teus brinquedos com os teus amigos! Brincar juntos é muito mais divertido! 🤝❤️",
                emotion = MascotEmotion.PROUD
            ),
            ZeBalloonTip(
                category = "Sabias que?",
                badgeLabel = "SABIAS QUE? ❓",
                icon = Icons.Default.AutoAwesome,
                badgeBgColor = Color(0xFFE0F2FE),
                badgeTextColor = Color(0xFF0288D1),
                text = "Sabias que as borboletas provam a comida usando as suas patinhas? 🦋🦶",
                emotion = MascotEmotion.EXCITED
            ),
            ZeBalloonTip(
                category = "Adivinhas",
                badgeLabel = "ADIVINHA DO ZÉ 🧩",
                icon = Icons.Default.SmartToy,
                badgeBgColor = Color(0xFFFCE4EC),
                badgeTextColor = Color(0xFFD81B60),
                text = "Tem capas mas não é herói, tem folhas mas não é árvore. O que é?",
                answer = "O Livro! 📖✨",
                emotion = MascotEmotion.THINKING
            ),
            ZeBalloonTip(
                category = "Anedotas",
                badgeLabel = "ANEDOTA DO ZÉ 😄",
                icon = Icons.Default.Face,
                badgeBgColor = Color(0xFFFFF8E1),
                badgeTextColor = Color(0xFFF57F17),
                text = "O que diz um zero para o oito?",
                answer = "Que cinto bonito! ⭕8️⃣",
                emotion = MascotEmotion.CELEBRATING
            ),
            ZeBalloonTip(
                category = "Desafios",
                badgeLabel = "DESAFIO DO ZÉ 🏆",
                icon = Icons.Default.Favorite,
                badgeBgColor = Color(0xFFE8F5E9),
                badgeTextColor = Color(0xFF2E7D32),
                text = "Consegues dar 5 pulinhos no mesmo pé sem perder o equilíbrio? Experimenta! 🦘⭐",
                emotion = MascotEmotion.CELEBRATING
            ),

            // --- ROUND 2 ---
            ZeBalloonTip(
                category = "Dicas",
                badgeLabel = "DICA DO ZÉ 💡",
                icon = Icons.Default.Lightbulb,
                badgeBgColor = Color(0xFFFFECE0),
                badgeTextColor = Color(0xFFE65100),
                text = "Pede 'por favor' e diz 'obrigado', são palavras mágicas que espalham sorrisos! ✨😊",
                emotion = MascotEmotion.PROUD
            ),
            ZeBalloonTip(
                category = "Conselhos",
                badgeLabel = "CONSELHO DO ZÉ 🌟",
                icon = Icons.Default.Star,
                badgeBgColor = Color(0xFFF3E8FF),
                badgeTextColor = Color(0xFF7C4DFF),
                text = "Quando te sentires chateado ou cansado, respira fundo 3 vezes e conta até 10! 🌬️🧘",
                emotion = MascotEmotion.THINKING
            ),
            ZeBalloonTip(
                category = "Sabias que?",
                badgeLabel = "SABIAS QUE? ❓",
                icon = Icons.Default.AutoAwesome,
                badgeBgColor = Color(0xFFE0F2FE),
                badgeTextColor = Color(0xFF0288D1),
                text = "Sabias que os golfinhos dormem com um olho aberto para vigiar o oceano? 🐬👁️",
                emotion = MascotEmotion.THINKING
            ),
            ZeBalloonTip(
                category = "Adivinhas",
                badgeLabel = "ADIVINHA DO ZÉ 🧩",
                icon = Icons.Default.SmartToy,
                badgeBgColor = Color(0xFFFCE4EC),
                badgeTextColor = Color(0xFFD81B60),
                text = "Qual é a coisa qual é ela que cai em pé e corre deitada?",
                answer = "A Chuva! 🌧️💧",
                emotion = MascotEmotion.EXCITED
            ),
            ZeBalloonTip(
                category = "Anedotas",
                badgeLabel = "ANEDOTA DO ZÉ 😄",
                icon = Icons.Default.Face,
                badgeBgColor = Color(0xFFFFF8E1),
                badgeTextColor = Color(0xFFF57F17),
                text = "Porque é que os peixes não usam computador?",
                answer = "Porque têm medo da rede! 🐟💻",
                emotion = MascotEmotion.HAPPY
            ),
            ZeBalloonTip(
                category = "Desafios",
                badgeLabel = "DESAFIO DO ZÉ 🏆",
                icon = Icons.Default.Favorite,
                badgeBgColor = Color(0xFFE8F5E9),
                badgeTextColor = Color(0xFF2E7D32),
                text = "Tenta dizer super rápido sem te enganares: 'O rato roeu a rolha do rei de Roma'! 🐭👑",
                emotion = MascotEmotion.EXCITED
            ),

            // --- ROUND 3 ---
            ZeBalloonTip(
                category = "Dicas",
                badgeLabel = "DICA DO ZÉ 💡",
                icon = Icons.Default.Lightbulb,
                badgeBgColor = Color(0xFFFFECE0),
                badgeTextColor = Color(0xFFE65100),
                text = "Arruma os teus brinquedos depois de brincar. O teu quarto vai ficar incrível! 🧸📦",
                emotion = MascotEmotion.HAPPY
            ),
            ZeBalloonTip(
                category = "Conselhos",
                badgeLabel = "CONSELHO DO ZÉ 🌟",
                icon = Icons.Default.Star,
                badgeBgColor = Color(0xFFF3E8FF),
                badgeTextColor = Color(0xFF7C4DFF),
                text = "Se não souberes fazer alguma coisa, pergunta sem medo! Aprender é fantástico! 🙋‍♂️💡",
                emotion = MascotEmotion.HAPPY
            ),
            ZeBalloonTip(
                category = "Sabias que?",
                badgeLabel = "SABIAS QUE? ❓",
                icon = Icons.Default.AutoAwesome,
                badgeBgColor = Color(0xFFE0F2FE),
                badgeTextColor = Color(0xFF0288D1),
                text = "Sabias que a baleia-azul tem um coração gigante do tamanho de um carro pequeno? 🐋🚗",
                emotion = MascotEmotion.CELEBRATING
            ),
            ZeBalloonTip(
                category = "Adivinhas",
                badgeLabel = "ADIVINHA DO ZÉ 🧩",
                icon = Icons.Default.SmartToy,
                badgeBgColor = Color(0xFFFCE4EC),
                badgeTextColor = Color(0xFFD81B60),
                text = "Tenho dentes mas não mordo, sirvo para pentear o cabelo. Quem sou?",
                answer = "O Pente! 🪮",
                emotion = MascotEmotion.HAPPY
            ),
            ZeBalloonTip(
                category = "Anedotas",
                badgeLabel = "ANEDOTA DO ZÉ 😄",
                icon = Icons.Default.Face,
                badgeBgColor = Color(0xFFFFF8E1),
                badgeTextColor = Color(0xFFF57F17),
                text = "O que é que a lâmpada disse quando se apagou?",
                answer = "Estou de noites! 💡🌙",
                emotion = MascotEmotion.CELEBRATING
            ),
            ZeBalloonTip(
                category = "Desafios",
                badgeLabel = "DESAFIO DO ZÉ 🏆",
                icon = Icons.Default.Favorite,
                badgeBgColor = Color(0xFFE8F5E9),
                badgeTextColor = Color(0xFF2E7D32),
                text = "Procura 3 coisas de cor vermelha ou azul à tua volta agora mesmo! 🔴🔵🔍",
                emotion = MascotEmotion.THINKING
            ),

            // --- ROUND 4 ---
            ZeBalloonTip(
                category = "Dicas",
                badgeLabel = "DICA DO ZÉ 💡",
                icon = Icons.Default.Lightbulb,
                badgeBgColor = Color(0xFFFFECE0),
                badgeTextColor = Color(0xFFE65100),
                text = "Bebe água fresquinha ao longo do dia para ficares super forte e saudável! 💧🥤",
                emotion = MascotEmotion.EXCITED
            ),
            ZeBalloonTip(
                category = "Conselhos",
                badgeLabel = "CONSELHO DO ZÉ 🌟",
                icon = Icons.Default.Star,
                badgeBgColor = Color(0xFFF3E8FF),
                badgeTextColor = Color(0xFF7C4DFF),
                text = "Dorme cedo para sonhares com aventuras incríveis no Espaço Mágico! 🌙🚀",
                emotion = MascotEmotion.HAPPY
            ),
            ZeBalloonTip(
                category = "Sabias que?",
                badgeLabel = "SABIAS QUE? ❓",
                icon = Icons.Default.AutoAwesome,
                badgeBgColor = Color(0xFFE0F2FE),
                badgeTextColor = Color(0xFF0288D1),
                text = "Sabias que os gatos conseguem emitir mais de 100 sons diferentes? 🐱🎵",
                emotion = MascotEmotion.HAPPY
            ),
            ZeBalloonTip(
                category = "Adivinhas",
                badgeLabel = "ADIVINHA DO ZÉ 🧩",
                icon = Icons.Default.SmartToy,
                badgeBgColor = Color(0xFFFCE4EC),
                badgeTextColor = Color(0xFFD81B60),
                text = "Qual é o animal que anda sempre com a casa às costas?",
                answer = "O Caracol! 🐚🐌",
                emotion = MascotEmotion.PROUD
            ),
            ZeBalloonTip(
                category = "Anedotas",
                badgeLabel = "ANEDOTA DO ZÉ 😄",
                icon = Icons.Default.Face,
                badgeBgColor = Color(0xFFFFF8E1),
                badgeTextColor = Color(0xFFF57F17),
                text = "Dois gatos estavam a fazer uma corrida. Quem ganhou?",
                answer = "O gato 'Miau-to' rápido! 🐱🏎️",
                emotion = MascotEmotion.PROUD
            ),
            ZeBalloonTip(
                category = "Desafios",
                badgeLabel = "DESAFIO DO ZÉ 🏆",
                icon = Icons.Default.Favorite,
                badgeBgColor = Color(0xFFE8F5E9),
                badgeTextColor = Color(0xFF2E7D32),
                text = "Dá um abraço bem apertado a quem estiver perto de ti hoje! 🤗💖",
                emotion = MascotEmotion.PROUD
            )
        )
    }

    var currentTipIndex by remember { mutableIntStateOf(0) }

    val activeTip = remember(currentTipIndex, allZeTips) {
        allZeTips[currentTipIndex % allZeTips.size]
    }

    // Continuous auto-rotation for the full-time balloon
    LaunchedEffect(allZeTips.size) {
        while (true) {
            delay(8000L) // Rotates every 8 seconds continuously
            currentTipIndex = (currentTipIndex + 1) % allZeTips.size
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .padding(bottom = 90.dp) // Extra padding for bottom nav
                .background(Color.Transparent)
                .testTag("home_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Hero Banner Image ---
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

            // --- Quick Menu Section (2x2 Grid) ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "O que vamos fazer hoje? ✨ 🚀",
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
                        bgColor = Color(0xFFF59E0B),
                        testTag = "home_quick_media",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Media) }
                    )
                    QuickMenuCard(
                        title = "ZéAI",
                        subtitle = "Falar com o Zé Traquina",
                        icon = Icons.Default.Face,
                        bgColor = Color(0xFFE53935),
                        testTag = "home_quick_chat",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Chat) }
                    )
                }
            }

            // --- 5. Destaques de Aprendizado do Dia ---
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
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HighlightCard(
                        category = "Matemática",
                        symbol = "123",
                        emoji = "🔢",
                        title = "Contar",
                        badgeColor = KidStarOrange,
                        containerColor = Color(0xFFFFF7E6),
                        testTag = "home_highlight_math",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Learn) }
                    )

                    HighlightCard(
                        category = "Letras",
                        symbol = "ABC",
                        emoji = "📚",
                        title = "Alfabeto",
                        badgeColor = SkyBluePrimary,
                        containerColor = Color(0xFFF0F9FF),
                        testTag = "home_highlight_letters",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Learn) }
                    )
                }

                Text(
                    text = "Aprender, brincar e cantar juntos!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }

            // --- Full-Time Zé Traquina Speech Balloon (Only the balloon at the bottom) ---
            FullTimeZeBalloonCard(
                activeTip = activeTip,
                onNextTip = {
                    currentTipIndex = (currentTipIndex + 1) % allZeTips.size
                },
                onSpeak = { textToSpeak ->
                    val cleanText = textToSpeak.replace(Regex("[^A-Za-zÀ-ÖØ-öø-ÿ0-9,?!. ]"), "")
                    viewModel.speak(cleanText)
                }
            )
        }
    }
}

@Composable
fun FullTimeZeBalloonCard(
    activeTip: ZeBalloonTip,
    onNextTip: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isAnswerRevealed by remember(activeTip) { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ze_balloon_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SunshineYellow),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(2.5.dp, MintGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mascot Avatar with real-time emotion
                ZeTraquinaMascot(
                    size = 50.dp,
                    showSpeechBubble = false,
                    emotion = if (isAnswerRevealed) MascotEmotion.CELEBRATING else activeTip.emotion,
                    triggerEmotionKey = activeTip.text + isAnswerRevealed,
                    onInteract = {
                        val speechText = if (activeTip.answer != null) {
                            if (isAnswerRevealed) "${activeTip.text} A resposta é: ${activeTip.answer}" else activeTip.text
                        } else activeTip.text
                        onSpeak(speechText)
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Speech Content & Badge
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = activeTip.badgeBgColor
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = activeTip.icon,
                                    contentDescription = null,
                                    tint = activeTip.badgeTextColor,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = activeTip.badgeLabel,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = activeTip.badgeTextColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = {
                                val speechText = if (activeTip.answer != null) {
                                    if (isAnswerRevealed) "${activeTip.text} A resposta é: ${activeTip.answer}" else activeTip.text
                                } else activeTip.text
                                onSpeak(speechText)
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Ouvir o Zé Traquina",
                                tint = Color(0xFF0D47A1),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = activeTip.text,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        lineHeight = 17.sp
                    )

                    if (activeTip.answer != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        if (isAnswerRevealed) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = BorderStroke(1.5.dp, Color(0xFFD81B60).copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "👉 Resposta: ${activeTip.answer}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFD81B60),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFD81B60),
                                modifier = Modifier.clickable {
                                    isAnswerRevealed = true
                                    val speechText = "${activeTip.text} A resposta é: ${activeTip.answer}"
                                    onSpeak(speechText)
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Help,
                                        contentDescription = "Ver Resposta",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "🔍 Ver Resposta!",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // "Outra Dica / Próxima" Button
                IconButton(
                    onClick = {
                        isAnswerRevealed = false
                        onNextTip()
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Nova Dica",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(20.dp)
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
    Card(
        modifier = modifier
            .height(86.dp)
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.25f),
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
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
}}
