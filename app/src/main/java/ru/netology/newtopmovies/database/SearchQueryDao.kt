package ru.netology.newtopmovies.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE

@Dao
interface SearchQueryDao {

    @Query("SELECT * FROM searchQuery")
    fun getSearchQuery(): LiveData<List<SearchQueryEntity>>

    @Insert(onConflict = REPLACE)
    fun saveSearchQuery(searchQuery: SearchQueryEntity)

    @Query("DELETE FROM searchQuery WHERE id = :id")
    fun deleteSearchQuery(id: Long)

    @Query("UPDATE searchQuery SET title = :title, year = :year WHERE id = :id")
    fun updateSearchQuery(id: Long, title: String, year: Int)

    @Query("DELETE FROM searchQuery")
    fun deleteAllSearchQuery()
}