package com.mmk.foodrecipe.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mmk.foodrecipe.R
import com.mmk.foodrecipe.ui.screen.common.ErrorMessage
import com.mmk.foodrecipe.ui.screen.common.LoadingIndicator
import com.mmk.foodrecipe.ui.screen.common.RecipeCard
import com.mmk.foodrecipe.ui.theme.FoodRecipeTheme
import com.mmk.foodrecipe.ui.viewmodel.HomeUiState
import com.mmk.foodrecipe.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    onRecipeClick: (Int) -> Unit,
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onChipClick: () -> Unit = {},
    onRecordClick: () -> Unit = {}
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val favoriteCount by homeViewModel.favoriteCount.collectAsState()

    Scaffold(
        topBar = {
            HomeAppBar(
                profileImageUrl = "https://picsum.photos/id/237/200/300", // Replace with actual or placeholder
                userName = "Maximilian",
                chipCount = favoriteCount,
                onProfileClick = onProfileClick,
                onSearchClick = onSearchClick,
                onChipClick = onChipClick,
                onRecordClick = onRecordClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { homeViewModel.fetchRandomRecipes() }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Refresh recipes")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> LoadingIndicator()
                is HomeUiState.Success -> {
                    if (state.recipes.isEmpty()) {
                        ErrorMessage(
                            message = "No recipes found. Try refreshing.",
                            onRetry = { homeViewModel.fetchRandomRecipes() })
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 8.dp,
                                end = 8.dp,
                                top = 8.dp,
                                bottom = 72.dp
                            ),
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
                }

                is HomeUiState.Error -> ErrorMessage(
                    message = state.message,
                    onRetry = { homeViewModel.fetchRandomRecipes() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    profileImageUrl: String?,
    userName: String,
    chipCount: Int,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onChipClick: () -> Unit,
    onRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onProfileClick)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profileImageUrl)
                        .placeholder(R.drawable.ic_launcher_foreground) // Replace with a generic profile placeholder
                        .error(R.drawable.ic_launcher_foreground)       // Replace
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "View Profile",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        actions = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                /*IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(28.dp)
                    )
                }*/

                // Chip-like element
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(
                            color = Color(0xFF6A5ACD), // SlateBlue, adjust as needed
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(onClick = onChipClick)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Points",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = chipCount.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                /*IconButton(onClick = onRecordClick) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color.Red.copy(alpha = 0.8f), CircleShape)
                            .padding(3.dp)
                            .background(Color.Red, CircleShape), // Inner red circle (solid part)
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Circle,
                            contentDescription = "Record",
                            tint = Color.White, // Or a slightly darker red for inner ring
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }*/
                Spacer(modifier = Modifier.width(4.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview(showBackground = true)
@Composable
fun HomeAppBarPreview() {
    FoodRecipeTheme {
        HomeAppBar(
            profileImageUrl = null,
            userName = "Santiago",
            chipCount = 458,
            onProfileClick = {},
            onSearchClick = {},
            onChipClick = {},
            onRecordClick = {}
        )
    }
}

// Dummy ViewModel and State for Preview - you can remove this if your koinViewModel() setup works in previews
// Or keep it and make your actual HomeViewModel constructor have default parameters for preview.
// class PreviewHomeViewModel : HomeViewModel(GetRandomRecipesUseCase(object : RecipeRepository { /* ... dummy implementations ... */ }))

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FoodRecipeTheme {
        // You'll need to provide a dummy HomeViewModel or mock its dependencies for the preview
        // For simplicity, I'm assuming the default koinViewModel() might work or you'll adjust
        HomeScreen(
            // homeViewModel = koinViewModel(), // This might crash preview if Koin isn't set up for preview
            onRecipeClick = {}
        )
    }
}