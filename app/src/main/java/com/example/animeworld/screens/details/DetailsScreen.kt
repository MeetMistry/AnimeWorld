package com.example.animeworld.screens.details

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.animeworld.model.Anime
import com.example.animeworld.model.Genre
import com.example.animeworld.model.Images
import com.example.animeworld.model.JpgImage
import com.example.animeworld.model.Producer
import com.example.animeworld.model.Trailer
import com.example.animeworld.model.TrailerImages
import com.example.animeworld.model.WebpImage
import com.example.animeworld.screens.components.NetworkErrorBanner
import com.example.animeworld.ui.theme.AnimeWorldTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.skydoves.landscapist.glide.GlideImage

@Composable
internal fun DetailsScreenRoute(
    modifier: Modifier = Modifier,
    animeId: Int,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isInternetConnected.collectAsStateWithLifecycle()
    val anime by viewModel.animeDetail.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAnimeDetail(animeId)
    }

    DetailScreen(
        modifier = modifier,
        isConnected = isConnected,
        isLoading = isLoading,
        anime = anime
    )
}

@Composable
private fun DetailScreen(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    isLoading: Boolean,
    anime: Anime?,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (!isConnected) {
            NetworkErrorBanner(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        } else {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                anime?.let { anime ->
                    DetailScreenMainContent(
                        modifier = modifier,
                        isLoading = isLoading,
                        posterUrl = anime.images.jpg.image_url.orEmpty(),
                        trailerYoutubeId = anime.trailer?.youtube_id,
                        title = anime.title,
                        synopsis = anime.synopsis,
                        genres = anime.genres.map { it.name },
                        mainCast = anime.producers.map { it.name },
                        episodes = anime.episodes,
                        rating = anime.rating
                    )
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailScreenMainContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    posterUrl: String,
    trailerYoutubeId: String?,
    title: String,
    synopsis: String?,
    genres: List<String>,
    mainCast: List<String>,
    episodes: Int?,
    rating: String?
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                // Trailer/Poster
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                        .background(Color.LightGray, RoundedCornerShape(16.dp))
                ) {
                    if (!trailerYoutubeId.isNullOrEmpty()) {
                        // Embed YouTube player
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { ctx ->
                                val view = YouTubePlayerView(ctx)
                                view.addYouTubePlayerListener(
                                    object : AbstractYouTubePlayerListener() {
                                        override fun onReady(youTubePlayer: YouTubePlayer) {
                                            super.onReady(youTubePlayer)
                                            youTubePlayer.loadVideo(trailerYoutubeId, 0f)
                                        }
                                    }
                                )
                                view
                            }
                        )
                    } else {
                        // Show Poster Image as fallback
                        GlideImage(
                            imageModel = { posterUrl },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = synopsis ?: "No synopsis available.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Genres
                if (genres.isNotEmpty()) {
                    Text(
                        text = "Genres: " + genres.joinToString { it },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Main Cast
                if (mainCast.isNotEmpty()) {
                    Text(
                        text = "Main Cast:",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    FlowRow(
                        modifier = Modifier.padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        mainCast.forEach { cast ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFE0E0E0)
                            ) {
                                Text(
                                    text = cast,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Episodes and Rating Row
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Text(
                        text = "Episodes: ${episodes ?: "?"}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Rating: ${rating ?: "N/A"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFBC02D), // gold color for rating
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

//region Preview

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AnimeWorldTheme {
        val sampleAnime = Anime(
            mal_id = 5114,
            images = Images(
                jpg = JpgImage(
                    image_url = "https://cdn.myanimelist.net/images/anime/1208/94745.jpg",
                    small_image_url = "https://cdn.myanimelist.net/images/anime/1208/94745t.jpg",
                    large_image_url = "https://cdn.myanimelist.net/images/anime/1208/94745l.jpg"
                ),
                webp = WebpImage(
                    image_url = "https://cdn.myanimelist.net/images/anime/1208/94745.webp",
                    small_image_url = "https://cdn.myanimelist.net/images/anime/1208/94745t.webp",
                    large_image_url = "https://cdn.myanimelist.net/images/anime/1208/94745l.webp"
                )
            ),
            title = "Fullmetal Alchemist: Brotherhood",
            episodes = 64,
            rating = "R - 17+ (violence & profanity)",
            trailer = Trailer(
                youtube_id = "1ac3_YdSSy0",
                url = "https://www.youtube.com/watch?v=1ac3_YdSSy0",
                embed_url = "https://www.youtube.com/embed/1ac3_YdSSy0",
                images = TrailerImages(
                    image_url = "https://img.youtube.com/vi/1ac3_YdSSy0/default.jpg",
                    small_image_url = "https://img.youtube.com/vi/1ac3_YdSSy0/sddefault.jpg",
                    medium_image_url = "https://img.youtube.com/vi/1ac3_YdSSy0/mqdefault.jpg",
                    large_image_url = "https://img.youtube.com/vi/1ac3_YdSSy0/hqdefault.jpg",
                    maximum_image_url = "https://img.youtube.com/vi/1ac3_YdSSy0/maxresdefault.jpg"
                )
            ),
            genres = listOf(
                Genre(
                    mal_id = 1,
                    name = "Action",
                    url = "https://myanimelist.net/anime/genre/1/Action",
                    type = ""
                ),
                Genre(mal_id = 2, name = "Adventure", url = "https://myanimelist.net/anime/genre/2/Adventure", type = ""),
                Genre(mal_id = 8, name = "Drama", url = "https://myanimelist.net/anime/genre/8/Drama", type = ""),
                Genre(mal_id = 10, name = "Fantasy", url = "https://myanimelist.net/anime/genre/10/Fantasy", type = "")
            ),
            synopsis = "After a horrific alchemy experiment goes wrong, brothers Edward and Alphonse are left in a catastrophic new reality. Seeking redemption, they set out on a quest to locate the Philosopher’s Stone and restore their bodies.",
            producers = listOf(
                Producer(
                    mal_id = 17,
                    type = "anime",
                    name = "Aniplex",
                    url = "https://myanimelist.net/anime/producer/17/Aniplex"
                ),
                Producer(mal_id = 53, type = "anime", name = "Dentsu", url = "https://myanimelist.net/anime/producer/53/Dentsu"),
                Producer(mal_id = 62, type = "anime", name = "Shogakukan-Shueisha Productions", url = "https://myanimelist.net/anime/producer/62/Shogakukan-Shueisha_Productions")
            )
        )
        DetailScreen(
            modifier = Modifier
                .fillMaxSize(),
            isConnected = false,
            isLoading = false,
            anime = sampleAnime
        )
    }
}

//endregion