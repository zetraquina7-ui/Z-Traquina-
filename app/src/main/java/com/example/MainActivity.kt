package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.zIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.UniversoBottomNavBar
import com.example.ui.navigation.Screen
import com.example.ui.navigation.mainNavScreens
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.GamesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.MaisScreen
import com.example.ui.screens.MediaScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.theme.PlayfulBackgroundGradient
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.ui.theme.UniversoZeTraquinaTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            UniversoZeTraquinaTheme {
                MainAppContent(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentScreen = viewModel.currentScreen

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlayfulBackgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                UniversoBottomNavBar(
                    screens = mainNavScreens,
                    currentScreen = currentScreen,
                    onScreenSelected = { screen ->
                        viewModel.navigateTo(screen)
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    Screen.Home -> HomeScreen(
                        viewModel = viewModel,
                        onNavigate = { screen -> viewModel.navigateTo(screen) }
                    )
                    Screen.Learn -> LearnScreen(viewModel = viewModel)
                    Screen.Games -> GamesScreen(mainViewModel = viewModel)
                    Screen.Media -> MediaScreen(mainViewModel = viewModel)
                    Screen.Chat -> ChatScreen(mainViewModel = viewModel)
                    Screen.Parents -> MoreScreen(mainViewModel = viewModel)
                    Screen.More -> MaisScreen(mainViewModel = viewModel)
                }
            }
        }
    }
}
