package com.mmk.foodrecipe.domain.usecase

import com.mmk.foodrecipe.domain.model.RecipeDetails
import com.mmk.foodrecipe.domain.repository.RecipeRepository

class GetRecipeDetailsUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(id: Int): Result<RecipeDetails> {
        return repository.getRecipeDetails(id)
    }
}