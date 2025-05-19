package com.mmk.foodrecipe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.domain.usecase.SearchRecipesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel for the Search screen.
class SearchViewModel(
    private val searchRecipesUseCase: SearchRecipesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    // Updates the search query and triggers a new search with debounce.
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel() // Cancel previous job
        if (query.length < 3) { // Don't search for very short queries
            _uiState.value = if (query.isEmpty()) SearchUiState.Idle else SearchUiState.EmptyQuery
            return
        }
        searchJob = viewModelScope.launch {
            delay(500) // Debounce: wait 500ms after last keystroke
            _uiState.value = SearchUiState.Loading
            searchRecipesUseCase(query).onSuccess { recipes ->
                _uiState.value = if (recipes.isEmpty()) SearchUiState.NoResults else SearchUiState.Success(recipes)
            }.onFailure { error ->
                _uiState.value = SearchUiState.Error(error.message ?: "Search failed")
            }
        }
    }

    // Clears the search query and results.
    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.value = SearchUiState.Idle
        searchJob?.cancel()
    }
}

// Sealed interface for Search screen UI states.
sealed interface SearchUiState {
    object Idle : SearchUiState // Initial state, nothing searched yet
    object EmptyQuery : SearchUiState // Query is too short or empty
    object Loading : SearchUiState
    data class Success(val recipes: List<Recipe>) : SearchUiState
    object NoResults : SearchUiState // Search completed, but no recipes found
    data class Error(val message: String) : SearchUiState
}