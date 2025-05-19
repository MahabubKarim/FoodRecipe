package com.mmk.foodrecipe.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeSummaryDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("image") val imageUrl: String?,
    @SerialName("imageType") val imageType: String? = null, // e.g. "jpg"
    @SerialName("readyInMinutes") val readyInMinutes: Int? = null,
    @SerialName("servings") val servings: Int? = null,
    @SerialName("sourceUrl") val sourceUrl: String? = null
)

@Serializable
data class RecipeDetailsDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("image") val imageUrl: String?,
    @SerialName("readyInMinutes") val readyInMinutes: Int,
    @SerialName("servings") val servings: Int,
    @SerialName("sourceUrl") val sourceUrl: String,
    @SerialName("summary") val summary: String, // HTML content
    @SerialName("instructions") val instructions: String?, // Can be null or HTML
    @SerialName("analyzedInstructions") val analyzedInstructions: List<AnalyzedInstructionDto> = emptyList(),
    @SerialName("extendedIngredients") val extendedIngredients: List<ExtendedIngredientDto> = emptyList(),
    @SerialName("dishTypes") val dishTypes: List<String> = emptyList(),
    @SerialName("diets") val diets: List<String> = emptyList(),
    @SerialName("vegetarian") val vegetarian: Boolean = false,
    @SerialName("vegan") val vegan: Boolean = false,
    @SerialName("glutenFree") val glutenFree: Boolean = false,
    @SerialName("dairyFree") val dairyFree: Boolean = false,
    @SerialName("veryHealthy") val veryHealthy: Boolean = false,
    @SerialName("cheap") val cheap: Boolean = false,
    @SerialName("veryPopular") val veryPopular: Boolean = false,
)

@Serializable
data class ExtendedIngredientDto(
    @SerialName("id") val id: Int? = null, // Can be null if not from Spoonacular's ingredient DB
    @SerialName("aisle") val aisle: String?,
    @SerialName("image") val image: String?, // e.g. "flour.png"
    @SerialName("consistency") val consistency: String?,
    @SerialName("name") val name: String,
    @SerialName("nameClean") val nameClean: String?,
    @SerialName("original") val original: String, // e.g. "1 cup flour"
    @SerialName("originalName") val originalName: String?,
    @SerialName("amount") val amount: Double,
    @SerialName("unit") val unit: String,
    @SerialName("meta") val meta: List<String> = emptyList(),
) {
    val fullImageUrl: String?
        get() = image?.let { "https://spoonacular.com/cdn/ingredients_100x100/$it" }
}

@Serializable
data class AnalyzedInstructionDto(
    @SerialName("name") val name: String, // Usually empty
    @SerialName("steps") val steps: List<StepDto>
)

@Serializable
data class StepDto(
    @SerialName("number") val number: Int,
    @SerialName("step") val step: String,
    @SerialName("ingredients") val ingredients: List<IngredientRefDto> = emptyList(),
    @SerialName("equipment") val equipment: List<EquipmentRefDto> = emptyList(),
    @SerialName("length") val length: LengthDto? = null
)

@Serializable
data class IngredientRefDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("localizedName") val localizedName: String,
    @SerialName("image") val image: String
)

@Serializable
data class EquipmentRefDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("localizedName") val localizedName: String,
    @SerialName("image") val image: String
)

@Serializable
data class LengthDto(
    @SerialName("number") val number: Int,
    @SerialName("unit") val unit: String // e.g. "minutes"
)

@Serializable
data class SearchRecipesResponseDto(
    @SerialName("results") val results: List<RecipeSummaryDto>,
    @SerialName("offset") val offset: Int,
    @SerialName("number") val number: Int,
    @SerialName("totalResults") val totalResults: Int
)