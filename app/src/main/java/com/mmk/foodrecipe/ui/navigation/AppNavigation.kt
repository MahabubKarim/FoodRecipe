package com.mmk.foodrecipe.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mmk.foodrecipe.ui.screen.details.RecipeDetailsScreen
import com.mmk.foodrecipe.ui.screen.favorites.FavoritesScreen
import com.mmk.foodrecipe.ui.screen.home.HomeScreen
import com.mmk.foodrecipe.ui.screen.search.SearchScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

// Sets up the navigation graph for the application.
@Composable
fun AppNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(AppScreen.Home.route) {
            HomeScreen(
                homeViewModel = koinViewModel(),
                onRecipeClick = { recipeId ->
                    navController.navigate(AppScreen.RecipeDetails.createRoute(recipeId))
                },
                onChipClick = {
                    navController.navigate(AppScreen.Favorites.route)
                }
            )
        }
        composable(AppScreen.Search.route) {
            SearchScreen(
                searchViewModel = koinViewModel(),
                onRecipeClick = { recipeId ->
                    navController.navigate(AppScreen.RecipeDetails.createRoute(recipeId))
                }
            )
        }
        composable(AppScreen.Favorites.route) {
            FavoritesScreen(
                favoritesViewModel = koinViewModel(),
                onRecipeClick = { recipeId ->
                    navController.navigate(AppScreen.RecipeDetails.createRoute(recipeId))
                }
            )
        }
        composable(
            route = AppScreen.RecipeDetails.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            RecipeDetailsScreen(
                recipeDetailsViewModel = koinViewModel(parameters = { parametersOf(recipeId) }),
                onBack = { navController.popBackStack() }
            )
        }
    }
}