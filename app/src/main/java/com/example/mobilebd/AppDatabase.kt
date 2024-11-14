package com.example.mobilebd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [MoviePhoto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviePhotoDao(): MoviePhotoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(MovieDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }


        private class MovieDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.moviePhotoDao())
                    }
                }
            }

            suspend fun populateDatabase(moviePhotoDao: MoviePhotoDao) {
                moviePhotoDao.insert(MoviePhoto(movieId = 1, photoName = "default_photo.jpg"))
            }
        }
    }
}
