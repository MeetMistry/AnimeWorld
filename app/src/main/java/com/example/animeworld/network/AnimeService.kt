package com.example.animeworld.network

import com.example.animeworld.model.AnimeDetailResponse
import com.example.animeworld.model.AnimeListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeService {
    @GET("v4/top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int
    ): AnimeListResponse

    @GET("v4/anime/{anime_id}")
    suspend fun getAnimeDetail(
        @Path("anime_id") animeId: Int
    ): AnimeDetailResponse
}