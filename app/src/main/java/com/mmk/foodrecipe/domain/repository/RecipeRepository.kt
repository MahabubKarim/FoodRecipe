package com.mmk.foodrecipe.domain.repository

import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.model.RecipeDetails
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRandomRecipes(count: Int): Result<List<Recipe>>

    suspend fun getRecipeDetails(id: Int): Result<RecipeDetails>

    suspend fun searchRecipes(query: String): Result<List<Recipe>>

    fun getFavoriteRecipes(): Flow<List<Recipe>>

    suspend fun addFavoriteRecipe(recipe: RecipeDetails) // Use RecipeDetails to have all info for DB

    suspend fun removeFavoriteRecipe(recipeId: Int)

    suspend fun isRecipeFavorite(recipeId: Int): Boolean

    fun getFavoriteRecipesCount(): Flow<Int>
}