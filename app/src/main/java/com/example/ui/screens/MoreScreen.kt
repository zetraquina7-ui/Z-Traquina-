package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun MoreScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("ze_traquina_prefs", android.content.Context.MODE_PRIVATE) }
    val userProgress by mainViewModel.userProgress.collectAsState()

    var isUnlocked by remember { mutableStateOf(false) }
    var mathInput by remember { mutableStateOf("") }
    var mathError by remember { mutableStateOf(false) }


    var childNameState by remember { mutableStateOf(userProgress.childName) }
    var ageGroupState by remember { mutableStateOf(userProgress.ageGroup) }
    var timeLimitState by remember { mutableStateOf(userProgress.timeLimitMinutes) }
    var soundEnabledState by remember { mutableStateOf(userProgress.soundEnabled) }

    var miniMaxApiKey by remember { mutableStateOf(prefs.getString("minimax_api_key", "") ?: "") }
    var miniMaxGroupId by remember { mutableStateOf(prefs.getString("minimax_group_id", "") ?: "") }
    var miniMaxVoiceId by remember { mutableStateOf(prefs.getString("minimax_voice_id", "") ?: "") }


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("more_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Área dos Pais & Configurações ⚙️",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // --- Parental Gate Card ---
        if (!isUnlocked) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Bloqueio de Pais",
                            tint = SkyBluePrimary,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Portão dos Pais 🔒",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = "Para aceder às configurações, resolve a conta abaixo:",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Quanto é 3 + 4 = ?",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = SkyBluePrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = mathInput,
                            onValueChange = {
                                mathInput = it
                                mathError = false
                            },
                            placeholder = { Text("Escreve a resposta") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )

                        if (mathError) {
                            Text(
                                text = "Resposta incorreta! Tenta novamente.",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (mathInput.trim() == "7") {
                                    isUnlocked = true
                                    mathError = false
                                } else {
                                    mathError = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                        ) {
                            Text(text = "Desbloquear Área dos Pais", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- Progress Summary (Always Visible) ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📊 Relatório de Progresso Infantil",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⭐ Estrelas", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "${userProgress.starsCount}", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = KidStarOrange)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🎮 Jogos", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "${userProgress.totalGamesPlayed}", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = SkyBluePrimary)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🔥 Sequência", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "${userProgress.streakDays} dias", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Color(0xFFFF5722))
                        }
                    }
                }
            }
        }

        // --- Unlocked Settings ---
        if (isUnlocked) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LockOpen, contentDescription = "Aberto", tint = Color(0xFF4CAF50))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Definições do Controlo Parental",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                        }

                        // Child Name Input
                        Column {
                            Text(text = "Nome da Criança:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            OutlinedTextField(
                                value = childNameState,
                                onValueChange = { childNameState = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }

                        // Age Group Selection
                        Column {
                            Text(text = "Faixa Etária:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("2-3", "4-5", "6+").forEach { age ->
                                    val isSel = ageGroupState == age
                                    Button(
                                        onClick = { ageGroupState = age },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSel) SkyBluePrimary else Color.LightGray
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = "$age Anos", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Screen Time Limit
                        Column {
                            Text(text = "Limite de Tempo de Tela (por sessão):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(15, 30, 45, 0).forEach { mins ->
                                    val isSel = timeLimitState == mins
                                    val label = if (mins == 0) "Livre" else "${mins}m"
                                    Button(
                                        onClick = { timeLimitState = mins },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSel) SunshineYellow else Color.LightGray
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = label, color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Sound Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Sons e Narração Ativados", fontWeight = FontWeight.Bold)
                            Switch(
                                checked = soundEnabledState,
                                onCheckedChange = { soundEnabledState = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = SkyBluePrimary)
                            )
                        }

                        // --- MiniMax.io Voice Cloning Integration ---
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "🎙️ Voz Clonada do Zé Traquina (MiniMax.io)",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Configura a API Key e o ID da Voz clonada no MiniMax.io para a mascote falar com a sua voz real!",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                            )

                            Text(text = "MiniMax API Key:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = miniMaxApiKey,
                                onValueChange = { miniMaxApiKey = it },
                                placeholder = { Text("Cole a tua API Key do MiniMax") },
                                singleLine = true,
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(text = "MiniMax Group ID (Opcional):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = miniMaxGroupId,
                                onValueChange = { miniMaxGroupId = it },
                                placeholder = { Text("Ex: 1812345678") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(text = "Voice ID (ID da Voz do Zé Traquina):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = miniMaxVoiceId,
                                onValueChange = { miniMaxVoiceId = it },
                                placeholder = { Text("Ex: ze_traquina_voice_01") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    prefs.edit()
                                        .putString("minimax_api_key", miniMaxApiKey.trim())
                                        .putString("minimax_group_id", miniMaxGroupId.trim())
                                        .putString("minimax_voice_id", miniMaxVoiceId.trim())
                                        .apply()

                                    mainViewModel.speak("Olá! Teste de som do Zé Traquina com a voz do MiniMax!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Testar Voz do Zé Traquina 🎙️",
                                    color = Color.Black,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        // Save Button
                        Button(
                            onClick = {
                                mainViewModel.updateParentSettings(
                                    childName = childNameState,
                                    ageGroup = ageGroupState,
                                    timeLimitMinutes = timeLimitState,
                                    soundEnabled = soundEnabledState
                                )
                                prefs.edit()
                                    .putString("minimax_api_key", miniMaxApiKey.trim())
                                    .putString("minimax_group_id", miniMaxGroupId.trim())
                                    .putString("minimax_voice_id", miniMaxVoiceId.trim())
                                    .apply()

                                mainViewModel.speak("Definições guardadas com sucesso!")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Guardar Alterações 💾", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- About App Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Sobre", tint = SkyBluePrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sobre o Universo Zé Traquina",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Aplicação 100% segura para crianças. Desenvolvida para estimular a cognição, coordenação motora, linguagem, raciocínio lógico e criatividade na educação infantil.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Versão 1.0.0 • Livre de anúncios • Ambientes Protegidos",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyBluePrimary
                    )


                }
            }
        }
    }


}
