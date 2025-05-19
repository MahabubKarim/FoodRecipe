package com.mmk.foodrecipe.domain.usecase

import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.repository.RecipeRepository

class GetRandomRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(count: Int = 10): Result<List<Recipe>> {
        return repository.getRandomRecipes(count)
    }
}