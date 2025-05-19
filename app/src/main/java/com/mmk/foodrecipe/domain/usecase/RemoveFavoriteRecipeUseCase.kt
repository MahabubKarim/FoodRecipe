package com.mmk.foodrecipe.domain.usecase

import com.mmk.foodrecipe.domain.repository.RecipeRepository

class RemoveFavoriteRecipeUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipeId: Int) {
        repository.removeFavoriteRecipe(recipeId)
    }
}