package ru.netology.newtopmovies.database

import androidx.room.TypeConverter
import ru.netology.newtopmovies.data.Genre


class GenreConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromGenres(genres: List<Genre>?): String? {
            var stringGenres = ""
            genres?.forEachIndexed { index, genre ->
                stringGenres += if (index < genres.size - 1) genre.genre + ", " else genre.genre
            }
            return stringGenres.ifBlank { null }
        }

        @TypeConverter
        @JvmStatic
        fun toGenres(data: String?): List<Genre>? {
            val listGenre = mutableListOf<Genre>()
            return if (data == null) {
                null
            } else {
                data.trim().split(", ").forEach {
                    listGenre.add(Genre(it))
                }
                listGenre.toList()
            }
        }
    }
}