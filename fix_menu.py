with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    text = f.read()

# Replace the spacer back with the QuickMenuCard for ZéAI
target = """                    Spacer(modifier = Modifier.weight(1f))
                }
            }"""

replacement = """                    QuickMenuCard(
                        title = "ZéAI",
                        subtitle = "Falar com o Zé Traquina",
                        icon = Icons.Default.Face,
                        bgColor = Color(0xFFE53935),
                        testTag = "home_quick_chat",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Chat) }
                    )
                }
            }"""

if target in text:
    text = text.replace(target, replacement)
    with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
        f.write(text)
    print("Fixed ZéAI card!")
else:
    print("Not found")
