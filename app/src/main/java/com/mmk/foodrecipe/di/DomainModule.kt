package com.mmk.foodrecipe.di

import com.mmk.foodrecipe.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory { GetRandomRecipesUseCase(get()) } // Injects RecipeRepository
    factory { GetRecipeDetailsUseCase(get()) }
    factory { SearchRecipesUseCase(get()) }
    factory { GetFavoriteRecipesUseCase(get()) }
    factory { AddFavoriteRecipeUseCase(get()) }
    factory { RemoveFavoriteRecipeUseCase(get()) }
    factory { IsRecipeFavoriteUseCase(get()) }
    factory { GetFavoriteRecipeCountUseCase(get()) }
}