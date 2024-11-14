package com.example.mobilebd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(application, viewModelScope)
    private val _moviesWithPhotos = MutableStateFlow<List<MovieWithPhotos>>(emptyList())
    val moviesWithPhotos = _moviesWithPhotos.asStateFlow()

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            repository.getMoviesWithPhotos { movies ->
                _moviesWithPhotos.value = movies
            }
        }
    }

    fun addMovie(title: String, rating: Float, description: String, photoName: String) {
        viewModelScope.launch {
            repository.addMovieWithPhoto(title, rating, description, photoName)
            loadMovies() // Перезагружаем список после добавления
        }
    }

    // Функция для получения ресурса drawable по имени
    fun getResourceIdByName(resourceName: String): Int {
        return getApplication<Application>().resources.getIdentifier(resourceName, "drawable", getApplication<Application>().packageName)
    }
}
