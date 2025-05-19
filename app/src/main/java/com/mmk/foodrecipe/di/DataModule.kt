package com.mmk.foodrecipe.di

import com.mmk.foodrecipe.data.repository.RecipeRepositoryImpl
import com.mmk.foodrecipe.domain.repository.RecipeRepository
import org.koin.dsl.module

val dataModule = module {
    single<RecipeRepository> {
        RecipeRepositoryImpl(
            apiService = get(), // Injects SpoonacularApiService
            recipeDao = get(),   // Injects RecipeDao
            context = get()      // Injects ApplicationContext for potential resource access
        )
    }
}