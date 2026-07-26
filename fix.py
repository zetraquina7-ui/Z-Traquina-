with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    text = f.read()

# find the start of Quick Menu Section
idx = text.find("// --- Quick Menu Section (2x2 Grid) ---")

new_text = text[:idx] + """// --- Quick Menu Section (2x2 Grid) ---
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
                        bgColor = Color(0xFFF59E0B),
                        testTag = "home_quick_media",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Media) }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            
            Text(
                text = "Aprender, brincar e cantar juntos!",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
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
    }
}
"""
with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(new_text)
