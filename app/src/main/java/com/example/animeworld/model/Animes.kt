package com.example.animeworld.model

data class AnimeListResponse(
    val pagination: Pagination,
    val data: List<Anime>
)

data class AnimeDetailResponse(
    val data: Anime
)

data class Pagination(
    val last_visible_page: Int,
    val has_next_page: Boolean,
    val current_page: Int,
    val items: Items
)

data class Items(
    val count: Int,
    val total: Int,
    val per_page: Int
)

data class Anime(
    val mal_id: Int,
    val images: Images,
    val title: String,
    val episodes: Int?,
    val rating: String?,
    val trailer: Trailer?,
    val genres: List<Genre>,
    val synopsis: String?,
    val producers: List<Producer>
)

data class Images(
    val jpg: JpgImage,
    val webp: WebpImage
)

data class JpgImage(
    val image_url: String?,
    val small_image_url: String?,
    val large_image_url: String?
)

data class WebpImage(
    val image_url: String?,
    val small_image_url: String?,
    val large_image_url: String?
)

data class Producer(
    val mal_id: Int,
    val type: String,
    val name: String,
    val url: String
)

data class Trailer(
    val youtube_id: String?,
    val url: String?,
    val embed_url: String?,
    val images: TrailerImages?
)

data class TrailerImages(
    val image_url: String?,
    val small_image_url: String?,
    val medium_image_url: String?,
    val large_image_url: String?,
    val maximum_image_url: String?
)

data class Genre(
    val mal_id: Int,
    val type: String,
    val name: String,
    val url: String
)