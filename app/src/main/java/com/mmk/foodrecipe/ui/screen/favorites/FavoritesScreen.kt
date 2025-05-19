package com.mmk.foodrecipe.ui.screen.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mmk.foodrecipe.domain.model.Recipe
import com.mmk.foodrecipe.ui.screen.common.EmptyState
import com.mmk.foodrecipe.ui.screen.common.ErrorMessage
import com.mmk.foodrecipe.ui.screen.common.LoadingIndicator
import com.mmk.foodrecipe.ui.screen.common.RecipeCard
import com.mmk.foodrecipe.ui.viewmodel.FavoritesUiState
import com.mmk.foodrecipe.ui.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel,
    onRecipeClick: (Int) -> Unit
) {
    val uiState by favoritesViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is FavoritesUiState.Loading -> LoadingIndicator()
            is FavoritesUiState.Empty -> EmptyState(message = "You have no favorite recipes yet.")
            is FavoritesUiState.Success -> {
                FavoriteRecipeList(
                    recipes = state.recipes,
                    onRecipeClick = onRecipeClick,
                    onRemoveFavorite = { recipeId -> favoritesViewModel.removeFavorite(recipeId) }
                )
            }
            is FavoritesUiState.Error -> ErrorMessage(message = state.message) // No retry for now, flow should update
        }
    }
}

@Composable
fun FavoriteRecipeList(
    recipes: List<Recipe>,
    onRecipeClick: (Int) -> Unit,
    onRemoveFavorite: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recipes, key = { it.id }) { recipe ->
            var showDialog by remember { mutableStateOf(false) }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Remove Favorite") },
                    text = { Text("Are you sure you want to remove '${recipe.title}' from favorites?") },
                    confirmButton = {
                        TextButton(onClick = {
                            onRemoveFavorite(recipe.id)
                            showDialog = false
                        }) { Text("Remove") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe.id) }
                    )
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Remove Favorite")
                }
            }
        }
    }
}