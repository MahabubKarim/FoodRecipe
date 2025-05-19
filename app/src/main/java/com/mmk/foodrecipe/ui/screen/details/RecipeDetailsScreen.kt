package com.mmk.foodrecipe.ui.screen.details

import android.text.Html
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mmk.foodrecipe.R
import com.mmk.foodrecipe.ui.screen.common.ErrorMessage
import com.mmk.foodrecipe.ui.screen.common.LoadingIndicator
import com.mmk.foodrecipe.ui.viewmodel.RecipeDetailsUiState
import com.mmk.foodrecipe.ui.viewmodel.RecipeDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    recipeDetailsViewModel: RecipeDetailsViewModel,
    onBack: () -> Unit
) {
    val uiState by recipeDetailsViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = when (uiState) {
                        is RecipeDetailsUiState.Success -> (uiState as RecipeDetailsUiState.Success).recipeDetails?.title
                        else -> "Recipe Details"
                    } ?: "Recipe Details"
                    Text(title, maxLines = 2, overflow = TextOverflow.Ellipsis,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is RecipeDetailsUiState.Success) {
                        val successState = uiState as RecipeDetailsUiState.Success
                        successState.recipeDetails?.let {
                            IconButton(onClick = { recipeDetailsViewModel.toggleFavorite() }) {
                                Icon(
                                    imageVector = if (successState.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (successState.isFavorite) Color.Red else LocalContentColor.current
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            when (uiState) {
                is RecipeDetailsUiState.Loading -> {
                    LoadingIndicator()
                }

                is RecipeDetailsUiState.Error -> {
                    val error = (uiState as RecipeDetailsUiState.Error).message
                    ErrorMessage(message = error)
                }

                is RecipeDetailsUiState.Success -> {
                    val success = uiState as RecipeDetailsUiState.Success
                    val recipe = success.recipeDetails
                    if (recipe != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(recipe.imageUrl)
                                    .crossfade(true)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.ic_launcher_background)
                                    .build(),
                                contentDescription = recipe.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(bottom = 16.dp)
                            )

                            Text(
                                text = recipe.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Ready in: ${recipe.readyInMinutes} mins | Servings: ${recipe.servings}", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text(Html.fromHtml(recipe.summary, Html.FROM_HTML_MODE_COMPACT).toString(), style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Ingredients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            recipe.ingredients.forEach { ingredient ->
                                Text("- ${ingredient.originalString}", style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Instructions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text(recipe.instructions ?: "No instructions provided.", style = MaterialTheme.typography.bodyMedium)
                        }

                        if (success.isLoadingDetails) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}
