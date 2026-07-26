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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
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
import com.example.ui.theme.SkyBluePrimary
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
    val categories = listOf("Alfabeto 🔤", "Números 🔢", "Cores 🎨", "Formas 📐", "Animais 🦁", "Frutas 🍎", "Corpo Humano 👀")

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

    val colorItems = remember {
        listOf(
            LearnCardItem("c1", "Vermelho", "Cor Vibrante", "🔴", "Cor Vermelha", "O vermelho é a cor das cerejas, morangos e dos bombeiros!", 0xFFFF8A80),
            LearnCardItem("c2", "Azul", "Cor do Céu", "🔵", "Cor Azul", "O azul é a cor do céu num dia de sol e do vasto oceano!", 0xFF82B1FF),
            LearnCardItem("c3", "Amarelo", "Cor do Sol", "🟡", "Cor Amarela", "O amarelo brilha muito como o sol quente e os girassóis!", 0xFFFFE57F),
            LearnCardItem("c4", "Verde", "Cor da Natureza", "🟢", "Cor Verde", "O verde é a cor das folhas das árvores, da relva e dos sapinhos!", 0xFFC8E6C9),
            LearnCardItem("c5", "Laranja", "Cor Cítrica", "🟠", "Cor Laranja", "O laranja é a cor da fruta laranja e das cenouras docinhas!", 0xFFFFCC80),
            LearnCardItem("c6", "Roxo", "Cor Mágica", "🟣", "Cor Roxa", "O roxo é a cor das uvas e das violetas cheirosas!", 0xFFD1C4E9),
            LearnCardItem("c7", "Rosa", "Cor Fofinha", "🩷", "Cor Rosa", "O rosa é a cor dos flamingos elegantes e dos doces!", 0xFFF8BBD0),
            LearnCardItem("c8", "Castanho", "Cor da Terra", "🟤", "Cor Castanha", "O castanho é a cor do chocolate e dos troncos das árvores!", 0xFFD7CCC8),
            LearnCardItem("c9", "Preto", "Cor da Noite", "🖤", "Cor Preta", "O preto é a cor da noite estrelada e das zebras!", 0xFFCFD8DC),
            LearnCardItem("c10", "Branco", "Cor da Neve", "🤍", "Cor Branca", "O branco é a cor das nuvens fofinhas e do leite fresquinho!", 0xFFFFFFFF),
            LearnCardItem("c11", "Dourado", "Cor do Brilho", "🌟", "Cor Dourada", "O dourado brilha nas coroas dos reis e nas estrelas reluzentes!", 0xFFFFD54F)
        )
    }

    val shapeItems = remember {
        listOf(
            LearnCardItem("s1", "Círculo", "Redondinho", "⚪", "Forma Círculo", "O círculo é redondo como uma bola de futebol e as rodas dos carros!", 0xFFCFD8DC),
            LearnCardItem("s2", "Quadrado", "4 Lados Iguais", "⬛", "Forma Quadrado", "O quadrado tem quatro lados exatamente iguais como uma caixa!", 0xFFFFD180),
            LearnCardItem("s3", "Triângulo", "3 Pontas", "🔺", "Forma Triângulo", "O triângulo tem 3 lados e parece uma fatia de pizza deliciosa!", 0xFFFFAB91),
            LearnCardItem("s4", "Retângulo", "Porta e Livro", "▭", "Forma Retângulo", "O retângulo parece uma porta alta ou o ecrã da televisão!", 0xFFC5CAE9),
            LearnCardItem("s5", "Estrela", "Forma Mágica", "⭐", "Forma Estrela", "As estrelas brilham no céu noitinha fora com muita luz!", 0xFFFFE082),
            LearnCardItem("s6", "Coração", "Forma de Amor", "❤️", "Forma Coração", "O coração representa o amor, o carinho e os abraços quentinhos!", 0xFFFF80AB),
            LearnCardItem("s7", "Oval", "Como um Ovo", "🥚", "Forma Oval", "O oval é compridinho e parece um ovo de galinha ou de pássaro!", 0xFFFFF9C4),
            LearnCardItem("s8", "Losango", "Como um Papagaio", "🔷", "Forma Losango", "O losango parece um papagaio de papel a voar alto no céu!", 0xFF80DEEA),
            LearnCardItem("s9", "Hexágono", "6 Lados", "⬡", "Forma Hexágono", "O hexágono tem 6 lados iguais, igual às colmeias de mel das abelhas!", 0xFFA5D6A7)
        )
    }

    val animalItems = remember {
        listOf(
            LearnCardItem("a1", "Leão", "Rei da Selva", "🦁", "O Leão ruge forte!", "O leão é o rei dos animais na savana e tem uma juba bonita!", 0xFFFFCC80),
            LearnCardItem("a2", "Elefante", "Gigante Gentil", "🐘", "O Elefante tem tromba comprida!", "O elefante tem uma memória extraordinária e grandes orelhas!", 0xFFB0BEC5),
            LearnCardItem("a3", "Cão", "Melhor Amigo", "🐶", "O Cão faz au au!", "Os cães são os animais mais fiéis e adoram brincar!", 0xFFFFD54F),
            LearnCardItem("a4", "Gato", "Fofinho", "🐱", "O Gato faz miau!", "Os gatos conseguem saltar muito alto e ver no escuro!", 0xFFFFAB91),
            LearnCardItem("a5", "Sapo", "Saltitão", "🐸", "O Sapo pula na lagoa!", "Os sapos nascem como girinos na água antes de aprenderem a saltar!", 0xFFA5D6A7),
            LearnCardItem("a6", "Passarinho", "Cantor", "🐦", "O Passarinho canta!", "Os passarinhos constroem ninhos nas árvores para os seus ovinhos!", 0xFF80DEEA),
            LearnCardItem("a7", "Macaco", "Divertido", "🐒", "O Macaco come banana!", "Os macacos adoram dar cambalhotas de árvore em árvore!", 0xFFD7CCC8),
            LearnCardItem("a8", "Zebra", "As Listas", "🦓", "A Zebra tem riscas!", "Não há duas zebras com o mesmo padrão de listas pretas e brancas!", 0xFFEEEEEE),
            LearnCardItem("a9", "Girafa", "Pescoço Comprido", "🦒", "A Girafa é muito alta!", "A girafa consegue comer as folhas mais altas no topo das árvores!", 0xFFFFE082),
            LearnCardItem("a10", "Urso", "Forte e Fofo", "🐻", "O Urso adora mel!", "Os ursos dormem no inverno quentinhos nas suas cavernas!", 0xFFD7CCC8),
            LearnCardItem("a11", "Coelho", "Orelhas Grandes", "🐰", "O Coelho salta alto!", "Os coelhos adoram roer cenouras e têm um nariz pisca-pisca!", 0xFFF8BBD0),
            LearnCardItem("a12", "Raposa", "Esperta", "🦊", "A Raposa é muito esperta!", "A raposa tem uma cauda fofinha e muito alaranjada!", 0xFFFFAB91),
            LearnCardItem("a13", "Tigre", "O Gigante Riscado", "🐯", "O Tigre é muito forte!", "Os tigres são excelentes nadadores e adoram água!", 0xFFFFCC80),
            LearnCardItem("a14", "Tubarão", "Navegador dos Mares", "🦈", "O Tubarão nada no oceano!", "Os tubarões conseguem nadar muito rápido nos mares profundos!", 0xFF80DEEA),
            LearnCardItem("a15", "Golfinho", "Amigo do Mar", "🐬", "O Golfinho dá saltos na água!", "Os golfinhos são super inteligentes e comunicam com assobios!", 0xFFB3E5FC),
            LearnCardItem("a16", "Galinha", "Pede Ovos", "🐔", "A Galinha faz có có có!", "A galinha cuida dos seus pintainhos amarelos com muito amor!", 0xFFFFE082),
            LearnCardItem("a17", "Porco", "Divertido", "🐷", "O Porco faz oinc oinc!", "Os porquinhos adoram rebolar na lama para se refrescarem!", 0xFFF8BBD0),
            LearnCardItem("a18", "Ovelha", "Dá Lã Quentinha", "🐑", "A Ovelha faz mééé!", "A lã da ovelha serve para fazer camisolas quentinhas para o inverno!", 0xFFEEEEEE),
            LearnCardItem("a19", "Tartaruga", "Devagar e Bem", "🐢", "A Tartaruga leva a casa nas costas!", "As tartarugas podem viver mais de cem anos e nadar milhas no mar!", 0xFFC8E6C9),
            LearnCardItem("a20", "Borboleta", "Cores no Ar", "🦋", "A Borboleta voa de flor em flor!", "As borboletas antes de voarem eram pequenas lagartinhas!", 0xFFE1BEE7),
            LearnCardItem("a21", "Pinguim", "Amigo do Gelo", "🐧", "O Pinguim desliza na neve!", "Os pinguins são ótimos nadadores e vestem-se como se fossem a uma festa!", 0xFFCFD8DC)
        )
    }

    val fruitItems = remember {
        listOf(
            LearnCardItem("f1", "Maçã", "Vermelha e Doce", "🍎", "Maçã saborosa", "Uma maçã por dia dá muita energia e saúde!", 0xFFFFCDD2),
            LearnCardItem("f2", "Banana", "Amarela e Boa", "🍌", "Banana nutritiva", "As bananas são cheias de potássio e dão força aos teus músculos!", 0xFFFFF9C4),
            LearnCardItem("f3", "Uva", "Cacho Roxo", "🍇", "Uvas docinhas", "As uvas são ótimas para comer em bagas e fazer sumos deliciosos!", 0xFFE1BEE7),
            LearnCardItem("f4", "Morango", "Fruta Silvestre", "🍓", "Morango delicioso", "O morango tem as suas sementinhas do lado de fora da fruta!", 0xFFFFAB91),
            LearnCardItem("f5", "Melancia", "Grande e Fresca", "🍉", "Melancia sumarenta", "A melancia é muito fresca e perfeita para os dias quentes!", 0xFFC8E6C9),
            LearnCardItem("f6", "Ananás", "Tropical", "🍍", "Ananás doce", "O ananás cresce com uma coroa verde de folhas no topo!", 0xFFFFE082),
            LearnCardItem("f7", "Pera", "Sumarenta", "🍐", "Pera doce e fresca", "A pera é uma fruta muito doce e cheia de água benéfica!", 0xFFDCEDC8),
            LearnCardItem("f8", "Laranja", "Rica em Vitamina C", "🍊", "Laranja sumarenta", "Sumo de laranja fresquinho de manhã dá uma super energia!", 0xFFFFCC80),
            LearnCardItem("f9", "Limão", "Cítrico e Fresco", "🍋", "Limão fresquinho", "Com o limão fazemos limonada fresquinha e azedinha!", 0xFFFFF59D),
            LearnCardItem("f10", "Pêssego", "Pele Aveludada", "🍑", "Pêssego cheiroso", "O pêssego tem uma pele macia como um peluche!", 0xFFFFAB91),
            LearnCardItem("f11", "Cereja", "Pares Vermelhos", "🍒", "Cerejas doces", "As cerejas costumam vir aos pares e parecem brincos de frutas!", 0xFFFF8A80),
            LearnCardItem("f12", "Kiwi", "Verde por Dentro", "🥝", "Kiwi saboroso", "O kiwi tem uma casca castanha com pelo e um interior verde fantástico!", 0xFFDCEDC8),
            LearnCardItem("f13", "Manga", "Doce Tropical", "🥭", "Manga perfumada", "A manga é super doce e tem uma cor alaranjada linda!", 0xFFFFD54F),
            LearnCardItem("f14", "Coco", "Água Fresca", "🥥", "Coco com água fresquinha", "O coco cresce nas palmeiras altas na praia e tem água fresca!", 0xFFD7CCC8),
            LearnCardItem("f15", "Abacate", "Cremoso", "🥑", "Abacate nutritivo", "O abacate é ótimo para a saúde e faz batidos cremosos!", 0xFFC8E6C9),
            LearnCardItem("f16", "Figo", "Doce de Verão", "🪺", "Figo apetitoso", "Os figos amadurecem no final do verão e são super doces!", 0xFFD1C4E9),
            LearnCardItem("f17", "Ameixa", "Fresca e Sumarenta", "🫐", "Ameixa doce", "As ameixas podem ser roxas, amarelas ou vermelhas!", 0xFFE1BEE7),
            LearnCardItem("f18", "Framboesa", "Pequena e Vermelha", "🫐", "Framboesa deliciosa", "As framboesas parecem pequenas coroas vermelhas cheias de sabor!", 0xFFF8BBD0)
        )
    }

    val bodyItems = remember {
        listOf(
            LearnCardItem("b1", "Olhos", "Sentido da Visão", "👀", "Dois olhos para ver", "Com os olhos vemos as cores, os livros e o sorriso da família!", 0xFFB3E5FC),
            LearnCardItem("b2", "Nariz", "Sentido do Olfato", "👃", "Um nariz para cheirar", "O nariz sente o cheirinho das flores e dos bolos a sair do forno!", 0xFFFFF9C4),
            LearnCardItem("b3", "Boca", "Paladar e Sorriso", "👄", "Uma boca para sorrir", "A boca serve para falar, cantar músicas e saborear a comida!", 0xFFFFAB91),
            LearnCardItem("b4", "Mãos", "Tato e Ação", "🤲", "Duas mãos para abraçar", "Com as mãos fazemos festinhas, construímos brinquedos e desenhamos!", 0xFFC8E6C9),
            LearnCardItem("b5", "Pés", "Movimento", "👣", "Pés para caminhar", "Os pés levam-nos a correr na relva, saltar e jogar à bola!", 0xFFD7CCC8),
            LearnCardItem("b6", "Orelhas", "Audição", "👂", "Orelhas para ouvir", "As orelhas ajudam-nos a ouvir os sons dos passarinhos e música!", 0xFFE1BEE7),
            LearnCardItem("b7", "Cabeça", "Pensa e Imagina", "🧠", "Cabeça para pensar", "Na cabeça está o nosso cérebro que guarda todas as ideias brilhantes!", 0xFFFFE082),
            LearnCardItem("b8", "Cabelo", "Protege a Cabeça", "💇", "Cabelo bonito", "O cabelo protege a nossa cabeça do frio e do sol!", 0xFFD7CCC8),
            LearnCardItem("b9", "Dentes", "Para Mastigar", "🦷", "Dentes fortes e brancos", "Lavar os dentes todos os dias deixa-os fortes, brancos e saudáveis!", 0xFFEEEEEE),
            LearnCardItem("b10", "Língua", "Sentir os Sabores", "👅", "Língua para saborear", "A língua ajuda-nos a sentir se o gelado é doce e fresco!", 0xFFFFAB91),
            LearnCardItem("b11", "Braços", "Dar Abraços", "💪", "Braços fortes", "Os braços servem para dar abraços apertados a quem mais gostamos!", 0xFFC8E6C9),
            LearnCardItem("b12", "Pernas", "Correr e Saltar", "🦵", "Pernas rápidas", "As pernas ajudam-nos a subir escadas e saltar à corda!", 0xFFFFD54F),
            LearnCardItem("b13", "Barriga", "Guarda a Comida", "🎽", "Barriguinha quentinha", "Na barriga a comida transforma-se na energia de que precisamos!", 0xFFFFCC80),
            LearnCardItem("b14", "Joelho", "Dobra as Pernas", "🦵", "Joelho resistente", "Os joelhos dobram-se para podermos agachar e dar grandes saltos!", 0xFFB3E5FC),
            LearnCardItem("b15", "Coração", "Bate com Amor", "❤️", "Coração que bate tum tum", "O coração bate ritmadamente para levar o sangue a todo o corpo!", 0xFFFF80AB),
            LearnCardItem("b16", "Dedos", "Agarrar Brinquedos", "👆", "Dedinhos das mãos", "Temos dez dedos nas mãos para contar, pintar e agarrar coisas!", 0xFFFFF59D)
        )
    }

    val activeItems = when(selectedCategoryIndex) {
        0 -> alphabetItems
        1 -> numberItems
        2 -> colorItems
        3 -> shapeItems
        4 -> animalItems
        5 -> fruitItems
        6 -> bodyItems
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
