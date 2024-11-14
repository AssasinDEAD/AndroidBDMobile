package com.example.mobilebd

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Movie(
    val id: Int = 0,
    val title: String,
    val rating: Float,
    val description: String
)

class MovieDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_MOVIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_MOVIES_TABLE)
        onCreate(db)
    }

    fun insertMovie(title: String, rating: Float, description: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_RATING, rating)
            put(COLUMN_DESCRIPTION, description)
        }
        return db.insert(TABLE_MOVIES, null, values)
    }

    fun getAllMovies(): List<Movie> {
        val db = readableDatabase
        val cursor = db.query(TABLE_MOVIES, null, null, null, null, null, null)
        val movies = mutableListOf<Movie>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val rating = getFloat(getColumnIndexOrThrow(COLUMN_RATING))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                movies.add(Movie(id, title, rating, description))
            }
            close()
        }
        return movies
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Movies.db"
        const val TABLE_MOVIES = "movies"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_RATING = "rating"
        const val COLUMN_DESCRIPTION = "description"

        private const val SQL_CREATE_MOVIES_TABLE = """
            CREATE TABLE $TABLE_MOVIES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_RATING REAL,
                $COLUMN_DESCRIPTION TEXT
            )
        """

        private const val SQL_DELETE_MOVIES_TABLE = "DROP TABLE IF EXISTS $TABLE_MOVIES"
    }
}
