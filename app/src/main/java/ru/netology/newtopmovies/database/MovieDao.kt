package ru.netology.newtopmovies.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE

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

    @Query("UPDATE movies SET sequelsAndPrequels = :sequelsAndPrequels WHERE kinopoiskId = :kinopoiskId")
    fun updateSequelsAndPrequels(sequelsAndPrequels: String?, kinopoiskId: Int?)

    @Query("UPDATE movies SET review = :review WHERE id = :idMovie")
    fun updateReview(review: String?, idMovie: Long)

    @Query("UPDATE movies SET franchise = null WHERE franchise = :nameFranchise")
    fun clearFranchise(nameFranchise: String?)

    @Query("UPDATE movies SET franchise = :nameFranchise WHERE kinopoiskId = :id")
    fun addToFranchise(nameFranchise: String?, id: Int?)

    @Query("UPDATE movies SET franchise = null WHERE id = :id")
    fun clearFranchiseOneMovie(id: Long)

    @Query("UPDATE movies SET franchise = :newName WHERE franchise = :oldName")
    fun editFranchiseOneMovie(newName: String?, oldName: String?)
}