package ru.netology.newtopmovies.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.netology.newtopmovies.data.Franchise
import ru.netology.newtopmovies.data.WishMovie

@Dao
interface WishMovieDao {

    @Query("SELECT * FROM wishMovies")
    fun getWishMovies(): LiveData<List<WishMovieEntity>>

    @Insert(onConflict = REPLACE)
    fun saveWishMovie(wishMovie: WishMovieEntity)

    @Query("DELETE FROM wishMovies WHERE id = :id")
    fun deleteWishMovie(id: Long)

    @Query("UPDATE wishMovies SET title = :title, year = :year WHERE id = :id")
    fun updateWishMovie(id: Long, title: String, year: Int)
}