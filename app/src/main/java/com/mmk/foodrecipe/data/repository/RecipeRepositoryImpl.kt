package com.mmk.foodrecipe.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.mmk.foodrecipe.data.local.dao.RecipeDao
import com.mmk.foodrecipe.data.mappers.toDomain
import com.mmk.foodrecipe.data.mappers.toEntity
import com.mmk.foodrecipe.data.remote.api.SpoonacularApiService
import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.model.RecipeDetails
import com.mmk.foodrecipe.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RecipeRepositoryImpl(
    private val apiService: SpoonacularApiService,
    private val recipeDao: RecipeDao,
    private val context: Context
) : RecipeRepository {

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    override suspend fun getRandomRecipes(count: Int): Result<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@withContext Result.failure(Exception("No internet connection"))
            try {
                val dtos = apiService.getRandomRecipes(number = count)
                Result.success(dtos.map { it.toDomain() })
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getRecipeDetails(id: Int): Result<RecipeDetails> {
        return withContext(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@withContext Result.failure(Exception("No internet connection"))
            try {
                val dto = apiService.getRecipeDetails(id)
                Result.success(dto.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun searchRecipes(query: String): Result<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@withContext Result.failure(Exception("No internet connection"))
            try {
                val responseDto = apiService.searchRecipes(query = query)
                Result.success(responseDto.results.map { it.toDomain() })
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun getFavoriteRecipes(): Flow<List<Recipe>> {
        return recipeDao.getFavoriteRecipes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addFavoriteRecipe(recipe: RecipeDetails) {
        withContext(Dispatchers.IO) {
            recipeDao.insertFavoriteRecipe(recipe.toEntity())
        }
    }

    override suspend fun removeFavoriteRecipe(recipeId: Int) {
        withContext(Dispatchers.IO) {
            recipeDao.deleteFavoriteRecipe(recipeId)
        }
    }

    override suspend fun isRecipeFavorite(recipeId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            recipeDao.isFavorite(recipeId)
        }
    }

    override fun getFavoriteRecipesCount(): Flow<Int> {
        return recipeDao.getFavoriteRecipesCount()
    }
}