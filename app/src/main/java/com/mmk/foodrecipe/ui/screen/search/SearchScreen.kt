package com.mmk.foodrecipe.ui.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mmk.foodrecipe.ui.screen.common.EmptyState
import com.mmk.foodrecipe.ui.screen.common.ErrorMessage
import com.mmk.foodrecipe.ui.screen.common.LoadingIndicator
import com.mmk.foodrecipe.ui.screen.common.RecipeCard
import com.mmk.foodrecipe.ui.viewmodel.SearchUiState
import com.mmk.foodrecipe.ui.viewmodel.SearchViewModel

// Composable for the Search Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onRecipeClick: (Int) -> Unit
) {
    val searchQuery by searchViewModel.searchQuery.collectAsState()
    val uiState by searchViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchViewModel.onSearchQueryChanged(it) },
            label = { Text("Search Recipes") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchViewModel.clearSearch() }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear Search")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {
                is SearchUiState.Idle -> EmptyState(message = "Start typing to search for recipes...")
                is SearchUiState.EmptyQuery -> EmptyState(message = "Type at least 3 characters to search.")
                is SearchUiState.Loading -> LoadingIndicator()
                is SearchUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.recipes, key = { it.id }) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = { onRecipeClick(recipe.id) }
                            )
                        }
                    }
                }
                is SearchUiState.NoResults -> EmptyState(message = "No recipes found for '$searchQuery'.")
                is SearchUiState.Error -> ErrorMessage(
                    message = state.message,
                    onRetry = { searchViewModel.onSearchQueryChanged(searchQuery) } // Retry last search
                )
            }
        }
    }
}