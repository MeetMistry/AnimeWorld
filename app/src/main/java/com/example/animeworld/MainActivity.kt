package com.example.animeworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animeworld.screens.details.DetailsScreenRoute
import com.example.animeworld.screens.home.HomeScreenRoute
import com.example.animeworld.ui.theme.AnimeWorldTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeWorldTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        navController = navController,
                        startDestination = "home",
                    ) {
                        composable("home") {
                            HomeScreenRoute(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .systemBarsPadding(),
                                navController = navController
                            )
                        }
                        composable(
                            "details/{animeId}",
                            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
                        ) { backstackEntry ->
                            val animeId = backstackEntry.arguments?.getInt("animeId")
                            DetailsScreenRoute(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .systemBarsPadding(),
                                animeId = animeId ?: 0
                            )
                        }
                    }
                }
            }
        }
    }
}