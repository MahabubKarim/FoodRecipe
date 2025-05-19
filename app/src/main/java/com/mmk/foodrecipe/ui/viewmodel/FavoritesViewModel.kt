package com.mmk.foodrecipe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.usecase.GetFavoriteRecipesUseCase
import com.mmk.foodrecipe.domain.usecase.RemoveFavoriteRecipeUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ViewModel for the Favorites screen.
class FavoritesViewModel(
    private val getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase,
    private val removeFavoriteRecipeUseCase: RemoveFavoriteRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    // Observes the flow of favorite recipes and updates UI state.
    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoriteRecipesUseCase()
                .catch { e -> _uiState.value = FavoritesUiState.Error(e.message ?: "Error fetching favorites") }
                .collect { recipes ->
                    _uiState.value = if (recipes.isEmpty()) FavoritesUiState.Empty else FavoritesUiState.Success(recipes)
                }
        }
    }

    // Removes a recipe from favorites.
    fun removeFavorite(recipeId: Int) {
        viewModelScope.launch {
            try {
                removeFavoriteRecipeUseCase(recipeId)
                // The flow will automatically update the list
            } catch (e: Exception) {
                // Handle error, maybe show a snackbar
                // For now, we assume the flow will reflect the change or lack thereof
            }
        }
    }
}

// Sealed interface for Favorites screen UI states.
sealed interface FavoritesUiState {
    object Loading : FavoritesUiState
    object Empty : FavoritesUiState
    data class Success(val recipes: List<Recipe>) : FavoritesUiState
    data class Error(val message: String) : FavoritesUiState
}