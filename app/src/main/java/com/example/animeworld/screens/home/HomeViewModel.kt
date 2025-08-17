package com.example.animeworld.screens.home

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
internal class HomeViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    val isInternetConnected: StateFlow<Boolean> = networkMonitor.isConnected

    private val _animList = MutableStateFlow<List<Anime>>(emptyList())
    val animList = _animList.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = false
    )

    private var currentPage = 1
    private var isLastPage = false

    fun resetAndLoad() {
        currentPage = 1
        isLastPage = false
        _animList.value = emptyList()
        loadNextPage()
    }

    fun loadNextPage() {
        if(isLoading.value || isLastPage) return
        _isLoading.value = true
        viewModelScope.launch {
            when(val result = animeRepository.getTopAnime(currentPage)) {
                is ApiResult.Success -> {
                    val newAnimes = result.data.data
                    val pagination = result.data.pagination
                    _animList.value = _animList.value + newAnimes
                    currentPage = pagination.current_page + 1
                    isLastPage = !pagination.has_next_page
                }

                is ApiResult.Error -> {
                    Log.e("HomeViewModel", "Error loading next page", result.exception)
                }
            }
            _isLoading.value = false
        }
    }
}