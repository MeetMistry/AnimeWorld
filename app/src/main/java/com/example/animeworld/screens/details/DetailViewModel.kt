package com.example.animeworld.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeworld.model.Anime
import com.example.animeworld.network.ApiResult
import com.example.animeworld.network.NetworkMonitor
import com.example.animeworld.network.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    val isInternetConnected: StateFlow<Boolean> = networkMonitor.isConnected

    private val _animeDetail = MutableStateFlow<Anime?>(null)
    val animeDetail = _animeDetail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = false
    )

    fun loadAnimeDetail(animeId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            when(val result = animeRepository.getAnimeDetail(animeId)) {
                is ApiResult.Success -> {
                    _animeDetail.value = result.data.data
                }

                is ApiResult.Error -> {
                    Log.e("DetailViewModel", "Error loading anime detail", result.exception)
                }
            }
            _isLoading.value = false
        }
    }
}