package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel

data class LearnCardItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val symbol: String, // Emoji or character
    val speechText: String,
    val colorHex: Long
)

@Composable
fun LearnScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    val categories = listOf("Alfabeto", "Números", "Cores & Formas", "Animais", "Frutas", "Corpo Humano")

    val alphabetItems = remember {
        listOf(
            LearnCardItem("A", "Abelha", "Letra A", "🐝", "A de Abelha", 0xFFFFE082),
            LearnCardItem("B", "Bola", "Letra B", "⚽", "B de Bola", 0xFF80DEEA),
            LearnCardItem("C", "Casa", "Letra C", "🏠", "C de Casa", 0xFFFFAB91),
            LearnCardItem("D", "Dado", "Letra D", "🎲", "D de Dado", 0xFFC5CAE9),
            LearnCardItem("E", "Elefante", "Letra E", "🐘", "E de Elefante", 0xFFA5D6A7),
            LearnCardItem("F", "Foguete", "Letra F", "🚀", "F de Foguete", 0xFFF48FB1),
            LearnCardItem("G", "Gato", "Letra G", "🐱", "G de Gato", 0xFFFFCC80),
            LearnCardItem("H", "Hipopótamo", "Letra H", "🦛", "H de Hipopótamo", 0xFFB39DDB),
            LearnCardItem("I", "Iglu", "Letra I", "🧊", "I de Iglu", 0xFF80CBC4),
            LearnCardItem("J", "Jacaré", "Letra J", "🐊", "J de Jacaré", 0xFFC8E6C9),
            LearnCardItem("K", "Kiwi", "Letra K", "🥝", "K de Kiwi", 0xFFDCEDC8),
            LearnCardItem("L", "Leão", "Letra L", "🦁", "L de Leão", 0xFFFFE082),
            LearnCardItem("M", "Macaco", "Letra M", "🐒", "M de Macaco", 0xFFD7CCC8),
            LearnCardItem("N", "Nuvem", "Letra N", "☁️", "N de Nuvem", 0xFFB3E5FC),
            LearnCardItem("O", "Ovelha", "Letra O", "🐑", "O de Ovelha", 0xFFE1BEE7),
            LearnCardItem("P", "Pato", "Letra P", "🦆", "P de Pato", 0xFFFFF59D),
            LearnCardItem("Q", "Queijo", "Letra Q", "🧀", "Q de Queijo", 0xFFFFE082),
            LearnCardItem("R", "Robô", "Letra R", "🤖", "R de Robô", 0xFFCFD8DC),
            LearnCardItem("S", "Sol", "Letra S", "☀️", "S de Sol", 0xFFFFE082),
            LearnCardItem("T", "Tubarão", "Letra T", "🦈", "T de Tubarão", 0xFF80DEEA),
            LearnCardItem("U", "Uva", "Letra U", "🍇", "U de Uva", 0xFFD1C4E9),
            LearnCardItem("V", "Vaca", "Letra V", "🐮", "V de Vaca", 0xFFFFCC80),
            LearnCardItem("W", "Wafer", "Letra W", "🧇", "W de Wafer", 0xFFD7CCC8),
            LearnCardItem("X", "Xilofone", "Letra X", "🎼", "X de Xilofone", 0xFFFFAB91),
            LearnCardItem("Y", "Yakitori", "Letra Y", "🍡", "Y de Yakitori", 0xFFF48FB1),
            LearnCardItem("Z", "Zebra", "Letra Z", "🏁", "Z de Zebra", 0xFFEEEEEE)
        )
    }

    val numberItems = remember {
        (1..10).map { num ->
            val emoji = when(num) {
                1 -> "🎈"
                2 -> "🐱"
                3 -> "⭐"
                4 -> "🍎"
                5 -> "🚀"
                6 -> "🎨"
                7 -> "🍓"
                8 -> "🐥"
                9 -> "🦋"
                else -> "🌟"
            }
            LearnCardItem(
                id = "$num",
                title = "$num",
                subtitle = "$emoji".repeat(num.coerceAtMost(5)),
                symbol = "$num",
                speechText = "Número $num",
                colorHex = if (num % 2 == 0) 0xFF80DEEA else 0xFFFFD54F
            )
        }
    }

    val shapeItems = remember {
        listOf(
            LearnCardItem("c1", "Vermelho", "Cor", "🔴", "Cor Vermelha", 0xFFFF8A80),
            LearnCardItem("c2", "Azul", "Cor", "🔵", "Cor Azul", 0xFF82B1FF),
            LearnCardItem("c3", "Amarelo", "Cor", "🟡", "Cor Amarela", 0xFFFFE57F),
            LearnCardItem("c4", "Verde", "Cor", "🟢", "Cor Verde", 0xB5B9F6B4),
            LearnCardItem("s1", "Círculo", "Forma", "⚪", "Forma Círculo", 0xFFCFD8DC),
            LearnCardItem("s2", "Quadrado", "Forma", "⬛", "Forma Quadrado", 0xFFFFD180),
            LearnCardItem("s3", "Estrela", "Forma", "⭐", "Forma Estrela", 0xFFFFE082),
            LearnCardItem("s4", "Coração", "Forma", "❤️", "Forma Coração", 0xFFFF80AB)
        )
    }

    val animalItems = remember {
        listOf(
            LearnCardItem("a1", "Leão", "Rei da Floresta", "🦁", "O Leão faz auuu!", 0xFFFFCC80),
            LearnCardItem("a2", "Elefante", "Super Grande", "🐘", "O Elefante tem uma tromba longa!", 0xFFB0BEC5),
            LearnCardItem("a3", "Cão", "Amigo Leal", "🐶", "O Cão faz au au!", 0xFFFFD54F),
            LearnCardItem("a4", "Gato", "Miau Miau", "🐱", "O Gato faz miau miau!", 0xFFFFAB91),
            LearnCardItem("a5", "Sapo", "Pula Pula", "🐸", "O Sapo pula na lagoa!", 0xFFA5D6A7),
            LearnCardItem("a6", "Passarinho", "Voa Voa", "🐦", "O Passarinho canta no céu!", 0xFF80DEEA)
        )
    }

    val activeItems = when(selectedCategoryIndex) {
        0 -> alphabetItems
        1 -> numberItems
        2 -> shapeItems
        3 -> animalItems
        else -> alphabetItems
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("learn_screen")
    ) {
        // --- Top Header ---
        Text(
            text = "Estação de Aprendizado 📚",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(16.dp)
        )

        // --- Category Tabs ---
        ScrollableTabRow(
            selectedTabIndex = selectedCategoryIndex,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = SkyBluePrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = selectedCategoryIndex == index,
                    onClick = { selectedCategoryIndex = index },
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selectedCategoryIndex == index) SkyBluePrimary
                                else MaterialTheme.colorScheme.surface
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedCategoryIndex == index) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Items Grid ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(activeItems) { item ->
                LearnCard(
                    item = item,
                    onClick = {
                        viewModel.speak(item.speechText)
                        viewModel.addStars(1)
                    }
                )
            }
        }
    }
}

@Composable
fun LearnCard(
    item: LearnCardItem,
    onClick: () -> Unit
) {
    var isTapped by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isTapped) 1.08f else 1.0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .scale(scale)
            .clickable {
                isTapped = true
                onClick()
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ isTapped = false }, 300)
            }
            .testTag("learn_card_${item.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(item.colorHex)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.id,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Ouvir som",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = item.symbol,
                fontSize = 40.sp
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = item.subtitle,
                    fontSize = 11.sp,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
        }
    }
}
