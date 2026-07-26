import sys

with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

target1 = """            }

            // --- Quick Menu Section (2x2 Grid) ---"""

replacement1 = """            }

            // --- Mascot Interactive Card ---
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
                        emotion = MascotEmotion.HAPPY,
                        triggerEmotionKey = userProgress.starsCount,
                        onInteract = {
                            viewModel.speak("Olá amiguinhos! Tens ${userProgress.starsCount} estrelas reluzentes!")
                            viewModel.addStars(1)
                        }
                    )

                    Spacer(modifier = Modifier.width(6.dp))

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

            // --- Quick Menu Section (2x2 Grid) ---"""

if target1 in content:
    content = content.replace(target1, replacement1)
else:
    # Try alternate match
    target_alt = "            }\n            // --- Quick Menu Section (2x2 Grid) ---"
    if target_alt in content:
        content = content.replace(target_alt, replacement1)
    else:
        print("Still not found")

with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
print("Done")
