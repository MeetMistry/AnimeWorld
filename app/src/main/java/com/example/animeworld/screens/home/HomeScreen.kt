package com.example.animeworld.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.animeworld.model.Anime
import com.example.animeworld.screens.components.NetworkErrorBanner
import com.example.animeworld.ui.theme.AnimeWorldTheme
import com.skydoves.landscapist.glide.GlideImage

@Composable
internal fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val isConnected by viewModel.isInternetConnected.collectAsStateWithLifecycle()
    val animeList by viewModel.animList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var shouldLoadNewPage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.resetAndLoad()
    }

    LaunchedEffect(shouldLoadNewPage) {
        viewModel.loadNextPage()
        shouldLoadNewPage = false
    }

    HomeScreen(
        modifier = modifier,
        isConnected = isConnected,
        isLoading = isLoading,
        animeList = animeList,
        onLoadNewPage = {
            shouldLoadNewPage = true
        },
        onAnimeClick = {
            navController.navigate("details/$it")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    isLoading: Boolean,
    animeList: List<Anime>,
    onLoadNewPage: () -> Unit,
    onAnimeClick: (Int) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Anime World",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isConnected) {
                NetworkErrorBanner()
            } else {
                LazyColumn(
                    modifier = Modifier
                ) {
                    items(animeList) { anime ->
                        AnimeCard(anime = anime, onAnimeClick = { onAnimeClick(anime.mal_id) })
                    }

                    item {
                        onLoadNewPage()
                    }

                    item {
                        if (animeList.isNotEmpty() && isLoading) {
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
                // Initial loader overlay
                if (animeList.isEmpty() && isLoading) {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimeCard(
    anime: Anime,
    modifier: Modifier = Modifier,
    onAnimeClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onAnimeClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                imageModel = { anime.images.jpg.image_url },
                modifier = Modifier
                    .size(width = 80.dp, height = 115.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)),
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Episodes: ${anime.episodes ?: "?"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Rating: ${anime.rating ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFfbc02d)
                )
            }
        }
    }
}

//region Preview

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AnimeWorldTheme {
        HomeScreen(
            modifier = Modifier
                .fillMaxSize(),
            isConnected = false,
            isLoading = false,
            animeList = emptyList(),
            onLoadNewPage = {},
            onAnimeClick = {}
        )
    }
}

//endregion