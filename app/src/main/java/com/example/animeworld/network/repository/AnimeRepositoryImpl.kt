package com.example.animeworld.network.repository

import com.example.animeworld.model.Anime
import com.example.animeworld.model.AnimeDetailResponse
import com.example.animeworld.model.AnimeListResponse
import com.example.animeworld.network.AnimeService
import com.example.animeworld.network.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeService: AnimeService
) : AnimeRepository {
    override suspend fun getTopAnime(page: Int): ApiResult<AnimeListResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = animeService.getTopAnime(page = page)
                ApiResult.Success(response)
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }

    override suspend fun getAnimeDetail(animeId: Int): ApiResult<AnimeDetailResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = animeService.getAnimeDetail(animeId = animeId)
                ApiResult.Success(response)
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}