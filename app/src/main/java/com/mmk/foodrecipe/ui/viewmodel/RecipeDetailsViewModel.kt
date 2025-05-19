package com.mmk.foodrecipe.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.foodrecipe.domain.model.RecipeDetails
import com.mmk.foodrecipe.domain.usecase.AddFavoriteRecipeUseCase
import com.mmk.foodrecipe.domain.usecase.GetRecipeDetailsUseCase
import com.mmk.foodrecipe.domain.usecase.IsRecipeFavoriteUseCase
import com.mmk.foodrecipe.domain.usecase.RemoveFavoriteRecipeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel for the Recipe Details screen.
class RecipeDetailsViewModel(
    // private val savedStateHandle: SavedStateHandle, // If you pass ID via SavedStateHandle
    private val recipeId: Int, // Or pass directly via Koin viewModel factory
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val addFavoriteRecipeUseCase: AddFavoriteRecipeUseCase,
    private val removeFavoriteRecipeUseCase: RemoveFavoriteRecipeUseCase,
    private val isRecipeFavoriteUseCase: IsRecipeFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeDetailsUiState>(RecipeDetailsUiState.Loading)
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    init {
        // val recipeId: Int = savedStateHandle["recipeId"] ?: -1 // If using SavedStateHandle
        if (recipeId != -1) {
            fetchRecipeDetails(recipeId)
            checkIfFavorite(recipeId)
        } else {
            _uiState.value = RecipeDetailsUiState.Error("Invalid Recipe ID")
        }
    }

    // Fetches details for the recipe and updates UI state.
    private fun fetchRecipeDetails(id: Int) {
        viewModelScope.launch {
            _uiState.update { if (it is RecipeDetailsUiState.Success) it.copy(isLoadingDetails = true) else RecipeDetailsUiState.Loading }
            getRecipeDetailsUseCase(id).onSuccess { details ->
                _uiState.update {
                    if (it is RecipeDetailsUiState.Success) it.copy(recipeDetails = details, isLoadingDetails = false)
                    else RecipeDetailsUiState.Success(recipeDetails = details, isFavorite = (_uiState.value as? RecipeDetailsUiState.Success)?.isFavorite ?: false)
                }
            }.onFailure { error ->
                _uiState.value = RecipeDetailsUiState.Error(error.message ?: "Unknown error loading details")
            }
        }
    }

    // Checks if the current recipe is a favorite and updates UI state.
    private fun checkIfFavorite(id: Int) {
        viewModelScope.launch {
            val isFav = isRecipeFavoriteUseCase(id)
            _uiState.update {
                if (it is RecipeDetailsUiState.Success) it.copy(isFavorite = isFav)
                else RecipeDetailsUiState.Success(recipeDetails = null, isFavorite = isFav, isLoadingDetails = true) // Initial state before details load
            }
        }
    }

    // Toggles the favorite status of the current recipe.
    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState is RecipeDetailsUiState.Success && currentState.recipeDetails != null) {
            viewModelScope.launch {
                if (currentState.isFavorite) {
                    removeFavoriteRecipeUseCase(currentState.recipeDetails.id)
                } else {
                    addFavoriteRecipeUseCase(currentState.recipeDetails)
                }
                // Update the isFavorite state after DB operation
                _uiState.update {
                    (it as RecipeDetailsUiState.Success).copy(isFavorite = !currentState.isFavorite)
                }
            }
        }
    }
}

sealed class RecipeDetailsUiState {
    object Loading : RecipeDetailsUiState()
    data class Success(
        val recipeDetails: RecipeDetails?,
        val isFavorite: Boolean,
        val isLoadingDetails: Boolean = false
    ) : RecipeDetailsUiState()
    data class Error(val message: String) : RecipeDetailsUiState()
}

// Data class to represent the UI state for the Recipe Details screen.
/*
data class RecipeDetailsUiState(
    val recipeDetails: RecipeDetails? = null,
    val isFavorite: Boolean = false,
    val isLoadingDetails: Boolean = true,
    val error: String? = null
) {
    companion object {
        val Loading = RecipeDetailsUiState(isLoadingDetails = true)
        fun Error(message: String) = RecipeDetailsUiState(error = message, isLoadingDetails = false)
        fun Success(recipeDetails: RecipeDetails?, isFavorite: Boolean, isLoadingDetails: Boolean = false) =
            RecipeDetailsUiState(recipeDetails, isFavorite, isLoadingDetails)
    }
}*/
