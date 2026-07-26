package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import com.example.audio.AudioSynthesizer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val emoji: String,
    val explanation: String
)

data class BalloonItem(
    val id: Int,
    val color: Color,
    val label: String,
    val emoji: String,
    var isPopped: Boolean = false
)

data class PianoKeyItem(
    val note: String,
    val label: String,
    val emoji: String,
    val color: Color,
    val speechText: String,
    val frequencyHz: Double = 261.63
)

@Composable
fun GamesScreen(
    mainViewModel: MainViewModel,
    gamesViewModel: GamesViewModel = remember { GamesViewModel() },
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Biblioteca 📚", "Memória 🧠", "Pintura 🎨", "Estrelas ⭐", "Quiz 🧩", "Balões 🎈", "Piano 🎹")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("games_screen")
    ) {
        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Estação de Jogos 🎮✨",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Escolhe um jogo divertido e ganha estrelas!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // --- Tabs ---
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = SkyBluePrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selectedTab == index) SkyBluePrimary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedTab == index) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedTab > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = { selectedTab = 0 },
                    shape = RoundedCornerShape(12.dp),
                    color = SunshineYellow,
                    shadowElevation = 2.dp,
                    modifier = Modifier.testTag("btn_back_to_game_library")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar à Biblioteca de Jogos",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Biblioteca de Jogos 📚",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        when (selectedTab) {
            0 -> GameLibraryView(mainViewModel = mainViewModel, onSelectGame = { gameTab -> selectedTab = gameTab })
            1 -> MemoryGameView(mainViewModel, gamesViewModel)
            2 -> DrawingGameView(mainViewModel, gamesViewModel)
            3 -> StarCountingGameView(mainViewModel, gamesViewModel)
            4 -> QuizGameView(mainViewModel)
            5 -> BalloonPopGameView(mainViewModel)
            6 -> MagicPianoGameView(mainViewModel)
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
    val totalPairs = cards.size / 2

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
            Column {
                Text(
                    text = "Jogo da Memória 🧠✨",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Pares encontrados: $matchedPairs / $totalPairs",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = SkyBluePrimary
                )
            }

            Button(
                onClick = {
                    gamesViewModel.startNewMemoryGame(pairCount = 4)
                    mainViewModel.speak("Novo jogo de memória iniciado!")
                },
                colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reiniciar", tint = Color.Black, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Novo Jogo", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        if (totalPairs > 0 && matchedPairs == totalPairs) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MintGreen)
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ZeTraquinaMascot(
                        size = 60.dp,
                        showSpeechBubble = true,
                        emotion = MascotEmotion.CELEBRATING,
                        triggerEmotionKey = matchedPairs,
                        customPhrase = "PARABÉNS! Encontraste todos os pares! 🎉⭐",
                        onInteract = {
                            mainViewModel.speak("És um génio dos jogos de memória!")
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(cards, key = { _, card -> card.id }) { index, card ->
                val isRevealed = card.isFlipped || card.isMatched

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.85f)
                        .clickable {
                            if (!card.isFlipped && !card.isMatched) {
                                gamesViewModel.flipMemoryCard(index) {
                                    mainViewModel.addStars(5)
                                    mainViewModel.speak("Par do ${card.label}! Muito bem!")
                                }
                                if (!card.isMatched) {
                                    mainViewModel.speak(card.label)
                                }
                            }
                        }
                        .testTag("memory_card_$index"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            card.isMatched -> Color(0xFFDCFCE7) // Soft Mint Green
                            card.isFlipped -> Color(0xFFE0F2FE) // Soft Sky Blue
                            else -> SunshineYellow
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isRevealed) 2.dp else 4.dp),
                    border = BorderStroke(
                        width = 1.5.dp,
                        color = when {
                            card.isMatched -> MintGreen
                            card.isFlipped -> SkyBluePrimary
                            else -> Color(0xFFF59E0B)
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isRevealed) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = card.icon, fontSize = 34.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = card.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "🧩", fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Zé",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1E293B)
                                )
                            }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                colorPalette.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable {
                                gamesViewModel.setColor(color)
                                mainViewModel.speak("Cor selecionada")
                            }
                    )
                }
            }

            IconButton(
                onClick = {
                    gamesViewModel.clearDrawing()
                    mainViewModel.speak("Ecrã limpo para nova obra de arte!")
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Limpar", tint = Color.Red, modifier = Modifier.size(18.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Espessura:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            listOf(8f, 16f, 26f).forEach { width ->
                Button(
                    onClick = { gamesViewModel.setBrushWidth(width) },
                    modifier = Modifier.padding(horizontal = 4.dp).height(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedWidth == width) SkyBluePrimary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (width == 8f) "Fino" else if (width == 16f) "Médio" else "Grosso",
                        fontSize = 11.sp,
                        color = if (selectedWidth == width) Color.White else Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 12.dp)
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
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Progresso: $userTappedStars / $targetCount",
                fontSize = 14.sp,
                color = SkyBluePrimary,
                fontWeight = FontWeight.Bold
            )

            if (userTappedStars == targetCount) {
                Spacer(modifier = Modifier.height(4.dp))
                ZeTraquinaMascot(
                    size = 56.dp,
                    showSpeechBubble = true,
                    emotion = MascotEmotion.CELEBRATING,
                    triggerEmotionKey = userTappedStars,
                    customPhrase = "Contaste todas as $targetCount estrelas! Magnífico! ⭐🌟",
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
                                    mainViewModel.speak("Muito bem! Ganhaste 5 estrelas!")
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
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

// --- 4. Quiz Divertido ---
@Composable
fun QuizGameView(
    mainViewModel: MainViewModel
) {
    val questions = remember {
        listOf(
            QuizQuestion(
                question = "Qual animal diz 'Miau Miau'?",
                options = listOf("🐶 Cão", "🐱 Gato", "🦁 Leão", "🐮 Vaca"),
                correctIndex = 1,
                emoji = "🐱",
                explanation = "O gato é um felino muito fofinho que adora brincar!"
            ),
            QuizQuestion(
                question = "Onde vivem os peixinhos?",
                options = listOf("🌲 Na floresta", "🌊 Na água", "☁️ No céu", "🏠 Em cima da mesa"),
                correctIndex = 1,
                emoji = "🐟",
                explanation = "Os peixinhos nadam felizes na água do mar e dos rios!"
            ),
            QuizQuestion(
                question = "Qual é a cor do sol brilhante?",
                options = listOf("🔵 Azul", "🟡 Amarelo", "🟢 Verde", "🟣 Roxo"),
                correctIndex = 1,
                emoji = "☀️",
                explanation = "O sol brilha com uma cor amarela radiante no céu!"
            ),
            QuizQuestion(
                question = "O que comem os macacos?",
                options = listOf("🍌 Bananas", "🧱 Tijolos", "🚗 Carros", "📚 Livros"),
                correctIndex = 0,
                emoji = "🐒",
                explanation = "Os macacos adoram trepar árvores e comer bananas docinhas!"
            )
        )
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val q = questions[currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pergunta ${currentQuestionIndex + 1} de ${questions.size}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SkyBluePrimary
                )
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SunshineYellow.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = "Quiz Zé Traquina 🧩",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = q.emoji, fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = q.question,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            q.options.forEachIndexed { optIndex, optionText ->
                val isSelected = selectedOption == optIndex
                val buttonColor = when {
                    isCorrect == true && isSelected -> MintGreen
                    isCorrect == false && isSelected -> Color(0xFFEF5350)
                    else -> MaterialTheme.colorScheme.surface
                }

                Button(
                    onClick = {
                        if (selectedOption == null) {
                            selectedOption = optIndex
                            val correct = optIndex == q.correctIndex
                            isCorrect = correct
                            if (correct) {
                                mainViewModel.addStars(5)
                                mainViewModel.speak("Correto! ${q.explanation}")
                            } else {
                                mainViewModel.speak("Quase! Tenta na próxima!")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = optionText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedOption != null && isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (selectedOption != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        selectedOption = null
                        isCorrect = null
                        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow)
                ) {
                    Text(
                        text = "Próxima Pergunta ➡️",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

// --- 5. Balloon Pop Game ---
@Composable
fun BalloonPopGameView(
    mainViewModel: MainViewModel
) {
    var balloons by remember {
        mutableStateOf(
            listOf(
                BalloonItem(1, Color(0xFFFF4081), "Vermelho", "🎈"),
                BalloonItem(2, Color(0xFF00B0FF), "Azul", "🎈"),
                BalloonItem(3, Color(0xFFFFD54F), "Amarelo", "🎈"),
                BalloonItem(4, Color(0xFF66BB6A), "Verde", "🎈"),
                BalloonItem(5, Color(0xFFAB47BC), "Roxo", "🎈"),
                BalloonItem(6, Color(0xFFFF7043), "Laranja", "🎈")
            )
        )
    }

    var poppedCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Estoura os Balões Mágicos! 🎈",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Balões rebentados: $poppedCount / ${balloons.size}",
                fontSize = 14.sp,
                color = SkyBluePrimary,
                fontWeight = FontWeight.Bold
            )

            if (poppedCount == balloons.size) {
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        balloons = balloons.map { it.copy(isPopped = false) }
                        poppedCount = 0
                        mainViewModel.speak("Novos balões cheios de magia!")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Encher Novos Balões 🎈", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            items(balloons) { balloon ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.1f)
                        .clickable {
                            if (!balloon.isPopped) {
                                balloons = balloons.map { if (it.id == balloon.id) it.copy(isPopped = true) else it }
                                poppedCount++
                                mainViewModel.addStars(3)
                                mainViewModel.speak("Pop! Balão ${balloon.label} rebentado! Ganhaste 3 estrelas!")
                            }
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (balloon.isPopped) MaterialTheme.colorScheme.surfaceVariant else balloon.color
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (balloon.isPopped) {
                            Text(text = "✨", fontSize = 40.sp)
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = balloon.emoji, fontSize = 48.sp)
                                Text(
                                    text = balloon.label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}

// --- 6. Magic Piano & Xylophone Game ---
@Composable
fun MagicPianoGameView(
    mainViewModel: MainViewModel
) {
    val pianoKeys = remember {
        listOf(
            PianoKeyItem("Dó", "Dó 🐶", "🐕", Color(0xFFFF8A80), "Dó", 261.63),
            PianoKeyItem("Ré", "Ré 🐱", "🐈", Color(0xFFFFD180), "Ré", 293.66),
            PianoKeyItem("Mi", "Mi 🐸", "🐸", Color(0xFFFFEE58), "Mi", 329.63),
            PianoKeyItem("Fá", "Fá 🦁", "🦁", Color(0xFFD4E157), "Fá", 349.23),
            PianoKeyItem("Sol", "Sol 🐮", "🐮", Color(0xFF81D4FA), "Sol", 392.00),
            PianoKeyItem("Lá", "Lá 🐥", "🐥", Color(0xFFCE93D8), "Lá", 440.00),
            PianoKeyItem("Si", "Si 🌟", "⭐", Color(0xFFF48FB1), "Si", 493.88),
            PianoKeyItem("Dó²", "Dó² 🚀", "🚀", Color(0xFFA7F3D0), "Dó Agudo", 523.25)
        )
    }

    var selectedInstrument by remember { mutableStateOf("xylophone") } // "xylophone", "piano", "chime"
    var voiceGuideEnabled by remember { mutableStateOf(false) }
    var lastPlayedKey by remember { mutableStateOf<String?>(null) }
    var isPlayingDemoSong by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- Top Title & Instrument Selector ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Xilofone & Piano Mágico 🎹🎶",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Toca nas barras para ouvir o som real!",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Voice toggle
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (voiceGuideEnabled) SunshineYellow else Color(0xFFE2E8F0),
                    modifier = Modifier.clickable { voiceGuideEnabled = !voiceGuideEnabled }
                ) {
                    Text(
                        text = if (voiceGuideEnabled) "🗣️ Voz ON" else "🗣️ Voz OFF",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Instrument Mode Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val instruments = listOf(
                    "xylophone" to "Xilofone 🪵",
                    "piano" to "Piano 🎹",
                    "chime" to "Sinos 🔔"
                )
                instruments.forEach { (type, label) ->
                    val isSelected = selectedInstrument == type
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) SkyBluePrimary else Color(0xFFF1F5F9),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedInstrument = type
                                AudioSynthesizer.stopMelody()
                                isPlayingDemoSong = false
                            }
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF475569)
                            )
                        }
                    }
                }
            }
        }

        // --- Active Key Display ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            color = SunshineYellow.copy(alpha = 0.2f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = SkyBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            isPlayingDemoSong -> "A tocar Melodia Mágica... 🎶"
                            lastPlayedKey != null -> "Nota: $lastPlayedKey 🎶"
                            else -> "Toca nas teclas abaixo para tocar!"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }

                if (isPlayingDemoSong) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Red.copy(alpha = 0.15f),
                        modifier = Modifier.clickable {
                            AudioSynthesizer.stopMelody()
                            isPlayingDemoSong = false
                        }
                    ) {
                        Text(
                            text = "Parar ⏹",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // --- Musical Keys / Xylophone Bars ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            pianoKeys.forEachIndexed { index, key ->
                var isPressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1.0f)

                // Xylophone bar length effect (longer at bottom, shorter at top)
                val barFraction = if (selectedInstrument == "xylophone") {
                    1.0f - (index * 0.035f)
                } else {
                    1.0f
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(barFraction)
                        .height(48.dp)
                        .scale(scale)
                        .clickable {
                            isPressed = true
                            lastPlayedKey = "${key.note} (${key.label})"

                            // 1. Instant sound synth
                            AudioSynthesizer.playSynthNote(
                                freqHz = key.frequencyHz,
                                durationMs = if (selectedInstrument == "xylophone") 400 else 500,
                                instrumentType = selectedInstrument
                            )

                            // 2. Voice guide if enabled
                            if (voiceGuideEnabled) {
                                mainViewModel.speak(key.note)
                            }

                            mainViewModel.addStars(1)
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ isPressed = false }, 200)
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = key.color),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = key.emoji, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = key.label,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1E293B)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${key.frequencyHz.toInt()}Hz",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // --- Demo Songs Shortcuts ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Tocar Músicas Automáticas: 🎵",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val demoSongs = listOf(
                    0 to "Férias ☀️",
                    4 to "ABC 🔤",
                    9 to "É Natal 🎄",
                    1 to "Santos 🎈"
                )
                items(demoSongs) { (songIdx, title) ->
                    Button(
                        onClick = {
                            isPlayingDemoSong = true
                            lastPlayedKey = title
                            AudioSynthesizer.playSongMelody(songIdx, false)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = title, fontSize = 12.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// --- Game Library Component ---
data class GameLibraryItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val emoji: String,
    val accentColor: Color,
    val starReward: String,
    val difficulty: String,
    val targetTab: Int
)

@Composable
fun GameLibraryView(
    mainViewModel: MainViewModel,
    onSelectGame: (Int) -> Unit
) {
    val gamesList = remember {
        listOf(
            GameLibraryItem(
                id = "memory",
                title = "Jogo da Memória",
                description = "Encontra os pares de cartas mágicas de animais e frutas!",
                category = "Raciocínio",
                emoji = "🧠",
                accentColor = SkyBluePrimary,
                starReward = "+10 ⭐",
                difficulty = "Fácil",
                targetTab = 1
            ),
            GameLibraryItem(
                id = "painting",
                title = "Atelier de Pintura",
                description = "Muda de cores, pinta desenhos incríveis e solta a tua arte!",
                category = "Criatividade",
                emoji = "🎨",
                accentColor = MintGreen,
                starReward = "+5 ⭐",
                difficulty = "Livre",
                targetTab = 2
            ),
            GameLibraryItem(
                id = "star_counter",
                title = "Contador de Estrelas",
                description = "Aprende os números até 10 a apanhar estrelas brilhantes no céu!",
                category = "Matemática",
                emoji = "⭐",
                accentColor = SunshineYellow,
                starReward = "+8 ⭐",
                difficulty = "Fácil",
                targetTab = 3
            ),
            GameLibraryItem(
                id = "quiz",
                title = "Quiz do Zé",
                description = "Perguntas super divertidas sobre animais, cores e natureza!",
                category = "Desafios",
                emoji = "🧩",
                accentColor = KidStarOrange,
                starReward = "+12 ⭐",
                difficulty = "Médio",
                targetTab = 4
            ),
            GameLibraryItem(
                id = "balloon_pop",
                title = "Estoura Balões",
                description = "Toca nos balões coloridos antes que voem pelo ar fora!",
                category = "Agilidade",
                emoji = "🎈",
                accentColor = Color(0xFFE91E63),
                starReward = "+10 ⭐",
                difficulty = "Fácil",
                targetTab = 5
            ),
            GameLibraryItem(
                id = "magic_piano",
                title = "Piano Mágico",
                description = "Cria melodias, descobre as notas e toca músicas alegres!",
                category = "Música",
                emoji = "🎹",
                accentColor = Color(0xFF9C27B0),
                starReward = "+5 ⭐",
                difficulty = "Livre",
                targetTab = 6
            )
        )
    }

    val categories = listOf("Todos 🎮", "Raciocínio 🧠", "Criatividade 🎨", "Matemática 🔢", "Desafios 🧩", "Agilidade 🎈", "Música 🎹")
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    val filteredGames = remember(selectedCategoryIndex) {
        if (selectedCategoryIndex == 0) gamesList
        else {
            val catName = when(selectedCategoryIndex) {
                1 -> "Raciocínio"
                2 -> "Criatividade"
                3 -> "Matemática"
                4 -> "Desafios"
                5 -> "Agilidade"
                6 -> "Música"
                else -> ""
            }
            gamesList.filter { it.category.equals(catName, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("game_library_component")
    ) {
        // --- Library Hero Header Card ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ZeTraquinaMascot(
                    size = 54.dp,
                    showSpeechBubble = false,
                    emotion = MascotEmotion.HAPPY,
                    onInteract = {
                        mainViewModel.speak("Explora a biblioteca de jogos e escolhe a tua atividade favorita!")
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Biblioteca de Jogos 📚🎮",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Escolhe um minijogo para aprenderes enquanto te divertes!",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // --- Category Filters ---
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories.size) { index ->
                val cat = categories[index]
                val isSelected = selectedCategoryIndex == index
                Surface(
                    onClick = { selectedCategoryIndex = index },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) SkyBluePrimary else MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = if (isSelected) 3.dp else 0.dp,
                    modifier = Modifier.testTag("category_filter_$index")
                ) {
                    Text(
                        text = cat,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- Games Grid ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredGames, key = { it.id }) { game ->
                GameLibraryCard(
                    game = game,
                    onPlay = {
                        mainViewModel.speak("Vamos jogar ${game.title}!")
                        mainViewModel.addStars(1)
                        onSelectGame(game.targetTab)
                    }
                )
            }
        }
    }
}

@Composable
fun GameLibraryCard(
    game: GameLibraryItem,
    onPlay: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 1.04f else 1.0f,
        label = "card_scale_${game.id}"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onPlay
            )
            .testTag("game_card_${game.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = game.accentColor.copy(alpha = 0.15f)),
        border = BorderStroke(2.dp, game.accentColor.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPressed) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Top Row: Category Pill & Star Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = game.accentColor,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        text = game.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SunshineYellow,
                ) {
                    Text(
                        text = game.starReward,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Emoji Avatar Center
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = game.accentColor.copy(alpha = 0.25f),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = game.emoji, fontSize = 28.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = game.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = game.description,
                fontSize = 10.sp,
                color = Color.Gray,
                lineHeight = 13.sp,
                maxLines = 2,
                modifier = Modifier.height(28.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Play Button
            Button(
                onClick = onPlay,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = game.accentColor),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Jogar",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "JOGAR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}
