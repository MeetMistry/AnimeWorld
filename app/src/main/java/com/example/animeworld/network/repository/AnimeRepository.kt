package com.example.animeworld.network.repository

import com.example.animeworld.model.Anime
import com.example.animeworld.model.AnimeDetailResponse
import com.example.animeworld.model.AnimeListResponse
import com.example.animeworld.network.ApiResult

interface AnimeRepository {

    suspend fun getTopAnime(page: Int): ApiResult<AnimeListResponse>

    suspend fun getAnimeDetail(animeId: Int): ApiResult<AnimeDetailResponse>
}