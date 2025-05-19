package com.mmk.foodrecipe.domain.usecase

import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteRecipesUseCase(private val repository: RecipeRepository) {
    operator fun invoke(): Flow<List<Recipe>> {
        return repository.getFavoriteRecipes()
    }
}