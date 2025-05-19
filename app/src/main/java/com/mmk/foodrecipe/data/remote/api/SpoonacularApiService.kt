package com.mmk.foodrecipe.data.remote.api

import com.mmk.foodrecipe.data.remote.dto.RandomRecipesResponseDto
import com.mmk.foodrecipe.data.remote.dto.RecipeDetailsDto
import com.mmk.foodrecipe.data.remote.dto.RecipeSummaryDto
import com.mmk.foodrecipe.data.remote.dto.SearchRecipesResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class SpoonacularApiService(private val client: HttpClient) {
    suspend fun getRandomRecipes(number: Int = 100, tags: String? = null): List<RecipeSummaryDto> {
        val response: RandomRecipesResponseDto = client.get("recipes/random") {
            parameter("number", number)
            tags?.let { parameter("tags", it) }
        }.body()
        return response.recipes
    }

    suspend fun getRecipeDetails(id: Int): RecipeDetailsDto {
        return client.get("recipes/$id/information") {
            parameter("includeNutrition", false) // Optional: add more parameters as needed
        }.body()
    }

    suspend fun searchRecipes(query: String, number: Int = 10, offset: Int = 0): SearchRecipesResponseDto {
        return client.get("recipes/complexSearch") {
            parameter("query", query)
            parameter("number", number)
            parameter("offset", offset)
        }.body()
    }
}