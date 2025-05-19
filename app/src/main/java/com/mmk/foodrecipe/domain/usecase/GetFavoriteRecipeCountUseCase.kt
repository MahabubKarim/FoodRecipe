package com.mmk.foodrecipe.domain.usecase

import com.mmk.foodrecipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteRecipeCountUseCase(private val repository: RecipeRepository) {
    operator fun invoke(): Flow<Int> {
        return repository.getFavoriteRecipesCount()
    }
}