package ru.netology.newtopmovies.database

import androidx.room.*

@Dao
interface FranchiseDao {

    /*@Query("SELECT * FROM franchises")
    fun getFranchise(): LiveData<List<FranchiseEntity>>

    @Insert(onConflict = REPLACE)
    fun saveFranchise(franchise: FranchiseEntity)

    @Query("DELETE FROM franchises WHERE id = :id")
    fun deleteFranchise(id: Long)

    @Query("UPDATE franchises SET title = :newName WHERE title = :oldName")
    fun updateNameFranchise(oldName: String?, newName: String?)*/
}