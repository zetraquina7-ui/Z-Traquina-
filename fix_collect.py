with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    text = f.read()

text = text.replace("collectIsPressedAsState(interactionSource)", "interactionSource.collectIsPressedAsState()")

with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(text)
