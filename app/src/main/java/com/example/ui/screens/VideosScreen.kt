package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel

data class StoryPage(
    val pageNumber: Int,
    val text: String,
    val illustrationEmoji: String,
    val bgColorHex: Long
)

data class VideoStory(
    val id: String,
    val title: String,
    val duration: String,
    val category: String, // "educativo" or "brincadeiras"
    val categoryLabel: String,
    val thumbnailEmoji: String,
    val pages: List<StoryPage>
)

@Composable
fun VideosScreen(
    mainViewModel: MainViewModel,
    categoryFilter: String = "educativo",
    modifier: Modifier = Modifier
) {
    var activeStory by remember { mutableStateOf<VideoStory?>(null) }
    var activePageIndex by remember { mutableStateOf(0) }

    val allStories = remember {
        listOf(
            // --- EDUCATIVO ---
            VideoStory(
                id = "v1",
                title = "Zé Traquina no Planeta das Letras",
                duration = "3 min",
                category = "educativo",
                categoryLabel = "Alfabeto & Leitura",
                thumbnailEmoji = "🚀🔤",
                pages = listOf(
                    StoryPage(1, "Era uma vez o Zé Traquina viajando no seu foguete colorido rumo ao Planeta das Letras!", "🚀", 0xFFE1F5FE),
                    StoryPage(2, "Chegando lá, ele encontrou a letra A dando gargalhadas numa árvore cheia de amoras docinhas!", "🌳🅰️", 0xFFFFF8E1),
                    StoryPage(3, "A letra B veio correndo com uma bola brilhante e convidou o Zé pra jogar!", "⚽🅱️", 0xFFE8F5E9),
                    StoryPage(4, "E assim, todas as letrinhas se reuniram numa festa cheia de música e alegria! Fim!", "🎉⭐", 0xFFFCE4EC)
                )
            ),
            VideoStory(
                id = "v2",
                title = "A Grande Corrida das Cores",
                duration = "2 min",
                category = "educativo",
                categoryLabel = "Cores & Misturas",
                thumbnailEmoji = "🌈🎨",
                pages = listOf(
                    StoryPage(1, "Num dia ensolarado, o Vermelho, o Azul e o Amarelo decidiram fazer uma corrida pela floresta!", "🏃‍♂️🌈", 0xFFFFF3E0),
                    StoryPage(2, "Quando o Azul abraçou o Amarelo, nasceu um amigo novo: o Verde bem fresquinho!", "🟢✨", 0xFFE8F5E9),
                    StoryPage(3, "E quando o Vermelho abraçou o Amarelo, apareceu o Laranja radiante como o sol!", "🟠☀️", 0xFFFFF8E1),
                    StoryPage(4, "Juntas, todas as cores formaram um arco-íris gigante no céu para alegrar todas as crianças!", "🌈🎈", 0xFFEDE7F6)
                )
            ),
            VideoStory(
                id = "v3",
                title = "Zé e os Amigos da Fazenda",
                duration = "4 min",
                category = "educativo",
                categoryLabel = "Animais & Sons",
                thumbnailEmoji = "🚜🐮",
                pages = listOf(
                    StoryPage(1, "O Zé Traquina acordou bem cedinho com o cocorocó do Galo Galante na fazenda!", "shut🐓☀️", 0xFFFFF8E1),
                    StoryPage(2, "Na lagoa, a Dona Vaca Vaidosa deu um 'Muuu' bem alegre e ofereceu leite quentinho!", "🐮🥛", 0xFFE1F5FE),
                    StoryPage(3, "Os porquinhos brincavam na lama dando risadinhas 'Oinc Oinc' super felizes!", "🐷🛁", 0xFFFCE4EC),
                    StoryPage(4, "Que dia especial aprendendo os sons dos animais com o Zé!", "🌾🚜", 0xFFE8F5E9)
                )
            ),
            VideoStory(
                id = "v4",
                title = "A Viagem dos Números de 1 a 10",
                duration = "3 min",
                category = "educativo",
                categoryLabel = "Matemática & Contagem",
                thumbnailEmoji = "🔢🎈",
                pages = listOf(
                    StoryPage(1, "O número 1 subiu no balão mágico e convidou o número 2 para ver as nuvens!", "🎈1️⃣", 0xFFE1F5FE),
                    StoryPage(2, "Os números 3, 4 e 5 fizeram uma roda cantando e batendo palminhas no ar!", "👏3️⃣", 0xFFFFF8E1),
                    StoryPage(3, "Chegaram os números 6, 7 e 8 trazendo estrelinhas brilhantes para contar!", "⭐6️⃣", 0xFFE8F5E9),
                    StoryPage(4, "E o 9 com o 10 completaram dez amigos cheios de alegria e sabedoria!", "🔟🏆", 0xFFFCE4EC)
                )
            ),
            VideoStory(
                id = "v5",
                title = "História para Adormecer: A Estrela Guia",
                duration = "3 min",
                category = "educativo",
                categoryLabel = "Calma & Relaxamento",
                thumbnailEmoji = "🌙⭐",
                pages = listOf(
                    StoryPage(1, "A noite chegou ao Universo Zé Traquina, cobrindo o céu com um manto azul aveludado.", "🌌🌙", 0xFFE1F5FE),
                    StoryPage(2, "Uma estrelinha muito carinhosa chamada Bibi acendeu a sua luz dourada para proteger os teus sonhos.", "⭐✨", 0xFFFFF8E1),
                    StoryPage(3, "Fecha os olhos devagar, respira fundo e sente o abraço quentinho do sono...", "💤😴", 0xFFE8F5E9),
                    StoryPage(4, "Tem sonhos lindos e cheios de magia! Boa noite, amiguinho!", "🌙💤", 0xFFFCE4EC)
                )
            ),

            // --- BRINCADEIRAS ---
            VideoStory(
                id = "b1",
                title = "A Dança Contagiante do Zé Traquina",
                duration = "3 min",
                category = "brincadeiras",
                categoryLabel = "Mexe o Corpo & Dança",
                thumbnailEmoji = "🕺⚡",
                pages = listOf(
                    StoryPage(1, "Levanta da cadeira! O Zé Traquina começa a dar 3 saltinhos para cima! Vamos lá!", "🤸‍♂️💨", 0xFFFFE0B2),
                    StoryPage(2, "Agora gira como um pião, mexe as mãos no ar e faz a dança do robô divertido!", "🤖🌀", 0xFFE1BEE7),
                    StoryPage(3, "Bate palmas no ritmo: 1, 2, 3! Pula com o pé esquerdo e depois com o direito!", "👏👟", 0xFFC8E6C9),
                    StoryPage(4, "Que energia incrível! Fica em estátua de super-herói! Boa!", "🦸‍♂️🗿", 0xFFFFF9C4)
                )
            ),
            VideoStory(
                id = "b2",
                title = "Jogo do 'Siga o Mestre' e Estátuas",
                duration = "2 min",
                category = "brincadeiras",
                categoryLabel = "Desafios & Atenção",
                thumbnailEmoji = "🗿🎶",
                pages = listOf(
                    StoryPage(1, "O Zé é o mestre! Quando a música tocar, tu podes dançar e correr à vontade!", "🎵🏃‍♀️", 0xFFB2EBF2),
                    StoryPage(2, "Atenção... Parou! Congela como uma estátua sem piscar nem mexer os dedinhos!", "🗿🥶", 0xFFE1F5FE),
                    StoryPage(3, "Faz agora uma careta bem engraçada de leão zangado... RRAAAR!", "🦁🤪", 0xFFFFF8E1),
                    StoryPage(4, "Uau, ganhaste a brincadeira! Es o rei das estátuas!", "👑🎉", 0xFFDCEDC8)
                )
            ),
            VideoStory(
                id = "b3",
                title = "Adivinhas e Traquinices do Zé",
                duration = "4 min",
                category = "brincadeiras",
                categoryLabel = "Raciocínio & Gargalhadas",
                thumbnailEmoji = "❓💡",
                pages = listOf(
                    StoryPage(1, "Primeira adivinha do Zé: O que é, o que é? Tem orelhas compridas e adora comer cenoura?", "🥕🐰", 0xFFFFF3E0),
                    StoryPage(2, "Acertaste! É o Coelho brincalhão! E agora: O que vive na água e faz 'Glub Glub'?", "🐟🌊", 0xFFE0F7FA),
                    StoryPage(3, "É o Peixinho colorido! E o que tem rodas e faz 'Vrum Vrum' pela estrada?", "🚗💨", 0xFFFFEBEE),
                    StoryPage(4, "É o Carro do Zé Traquina! Tu sabes tudo! Parabéns!", "🏆🧠", 0xFFE8F5E9)
                )
            ),
            VideoStory(
                id = "b4",
                title = "Ginástica dos Animais na Selva",
                duration = "3 min",
                category = "brincadeiras",
                categoryLabel = "Imitação & Exercício",
                thumbnailEmoji = "🦁🤸‍♂️",
                pages = listOf(
                    StoryPage(1, "Vamos fazer a ginástica da selva! Primeiro, pula como um sapinho bem alto!", "🐸🦘", 0xFFE8F5E9),
                    StoryPage(2, "Agora anda devagar como um elefante gigante batendo com os pés no chão: PUM! PUM!", "🐘🐾", 0xFFE1F5FE),
                    StoryPage(3, "Abre as asas e voa como um passarinho livre pelo céu azul!", "🦅☁️", 0xFFFFF8E1),
                    StoryPage(4, "Sensacional! Ficas com os músculos fortes e cheios de saúde!", "💪🦁", 0xFFFFE0B2)
                )
            ),
            VideoStory(
                id = "b5",
                title = "A Caça ao Tesouro Colorido",
                duration = "3 min",
                category = "brincadeiras",
                categoryLabel = "Exploração em Casa",
                thumbnailEmoji = "🗺️🔍",
                pages = listOf(
                    StoryPage(1, "Desafio do Zé Traquina! Encontra à tua volta algo que seja da cor VERMELHA!", "🔴👀", 0xFFFFCDD2),
                    StoryPage(2, "Muito bem! E agora... consegue encontrar um objeto que seja da cor AMARELA?", "🟡✨", 0xFFFFF9C4),
                    StoryPage(3, "Boa! Procura agora algo que seja super macio ou fofinho!", "🧸☁️", 0xFFF8BBD0),
                    StoryPage(4, "Encontraste o tesouro! Ganhaste o crachá de Detetive Traquina!", "🕵️‍♂️⭐", 0xFFC8E6C9)
                )
            )
        )
    }

    val stories = remember(categoryFilter) {
        allStories.filter { it.category == categoryFilter }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("videos_screen_$categoryFilter")
    ) {
        if (activeStory == null) {
            // --- Storybook / Video List View ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (categoryFilter == "educativo") "Vídeos Educativos 📚✨" else "Brincadeiras e Desafios 🎈⚡",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${stories.size} Vídeos",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stories) { story ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeStory = story
                                activePageIndex = 0
                                mainViewModel.tellStory(story.title, story.pages[0].text)
                            }
                            .testTag("video_story_${story.id}"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (story.category == "brincadeiras") KidStarOrange else SkyBluePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = story.thumbnailEmoji,
                                    fontSize = 28.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = story.categoryLabel,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (story.category == "brincadeiras") KidStarOrange else SkyBluePrimary
                                )
                                Text(
                                    text = story.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "⏱️ ${story.duration} • Vídeo Interativo",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Assistir",
                                tint = SunshineYellow,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // --- Interactive Reader Player View ---
            val story = activeStory!!
            val currentPage = story.pages[activePageIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            mainViewModel.stopStory()
                            activeStory = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(text = "← Voltar à lista", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "Página ${activePageIndex + 1} de ${story.pages.size}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Canvas Storybook Page Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("storybook_page_card"),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(currentPage.bgColorHex)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = story.title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Text(
                            text = currentPage.illustrationEmoji,
                            fontSize = 76.sp
                        )

                        Text(
                            text = currentPage.text,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        // Button to Trigger Zé Traquina Voice
                        Button(
                            onClick = {
                                mainViewModel.tellStory(story.title, currentPage.text)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Ouvir", tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Ouvir Zé Traquina 🎙️", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Page Control Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 90.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        enabled = activePageIndex > 0,
                        onClick = {
                            if (activePageIndex > 0) {
                                activePageIndex--
                                mainViewModel.tellStory(story.title, story.pages[activePageIndex].text)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Anterior")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Anterior")
                    }

                    Button(
                        enabled = activePageIndex < story.pages.size - 1,
                        onClick = {
                            if (activePageIndex < story.pages.size - 1) {
                                activePageIndex++
                                mainViewModel.addStars(2)
                                mainViewModel.tellStory(story.title, story.pages[activePageIndex].text)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow)
                    ) {
                        Text(text = "Próxima", color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Próxima", tint = Color.Black)
                    }
                }
            }
        }
    }
}
