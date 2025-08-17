package com.example.animeworld.network.di

import com.example.animeworld.network.repository.AnimeRepository
import com.example.animeworld.network.repository.AnimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsAnimeRepository(
        impl: AnimeRepositoryImpl
    ): AnimeRepository
}