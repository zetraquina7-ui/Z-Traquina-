import re

with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    text = f.read()

# Add missing imports
imports = """
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.MaterialTheme
import com.example.ui.components.MascotEmotion
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.ui.graphics.vector.ImageVector
"""
text = text.replace("import androidx.compose.material.icons.Icons", imports + "\nimport androidx.compose.material.icons.Icons")

# Remove extra } before QuickMenuCard
text = text.replace("}\n\n}\n\n@Composable\nfun QuickMenuCard(", "}\n\n@Composable\nfun QuickMenuCard(")

# Add missing } at the end
if not text.rstrip().endswith("}\n}"):
    text = text.rstrip() + "\n}\n"

with open("/app/applet/app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(text)

