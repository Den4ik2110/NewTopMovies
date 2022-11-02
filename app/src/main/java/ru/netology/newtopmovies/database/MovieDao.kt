package ru.netology.newtopmovies.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.netology.newtopmovies.data.Franchise

@Dao
interface MovieDao {

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

    @Query("SELECT * FROM franchises")
    fun getFranchise(): LiveData<List<FranchiseEntity>>

    @Insert(onConflict = REPLACE)
    fun saveFranchise(franchise: FranchiseEntity)

    @Query("UPDATE movies SET franchise = null WHERE franchise = :nameFranchise")
    fun clearFranchise(nameFranchise: String?)

    @Query("UPDATE movies SET franchise = :nameFranchise WHERE id = :id")
    fun addToFranchise(nameFranchise: String?, id: Long)

    @Query("UPDATE movies SET franchise = null WHERE id = :id")
    fun clearFranchiseOneMovie(id: Long)

    @Query("UPDATE movies SET franchise = :newName WHERE franchise = :oldName")
    fun editFranchiseOneMovie(newName: String?, oldName: String?)
}