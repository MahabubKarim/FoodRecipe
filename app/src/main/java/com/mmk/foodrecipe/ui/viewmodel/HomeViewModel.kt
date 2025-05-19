package com.mmk.foodrecipe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.usecase.GetFavoriteRecipeCountUseCase
import com.mmk.foodrecipe.domain.usecase.GetRandomRecipesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// ViewModel for the Home screen.
class HomeViewModel(
    private val getRandomRecipesUseCase: GetRandomRecipesUseCase,
    private val getFavoriteRecipeCountUseCase: GetFavoriteRecipeCountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _favoriteCount = MutableStateFlow(0) // Default to 0
    val favoriteCount: StateFlow<Int> = _favoriteCount.asStateFlow()

    init {
        fetchRandomRecipes()
        fetchTotalFavouriteCount()
    }

    // Fetches random recipes and updates the UI state.
    fun fetchRandomRecipes() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            getRandomRecipesUseCase().onSuccess { recipes ->
                _uiState.value = HomeUiState.Success(recipes)
            }.onFailure { error ->
                _uiState.value = HomeUiState.Error(error.message ?: "Unknown error")
            }
        }
    }

    private fun fetchTotalFavouriteCount() {
        viewModelScope.launch {
            getFavoriteRecipeCountUseCase()
                .catch { e ->
                    // Handle error, e.g., log it or show a generic error for the count
                    // For now, the count will remain at its last value or default
                    System.err.println("Error fetching favorite count: ${e.localizedMessage}")
                }
                .collect { count ->
                    _favoriteCount.value = count
                }
        }
    }
}

// Sealed interface to represent different UI states for the Home screen.
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val recipes: List<Recipe>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}