package ru.netology.newtopmovies.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    entities = [MovieEntity::class, FranchiseEntity::class, WishMovieEntity::class],
    version = 8,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4),
        AutoMigration (from = 4, to = 5),
        AutoMigration (from = 5, to = 6),
        AutoMigration (from = 6, to = 7),
        AutoMigration (from = 7, to = 8),
    ]
)
abstract class AppDb : RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val franchiseDao: FranchiseDao
    abstract val wishMovieDao: WishMovieDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "data_base_movie.db")
                .allowMainThreadQueries()
                .build()
    }
}