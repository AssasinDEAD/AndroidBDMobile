package com.example.mobilebd

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MovieRepository(context: Context, private val scope: CoroutineScope) {
    private val movieDbHelper = MovieDatabaseHelper(context)
    private val photoDao = AppDatabase.getDatabase(context, scope).moviePhotoDao()

    // Добавление фильма с фото
    fun addMovieWithPhoto(title: String, rating: Float, description: String, photoName: String) {
        // Добавляем фильм в SQLite
        val movieId = movieDbHelper.insertMovie(title, rating, description)

        // Добавляем название фото в Room
        scope.launch {
            photoDao.insert(MoviePhoto(movieId = movieId.toInt(), photoName = photoName))
        }
    }

    // Получение списка фильмов с фотографиями
    fun getMoviesWithPhotos(callback: (List<MovieWithPhotos>) -> Unit) {
        scope.launch {
            val movies = movieDbHelper.getAllMovies()
            val moviesWithPhotos = movies.map { movie ->
                val photos = photoDao.getPhotosForMovie(movie.id)
                MovieWithPhotos(movie, photos)
            }
            callback(moviesWithPhotos)
        }
    }
}
