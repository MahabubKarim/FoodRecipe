package com.mmk.foodrecipe.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val sourceUrl: String?,
    val addedAt: Long = System.currentTimeMillis()
)