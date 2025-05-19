package com.mmk.foodrecipe.domain.model

data class RecipeDetails(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String,
    val summary: String, // Can be HTML
    val instructions: String?, // Plain text or parsed HTML
    val ingredients: List<Ingredient>,
    val dishTypes: List<String>,
    val diets: List<String>,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val isGlutenFree: Boolean,
    val isDairyFree: Boolean
)

data class Ingredient(
    val name: String,
    val amount: Double,
    val unit: String,
    val originalString: String,
    val imageUrl: String?
)

// You might not need InstructionStep if you simplify instructions to a single string.
 data class InstructionStep(
    val number: Int,
    val description: String
 )