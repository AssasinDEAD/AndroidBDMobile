package com.example.mobilebd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_photos")
data class MoviePhoto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int, // Foreign key для связи с Movie
    val photoName: String
)
