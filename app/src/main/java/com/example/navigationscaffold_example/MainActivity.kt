package com.example.navigationscaffold_example

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navigationscaffold_example.presentation.detail.DetailScreen
import com.example.navigationscaffold_example.presentation.home.HomeScreen
import com.example.navigationscaffold_example.presentation.settings.SettingsScreen
import com.example.navigationscaffold_example.ui.theme.NavigationsuitscaffoldexampleTheme
import kotlinx.serialization.Serializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.window.core.layout.WindowWidthSizeClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var currentRoute by remember {
                mutableStateOf<Screen>(Screen.Home)
            }
            val windowWithClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
            val context = LocalContext.current

            NavigationsuitscaffoldexampleTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                ) { innerPadding ->
                    NavigationSuiteScaffold(
                        navigationSuiteItems = {
                            ScreenOptions.entries.forEachIndexed { index, screenOption ->
                                item(
                                    selected = screenOption.route == currentRoute,
                                    onClick = {
                                        currentRoute = screenOption.route
                                        navController.navigate(screenOption.route)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = screenOption.icon,
                                            contentDescription = screenOption.title
                                        )
                                    },
                                    label = {
                                        Text(text = screenOption.title)
                                    }
                                )
                            }
                        },
                        layoutType = if (windowWithClass == WindowWidthSizeClass.EXPANDED && isPhone(context)) {
                            NavigationSuiteType.NavigationRail
                        } else {
                            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                                currentWindowAdaptiveInfo()
                            )
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home
                        ) {
                            composable<Screen.Home>{
                                HomeScreen()
                            }
                            composable<Screen.Detail>{
                                DetailScreen()
                            }
                            composable<Screen.Settings>{
                                SettingsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun isPhone(context: Context): Boolean {
    val telephonyManager = ContextCompat.getSystemService(context, android.telephony.TelephonyManager::class.java)
    return telephonyManager != null && context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
}

enum class ScreenOptions(val title: String, val route: Screen, val icon: ImageVector) {
    Home("Home", Screen.Home, Icons.Default.Home),
    Detail("Detail", Screen.Detail, Icons.Default.AccountCircle),
    Settings("Setting", Screen.Settings,Icons.Default.Settings)
}

sealed interface Screen {
    @Serializable
    data object  Home: Screen

    @Serializable
    data object  Detail: Screen

    @Serializable
    data object  Settings: Screen
}

