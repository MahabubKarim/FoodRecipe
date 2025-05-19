package com.mmk.foodrecipe.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RandomRecipesResponseDto(
    @SerialName("recipes")
    val recipes: List<RecipeSummaryDto>
)