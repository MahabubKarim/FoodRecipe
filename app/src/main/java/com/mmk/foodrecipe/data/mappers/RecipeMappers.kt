package com.mmk.foodrecipe.data.mappers

import com.mmk.foodrecipe.data.local.model.RecipeEntity
import com.mmk.foodrecipe.data.remote.dto.RecipeDetailsDto
import com.mmk.foodrecipe.data.remote.dto.RecipeSummaryDto
import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.model.RecipeDetails
import com.mmk.foodrecipe.domain.model.Ingredient // Assuming you create these domain models
import com.mmk.foodrecipe.domain.model.InstructionStep

fun RecipeSummaryDto.toDomain(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        readyInMinutes = this.readyInMinutes,
        servings = this.servings
    )
}

fun RecipeDetailsDto.toDomain(): RecipeDetails {
    return RecipeDetails(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        readyInMinutes = this.readyInMinutes,
        servings = this.servings,
        sourceUrl = this.sourceUrl,
        summary = this.summary,
        instructions = this.instructions ?: this.analyzedInstructions.flatMap { it.steps }.joinToString(separator = "\n") { "${it.number}. ${it.step}" },
        ingredients = this.extendedIngredients.map {
            Ingredient(
                name = it.nameClean ?: it.name,
                amount = it.amount,
                unit = it.unit,
                originalString = it.original,
                imageUrl = it.fullImageUrl
            )
        },
        dishTypes = this.dishTypes,
        diets = this.diets,
        isVegetarian = this.vegetarian,
        isVegan = this.vegan,
        isGlutenFree = this.glutenFree,
        isDairyFree = this.dairyFree
    )
}

fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        readyInMinutes = this.readyInMinutes,
        servings = this.servings,
        sourceUrl = null
    )
}

fun RecipeDetails.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        readyInMinutes = this.readyInMinutes,
        servings = this.servings,
        sourceUrl = this.sourceUrl
    )
}

fun RecipeEntity.toDomain(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        readyInMinutes = this.readyInMinutes,
        servings = this.servings
    )
}