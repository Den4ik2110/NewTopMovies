package ru.netology.newtopmovies.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "humor")
    val humor: Int,
    @ColumnInfo(name = "music")
    val music: Int,
    @ColumnInfo(name = "dynamic")
    val dynamic: Int,
    @ColumnInfo(name = "image")
    val image: Int,
    @ColumnInfo(name = "dialogs")
    val dialogs: Int,
    @ColumnInfo(name = "heroes")
    val heroes: Int,
    @ColumnInfo(name = "antiheroes")
    val antiheroes: Int,
    @ColumnInfo(name = "story")
    val story: Int,
    @ColumnInfo(name = "drama")
    val drama: Int,
    @ColumnInfo(name = "repeat")
    val repeat: Int,
    @ColumnInfo(name = "rating")
    val rating: Int,
    @ColumnInfo(name = "keySort")
    val keySort: String = "New_Old",
    @ColumnInfo(name = "urlImage")
    val urlImage: String?,
    @ColumnInfo(name = "genre")
    val genre: String?,
    @ColumnInfo(name = "year")
    val year: String?,
    @ColumnInfo(name = "franchise")
    val franchise: String?
)

@Entity(tableName = "franchises")
class FranchiseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String
)

@Entity(tableName = "wishMovies")
class WishMovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "year")
    val year: Int
)