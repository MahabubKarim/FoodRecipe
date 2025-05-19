package com.mmk.foodrecipe.di

import com.mmk.foodrecipe.ui.viewmodel.RecipeDetailsViewModel
import com.mmk.foodrecipe.ui.viewmodel.FavoritesViewModel
import com.mmk.foodrecipe.ui.viewmodel.HomeViewModel
import com.mmk.foodrecipe.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) } // Injects GetRandomRecipesUseCase
    viewModel { (recipeId: Int) -> RecipeDetailsViewModel(recipeId, get(), get(), get(), get()) } // Injects GetRecipeDetailsUseCase and favorite use cases
    viewModel { SearchViewModel(get()) } // Injects SearchRecipesUseCase
    viewModel { FavoritesViewModel(get(), get()) } // Injects GetFavoriteRecipesUseCase & RemoveFavoriteUseCase
}