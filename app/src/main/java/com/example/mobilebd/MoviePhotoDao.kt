package com.example.mobilebd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoviePhotoDao {
    @Insert
    suspend fun insert(photo: MoviePhoto)

    @Query("SELECT * FROM movie_photos WHERE movieId = :movieId")
    suspend fun getPhotosForMovie(movieId: Int): List<MoviePhoto>
}
