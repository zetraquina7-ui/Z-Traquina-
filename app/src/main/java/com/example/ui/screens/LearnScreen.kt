package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import androidx.compose.ui.window.Dialog
import com.example.ui.components.MascotEmotion
import com.example.ui.components.ZeTraquinaMascot
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel

data class LearnCardItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val symbol: String,
    val speechText: String,
    val funFact: String,
    val colorHex: Long
)

@Composable
fun LearnScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    val categories = listOf("Alfabeto 🔤", "Números 🔢", "Cores & Formas 🎨", "Animais 🦁", "Frutas 🍎", "Corpo Humano 👀")

    var selectedCard by remember { mutableStateOf<LearnCardItem?>(null) }

    val alphabetItems = remember {
        listOf(
            LearnCardItem("A", "Abelha", "Letra A", "🐝", "A de Abelha", "As abelhas produzem mel docinho e voam de flor em flor!", 0xFFFFE082),
            LearnCardItem("B", "Bola", "Letra B", "⚽", "B de Bola", "Com a bola podemos jogar futebol com os amigos no recreio!", 0xFF80DEEA),
            LearnCardItem("C", "Casa", "Letra C", "🏠", "C de Casa", "A casa é o nosso refúgio quentinho e cheio de amor!", 0xFFFFAB91),
            LearnCardItem("D", "Dado", "Letra D", "🎲", "D de Dado", "O dado tem seis faces e serve para jogar em família!", 0xFFC5CAE9),
            LearnCardItem("E", "Elefante", "Letra E", "🐘", "E de Elefante", "O elefante é o maior animal terrestre e tem uma tromba gigante!", 0xFFA5D6A7),
            LearnCardItem("F", "Foguete", "Letra F", "🚀", "F de Foguete", "O foguete viaja pelo espaço para visitar as estrelas e a lua!", 0xFFF48FB1),
            LearnCardItem("G", "Gato", "Letra G", "🐱", "G de Gato", "Os gatos adoram ronronar e dormir ao sol!", 0xFFFFCC80),
            LearnCardItem("H", "Hipopótamo", "Letra H", "🦛", "H de Hipopótamo", "O hipopótamo adora banhar-se nos rios frescos da África!", 0xFFB39DDB),
            LearnCardItem("I", "Iglu", "Letra I", "🧊", "I de Iglu", "O iglu é uma casinha feita de blocos de neve bem quentinha por dentro!", 0xFF80CBC4),
            LearnCardItem("J", "Jacaré", "Letra J", "🐊", "J de Jacaré", "O jacaré nada muito rápido nos rios e lagoas!", 0xFFC8E6C9),
            LearnCardItem("K", "Kiwi", "Letra K", "🥝", "K de Kiwi", "O kiwi é uma fruta verdinha por dentro cheia de vitamina C!", 0xFFDCEDC8),
            LearnCardItem("L", "Leão", "Letra L", "🦁", "L de Leão", "O leão é conhecido como o rei da selva!", 0xFFFFE082),
            LearnCardItem("M", "Macaco", "Letra M", "🐒", "M de Macaco", "Os macacos adoram saltar de árvore em árvore e comer bananas!", 0xFFD7CCC8),
            LearnCardItem("N", "Nuvem", "Letra N", "☁️", "N de Nuvem", "As nuvens passeiam pelo céu azul feito algodão doce!", 0xFFB3E5FC),
            LearnCardItem("O", "Ovelha", "Letra O", "🐑", "O de Ovelha", "A ovelha tem um pelo fofinho que nos dá a lã quentinha!", 0xFFE1BEE7),
            LearnCardItem("P", "Pato", "Letra P", "🦆", "P de Pato", "O pato nada na lagoa fazendo 'Quack Quack'!", 0xFFFFF59D),
            LearnCardItem("Q", "Queijo", "Letra Q", "🧀", "Q de Queijo", "O queijo é delicioso e muito nutritivo para os ossos!", 0xFFFFE082),
            LearnCardItem("R", "Robô", "Letra R", "🤖", "R de Robô", "O robô é uma máquina inteligente que nos ajuda nas missões!", 0xFFCFD8DC),
            LearnCardItem("S", "Sol", "Letra S", "☀️", "S de Sol", "O sol ilumina o nosso dia e aquece a Terra inteira!", 0xFFFFE082),
            LearnCardItem("T", "Tubarão", "Letra T", "🦈", "T de Tubarão", "O tubarão é um grande nadador dos oceanos profundos!", 0xFF80DEEA),
            LearnCardItem("U", "Uva", "Letra U", "🍇", "U de Uva", "As uvas crescem em cachos e são docinhas e sumarentas!", 0xFFD1C4E9),
            LearnCardItem("V", "Vaca", "Letra V", "🐮", "V de Vaca", "A vaca dá-nos leite fresquinho todos os dias!", 0xFFFFCC80),
            LearnCardItem("W", "Wafer", "Letra W", "🧇", "W de Wafer", "O wafer é uma bolacha crocante e muito saborosa!", 0xFFD7CCC8),
            LearnCardItem("X", "Xilofone", "Letra X", "🎼", "X de Xilofone", "O xilofone é um instrumento musical que toca som de sininho!", 0xFFFFAB91),
            LearnCardItem("Y", "Yakitori", "Letra Y", "🍡", "Y de Yakitori", "Yakitori são deliciosos espetinhos tradicionais!", 0xFFF48FB1),
            LearnCardItem("Z", "Zebra", "Letra Z", "🏁", "Z de Zebra", "A zebra tem listas pretas e brancas muito elegantes!", 0xFFEEEEEE)
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
                title = "Número $num",
                subtitle = "Quantidade: $num",
                symbol = "$num $emoji",
                speechText = "Número $num",
                funFact = "Tens $num dedinhos numa mão ou sabes contar até $num!",
                colorHex = if (num % 2 == 0) 0xFF80DEEA else 0xFFFFD54F
            )
        }
    }

    val shapeItems = remember {
        listOf(
            LearnCardItem("c1", "Vermelho", "Cor Vibrante", "🔴", "Cor Vermelha", "O vermelho é a cor das cerejas e dos bombeiros!", 0xFFFF8A80),
            LearnCardItem("c2", "Azul", "Cor do Céu", "🔵", "Cor Azul", "O azul é a cor do céu e dos oceanos azuis!", 0xFF82B1FF),
            LearnCardItem("c3", "Amarelo", "Cor do Sol", "🟡", "Cor Amarela", "O amarelo brilha como o sol e as estrelas!", 0xFFFFE57F),
            LearnCardItem("c4", "Verde", "Cor da Natureza", "🟢", "Cor Verde", "O verde é a cor das árvores e dos prados!", 0xFFC8E6C9),
            LearnCardItem("s1", "Círculo", "Forma Geométrica", "⚪", "Forma Círculo", "O círculo é redondo como uma bola de futebol!", 0xFFCFD8DC),
            LearnCardItem("s2", "Quadrado", "Forma Geométrica", "⬛", "Forma Quadrado", "O quadrado tem quatro lados iguais!", 0xFFFFD180),
            LearnCardItem("s3", "Estrela", "Forma Mágica", "⭐", "Forma Estrela", "As estrelas brilham no céu nocturno com muita magia!", 0xFFFFE082),
            LearnCardItem("s4", "Coração", "Forma de Amor", "❤️", "Forma Coração", "O coração simboliza o carinho e o amor pela família!", 0xFFFF80AB)
        )
    }

    val animalItems = remember {
        listOf(
            LearnCardItem("a1", "Leão", "Rei da Selva", "🦁", "O Leão ruge forte!", "O leão é o rei dos animais na savana!", 0xFFFFCC80),
            LearnCardItem("a2", "Elefante", "Gigante Gentil", "🐘", "O Elefante tem tromba comprida!", "O elefante tem uma memória extraordinária!", 0xFFB0BEC5),
            LearnCardItem("a3", "Cão", "Melhor Amigo", "🐶", "O Cão faz au au!", "Os cães são os animais mais fiéis do mundo!", 0xFFFFD54F),
            LearnCardItem("a4", "Gato", "Fofinho", "🐱", "O Gato faz miau!", "Os gatos conseguem ver muito bem no escuro!", 0xFFFFAB91),
            LearnCardItem("a5", "Sapo", "Saltitão", "🐸", "O Sapo pula na lagoa!", "Os sapos nascem como girinos na água!", 0xFFA5D6A7),
            LearnCardItem("a6", "Passarinho", "Cantor", "🐦", "O Passarinho canta!", "Os passarinhos constroem ninhos nas árvores!", 0xFF80DEEA)
        )
    }

    val fruitItems = remember {
        listOf(
            LearnCardItem("f1", "Maçã", "Vermelha e Doce", "🍎", "Maçã saborosa", "Uma maçã por dia dá muita energia!", 0xFFFFCDD2),
            LearnCardItem("f2", "Banana", "Amarela e Boa", "🍌", "Banana nutritiva", "Os macacos adoram bananas ricas em potássio!", 0xFFFFF9C4),
            LearnCardItem("f3", "Uva", "Cacho Roxo", "🍇", "Uvas docinhas", "As uvas são ótimas para fazer sumo fresquinho!", 0xFFE1BEE7),
            LearnCardItem("f4", "Morango", "Fruta Silvestre", "🍓", "Morango delicioso", "O morango tem as sementinhas do lado de fora!", 0xFFFFAB91),
            LearnCardItem("f5", "Melancia", "Grande e Fresca", "🍉", "Melancia sumarenta", "A melancia é perfeita para os dias quentes de verão!", 0xFFC8E6C9),
            LearnCardItem("f6", "Ananás", "Tropical", "🍍", "Ananás doce", "O ananás cresce no chão com uma coroa de folhas!", 0xFFFFE082)
        )
    }

    val bodyItems = remember {
        listOf(
            LearnCardItem("b1", "Olhos", "Sentido da Visão", "👀", "Dois olhos para ver", "Com os olhos vemos as cores e o sorriso dos amigos!", 0xFFB3E5FC),
            LearnCardItem("b2", "Nariz", "Sentido do Olfato", "👃", "Um nariz para cheirar", "O nariz consegue distinguir milhares de cheiros diferentes!", 0xFFFFF9C4),
            LearnCardItem("b3", "Boca", "Paladar e Sorriso", "👄", "Uma boca para sorrir", "A boca serve para falar, cantar e saborear a comida!", 0xFFFFAB91),
            LearnCardItem("b4", "Mãos", "Tato e Ação", "🤲", "Duas mãos para abraçar", "Com as mãos fazemos festas e construímos torres!", 0xFFC8E6C9),
            LearnCardItem("b5", "Pés", "Movimento", "👣", "Pés para caminhar", "Os pés levam-nos a correr e saltar por todo o lado!", 0xFFD7CCC8),
            LearnCardItem("b6", "Orelhas", "Audição", "👂", "Orelhas para ouvir", "As orelhas ajudam-nos a ouvir músicas e histórias!", 0xFFE1BEE7)
        )
    }

    val activeItems = when(selectedCategoryIndex) {
        0 -> alphabetItems
        1 -> numberItems
        2 -> shapeItems
        3 -> animalItems
        4 -> fruitItems
        5 -> bodyItems
        else -> alphabetItems
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("learn_screen")
    ) {
        // --- Top Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Estação de Aprendizado 📚✨",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Toca num cartão para ouvir e aprender!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

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
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selectedCategoryIndex == index) SkyBluePrimary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
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
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(activeItems) { item ->
                LearnCard(
                    item = item,
                    onClick = {
                        selectedCard = item
                        viewModel.speak(item.speechText)
                        viewModel.addStars(1)
                    }
                )
            }
        }
    }

    // --- Interactive Detail Popup Dialog ---
    if (selectedCard != null) {
        val item = selectedCard!!
        Dialog(onDismissRequest = { selectedCard = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("learn_popup_dialog"),
                shape = RoundedCornerShape(28.dp),
                color = Color(item.colorHex),
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = item.id,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        IconButton(
                            onClick = { selectedCard = null },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar",
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = item.symbol,
                        fontSize = 72.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = Color.Black
                    )

                    Text(
                        text = item.subtitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Fun Fact Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "💡 Curiosidade do Zé Traquina:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = SkyBluePrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.funFact,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.speak(item.speechText + ". " + item.funFact)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Ouvir Som 🔊", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                        }

                        Button(
                            onClick = {
                                viewModel.addStars(2)
                                viewModel.speak("Ganhaste 2 estrelas extras! Excelente!")
                                selectedCard = null
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MintGreen)
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Brincar ⭐", color = Color.White, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
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
    val scale by animateFloatAsState(targetValue = if (isTapped) 1.06f else 1.0f)

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
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(item.colorHex)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Ouvir",
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Text(
                text = item.symbol,
                fontSize = 38.sp
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    maxLines = 1
                )
                Text(
                    text = item.subtitle,
                    fontSize = 10.sp,
                    color = Color.Black.copy(alpha = 0.6f),
                    maxLines = 1
                )
            }
        }
    }
}
