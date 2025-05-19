package com.mmk.foodrecipe.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val readyInMinutes: Int?,
    val servings: Int?
)