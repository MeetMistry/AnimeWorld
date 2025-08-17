# Anime App Features Documentation

## Overview

This app provides an immersive experience for anime enthusiasts by integrating with the Jikan API. It includes features such as anime listing, detail viewing with video trailers, network state monitoring, and robust error handling.

---

## Features

### 1. **Anime Listing and Pagination**
- Fetches top anime from the Jikan API with pagination support.
- Smooth infinite scrolling implemented using Jetpack Compose and Kotlin Flows.
- Efficient data loading appending new pages as the user scrolls.

### 2. **Anime Detail Screen**
- Displays detailed anime information including:
  - Poster image (with Glide image loading).
  - Trailer video playback (YouTube) with fallback to poster image if no trailer available.
  - Title, synopsis, genres, main cast, number of episodes, and rating.
- Clean, modern UI designed with Jetpack Compose and Material 3.
- Responsive layout with elegant handling of missing or partial data.

### 3. **Video Playback**
- Integrated YouTube trailer playback embedded inside the app using WebView.
- Option to open the video in the native YouTube app for better compatibility.
- Graceful fallback to displaying anime poster image if the trailer is unavailable.

### 4. **Network State Monitoring and Error Handling**
- Real-time monitoring of network connectivity using Android’s ConnectivityManager wrapped in a Hilt-provided singleton.
- Observes network changes throughout the app using Kotlin Flows.
- Displays user-friendly no-internet banners or messages when connectivity is lost.
- Prevents API calls when offline and shows appropriate error UI.
- Uses best practices for lifecycle-aware, singleton network monitoring in Compose and ViewModels.

### 5. **Architecture and Dependencies**
- Utilizes Jetpack Compose for UI components.
- Dagger Hilt for dependency injection and lifecycle management.
- Coroutine flows for asynchronous and reactive data streams.
- Landscapist Glide for image loading in Compose.
- Robust use of sealed classes to represent API states (Loading, Success, Error), improving UI state management.

---

## How to Use

1. **Anime List Screen:**
   - Opens with a paginated list of top anime.
   - Scroll down to load more data automatically.

2. **Anime Detail Screen:**
   - Tap on an anime to view detailed information.
   - Play trailers directly or open them externally.
   - View genres, cast, ratings, and synopsis.

3. **Network Handling:**
   - Offline message banner appears if internet connectivity is lost.
   - API calls automatically paused during no connection.

---

## Setup Instructions

- Requires Android Studio with Kotlin and Compose support.
- Make sure to include the following main dependencies:
  - Jetpack Compose
  - Dagger Hilt
  - Landscapist Glide
  - Kotlin Coroutines
- Internet permission in `AndroidManifest.xml`:
