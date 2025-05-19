package com.mmk.foodrecipe.domain.usecase

import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.repository.RecipeRepository

class SearchRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(query: String): Result<List<Recipe>> {
        if (query.isBlank()) return Result.success(emptyList()) // Basic validation
        return repository.searchRecipes(query)
    }
}