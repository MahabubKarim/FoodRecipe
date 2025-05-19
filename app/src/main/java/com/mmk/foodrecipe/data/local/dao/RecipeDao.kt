package com.mmk.foodrecipe.data.local.dao

import androidx.room.*
import com.mmk.foodrecipe.data.local.model.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM favorite_recipes ORDER BY addedAt DESC")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM favorite_recipes WHERE id = :recipeId")
    suspend fun deleteFavoriteRecipe(recipeId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :recipeId LIMIT 1)")
    suspend fun isFavorite(recipeId: Int): Boolean

    @Query("SELECT COUNT(id) FROM favorite_recipes")
    fun getFavoriteRecipesCount(): Flow<Int>
}