package com.mmk.foodrecipe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mmk.foodrecipe.ui.navigation.AppNavigation
import com.mmk.foodrecipe.ui.navigation.AppScreen
import com.mmk.foodrecipe.ui.theme.FoodRecipeTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodRecipeTheme {
                // Get the SystemUiController
                val systemUiController = rememberSystemUiController()

                // Determine if the status bar icons should be dark or light.
                // If the system is in dark theme, surface is usually dark, so icons should be light (darkIcons = false).
                // If the system is in light theme, surface is usually light, so icons should be dark (darkIcons = true).
                val useDarkIcons = !isSystemInDarkTheme()

                // Get the color for the status bar.
                // Your HomeAppBar uses MaterialTheme.colorScheme.surface by default.
                // If you've customized HomeAppBar's background, use that color.
                val statusBarColor = MaterialTheme.colorScheme.surface // Or your TopAppBar's background color

                // Use SideEffect to update the system UI (status bar) after composition
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = useDarkIcons
                    )
                    // Optional: You can also set the navigation bar color similarly
                    // systemUiController.setNavigationBarColor(
                    //    color = MaterialTheme.colorScheme.surface, // Or your navigation bar color
                    //    darkIcons = useDarkIcons
                    // )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppScreen()
                }

            }
        }
    }
}

// Main Composable for the app, including bottom navigation.
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // Padding is handled by AppNavigation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, AppScreen.Home.route),
        BottomNavItem("Search", Icons.Filled.Search, AppScreen.Search.route),
        BottomNavItem("Favorites", Icons.Filled.Favorite, AppScreen.Favorites.route)
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            // Only show bottom bar for top-level destinations
            val showBottomBar = bottomNavItems.any { it.route == currentDestination?.route }

            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues -> // This paddingValues should be passed to your NavHost
        AppNavigation(navController = navController, paddingValues = paddingValues)
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)