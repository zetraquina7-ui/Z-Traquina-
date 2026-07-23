package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MascotEmotion
import com.example.ui.components.ZeTraquinaMascot
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.DrawingStroke
import com.example.viewmodel.GamesViewModel
import com.example.viewmodel.MainViewModel

@Composable
fun GamesScreen(
    mainViewModel: MainViewModel,
    gamesViewModel: GamesViewModel = remember { GamesViewModel() },
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Memória 🧠", "Pintura 🎨", "Contar Estrelas ⭐")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("games_screen")
    ) {
        // --- Header ---
        Text(
            text = "Estação de Jogos 🎮",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(16.dp)
        )

        // --- Tabs ---
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SkyBluePrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> MemoryGameView(mainViewModel, gamesViewModel)
            1 -> DrawingGameView(mainViewModel, gamesViewModel)
            2 -> StarCountingGameView(mainViewModel, gamesViewModel)
        }
    }
}

// --- 1. Memory Game ---
@Composable
fun MemoryGameView(
    mainViewModel: MainViewModel,
    gamesViewModel: GamesViewModel
) {
    val cards by gamesViewModel.memoryCards.collectAsState()
    val matchedPairs by gamesViewModel.matchedPairs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pares encontrados: $matchedPairs / ${cards.size / 2}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Button(
                onClick = {
                    gamesViewModel.startNewMemoryGame(pairCount = 4)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reiniciar", tint = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Novo Jogo", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        if (matchedPairs == cards.size / 2 && cards.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MintGreen)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ZeTraquinaMascot(
                        size = 72.dp,
                        showSpeechBubble = true,
                        emotion = MascotEmotion.CELEBRATING,
                        triggerEmotionKey = matchedPairs,
                        customPhrase = "PARABÉNS! Encontraste todos os pares! Ganhaste +10 Estrelas! 🎉⭐",
                        onInteract = {
                            mainViewModel.speak("És um génio dos jogos de memória!")
                        }
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) {
            itemsIndexed(cards) { index, card ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            if (!card.isFlipped && !card.isMatched) {
                                mainViewModel.speak(card.label)
                                gamesViewModel.flipMemoryCard(index) {
                                    mainViewModel.addStars(5)
                                    mainViewModel.speak("Muito bem! Encontraste um par de ${card.label}!")
                                }
                            }
                        }
                        .testTag("memory_card_$index"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (card.isFlipped || card.isMatched) SkyBluePrimary else SunshineYellow
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (card.isFlipped || card.isMatched) {
                            Text(
                                text = card.icon,
                                fontSize = 48.sp
                            )
                        } else {
                            Text(
                                text = "❓",
                                fontSize = 36.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- 2. Drawing Canvas Game ---
@Composable
fun DrawingGameView(
    mainViewModel: MainViewModel,
    gamesViewModel: GamesViewModel
) {
    val strokes by gamesViewModel.strokes.collectAsState()
    val selectedColor by gamesViewModel.selectedColor.collectAsState()
    val selectedWidth by gamesViewModel.selectedBrushWidth.collectAsState()

    var currentPath by remember { mutableStateOf<Path?>(null) }

    val colorPalette = listOf(
        Color(0xFFFF4081), // Pink
        Color(0xFF0288D1), // Blue
        Color(0xFFFFC107), // Yellow
        Color(0xFF4CAF50), // Green
        Color(0xFF7E57C2), // Purple
        Color(0xFFFF5722), // Orange
        Color(0xFF000000)  // Black
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Palette Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                colorPalette.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable {
                                gamesViewModel.setColor(color)
                            }
                    )
                }
            }

            IconButton(
                onClick = {
                    gamesViewModel.clearDrawing()
                    mainViewModel.speak("Ecrã limpo!")
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Limpar", tint = Color.Red)
            }
        }

        // Canvas Area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 90.dp)
                .testTag("drawing_canvas_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPath = Path().apply { moveTo(offset.x, offset.y) }
                            },
                            onDrag = { change, _ ->
                                currentPath?.let { path ->
                                    path.lineTo(change.position.x, change.position.y)
                                    // Trigger recomposition
                                    gamesViewModel.addStroke(
                                        DrawingStroke(
                                            path = Path().apply { addPath(path) },
                                            color = selectedColor,
                                            strokeWidth = selectedWidth
                                        )
                                    )
                                }
                            },
                            onDragEnd = {
                                currentPath = null
                            }
                        )
                    }
            ) {
                strokes.forEach { stroke ->
                    drawPath(
                        path = stroke.path,
                        color = stroke.color,
                        style = Stroke(width = stroke.strokeWidth)
                    )
                }
            }
        }
    }
}

// --- 3. Star Counting Game ---
@Composable
fun StarCountingGameView(
    mainViewModel: MainViewModel,
    gamesViewModel: GamesViewModel
) {
    val targetCount by gamesViewModel.starTargetCount.collectAsState()
    val userTappedStars by gamesViewModel.userTappedStars.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Toca em $targetCount estrelinhas! ⭐",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Já tocaste em: $userTappedStars / $targetCount",
                fontSize = 16.sp,
                color = SkyBluePrimary,
                fontWeight = FontWeight.Bold
            )

            if (userTappedStars == targetCount) {
                Spacer(modifier = Modifier.height(8.dp))
                ZeTraquinaMascot(
                    size = 64.dp,
                    showSpeechBubble = true,
                    emotion = MascotEmotion.CELEBRATING,
                    triggerEmotionKey = userTappedStars,
                    customPhrase = "Contaste todas as $targetCount estrelas sem falhar! Magnífico! ⭐🌟",
                    onInteract = {
                        mainViewModel.speak("Saber contar é fantástico!")
                    }
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(targetCount) { index ->
                val isTapped = index < userTappedStars

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            if (index == userTappedStars) {
                                mainViewModel.speak("${index + 1}")
                                gamesViewModel.tapStar {
                                    mainViewModel.addStars(5)
                                    mainViewModel.speak("Muito bem! Contaste todas as estrelas!")
                                }
                            }
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isTapped) MintGreen else SunshineYellow
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Estrela",
                            tint = if (isTapped) Color.White else KidStarOrange,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(90.dp))
    }
}
