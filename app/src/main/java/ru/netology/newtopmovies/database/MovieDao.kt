package ru.netology.newtopmovies.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface MovieDao {

    /*@Insert
    fun insertToDataBase(movie: MovieEntity)*/

    @Query("DELETE FROM movies WHERE id = :id")
    fun delete(id: Long)

    @Insert(onConflict = REPLACE)
    fun saveMovie(movie: MovieEntity)

    @Query("DELETE FROM movies")
    fun deleteAll()

    @Query("SELECT * FROM movies")
    fun getFromDataBase(): LiveData<List<MovieEntity>>

    @Query("UPDATE movies SET keySort = :key")
    fun updateKey(key: String)

}