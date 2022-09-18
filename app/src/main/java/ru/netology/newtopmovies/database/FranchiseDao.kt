package ru.netology.newtopmovies.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.netology.newtopmovies.data.Franchise

@Dao
interface FranchiseDao {

    @Query("SELECT * FROM franchises")
    fun getFranchise(): LiveData<List<FranchiseEntity>>

    @Insert(onConflict = REPLACE)
    fun saveFranchise(franchise: FranchiseEntity)

    @Query("DELETE FROM franchises WHERE id = :id")
    fun deleteFranchise(id: Long)

    @Query("UPDATE franchises SET title = :name WHERE id = :id")
    fun updateNameFranchise(id: Long, name: String)
}