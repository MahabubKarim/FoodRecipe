package com.mmk.foodrecipe.di

import android.content.Context
import androidx.room.Room
import com.mmk.foodrecipe.BuildConfig
import com.mmk.foodrecipe.data.local.database.AppDatabase
import com.mmk.foodrecipe.data.remote.api.SpoonacularApiService
import com.mmk.foodrecipe.data.remote.KtorClient
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<HttpClient> {
        KtorClient.create(apiKey = BuildConfig.SPOONACULAR_API_KEY)
    }

    single<SpoonacularApiService> {
        SpoonacularApiService(get()) // Injects HttpClient
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "foodrecipe_db"
        ).fallbackToDestructiveMigration().build() // Consider migration strategies for production
    }

    single { get<AppDatabase>().recipeDao() }
}