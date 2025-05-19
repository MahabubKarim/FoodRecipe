package com.mmk.foodrecipe.ui.navigation

sealed class AppScreen(val route: String) {
    object Home : AppScreen("home")
    object Search : AppScreen("search")
    object Favorites : AppScreen("favorites")
    object RecipeDetails : AppScreen("recipe_details/{recipeId}") {
        fun createRoute(recipeId: Int) = "recipe_details/$recipeId"
    }
}