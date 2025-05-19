package com.mmk.foodrecipe.domain.usecase

import com.mmk.foodrecipe.domain.repository.RecipeRepository

class IsRecipeFavoriteUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipeId: Int): Boolean {
        return repository.isRecipeFavorite(recipeId)
    }
}