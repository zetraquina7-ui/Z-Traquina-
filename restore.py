import re

with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    text = f.read()

# I want to replace the "Aprender, brincar e cantar juntos" text block and what follows in the main layout with the original Destaques do dia + bubbles.

start_str = 'Text(\n                text = "Aprender, brincar e cantar juntos!"'

if start_str in text:
    idx = text.find(start_str)
    
    # We want to replace from this text up to the end of HomeScreen (which has `        }\n    }\n}`)
    end_of_homescreen = text.find("}\n\n@Composable\nfun QuickMenuCard", idx)
    
    if end_of_homescreen != -1:
        new_text = text[:idx] + """// --- 5. Destaques de Aprendizado do Dia ---
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

                HighlightCard(
                    category = "Matemática",
                    symbol = "123",
                    emoji = "🔢",
                    title = "Contar até 10",
                    badgeColor = KidStarOrange,
                    containerColor = Color(0xFFFFF7E6),
                    testTag = "home_highlight_math",
                    onClick = { onNavigate(Screen.Learn) }
                )

                HighlightCard(
                    category = "Letras",
                    symbol = "ABC",
                    emoji = "📚",
                    title = "O Alfabeto Mágico",
                    badgeColor = SkyBluePrimary,
                    containerColor = Color(0xFFF0F9FF),
                    testTag = "home_highlight_letters",
                    onClick = { onNavigate(Screen.Learn) }
                )
            }
        }

        FloatingZeTipBubble(
            tipText = zeTips[currentTipIndex],
            isVisible = isTipVisible,
            onDismiss = { isTipVisible = false },
            onClick = {
                viewModel.speak(zeTips[currentTipIndex].replace(Regex("[^A-Za-zÀ-ÖØ-öø-ÿ ]"), ""))
                isTipVisible = false
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .zIndex(10f)
        )
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
        coil.request.ImageRequest.Builder(context)
            .data(R.drawable.img_ze_mascot)
            .crossfade(true)
            .build()
    }

    androidx.compose.animation.AnimatedVisibility(
        visible = isVisible,
        enter = androidx.compose.animation.slideInVertically(initialOffsetY = { it }) + androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn(initialScale = 0.8f),
        exit = androidx.compose.animation.slideOutVertically(targetOffsetY = { it }) + androidx.compose.animation.fadeOut() + androidx.compose.animation.scaleOut(targetScale = 0.8f),
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
            border = androidx.compose.foundation.BorderStroke(3.dp, MintGreen)
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
                        .androidx.compose.foundation.border(2.dp, Color.White, CircleShape)
                ) {
                    coil.compose.AsyncImage(
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
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by androidx.compose.foundation.interaction.collectIsPressedAsState(interactionSource)
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
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
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.9f)),
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
                    .androidx.compose.foundation.border(
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
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}

""" + text[end_of_homescreen:]
        with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
            f.write(new_text)
        print("Restored Destaques do dia!")
    else:
        print("end of homescreen not found")
else:
    print("start str not found")

