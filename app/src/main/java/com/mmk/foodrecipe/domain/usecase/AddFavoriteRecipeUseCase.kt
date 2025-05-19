package com.mmk.foodrecipe.domain.usecase

import com.mmk.foodrecipe.domain.model.RecipeDetails
import com.mmk.foodrecipe.domain.repository.RecipeRepository

class AddFavoriteRecipeUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipe: RecipeDetails) {
        repository.addFavoriteRecipe(recipe)
    }
}