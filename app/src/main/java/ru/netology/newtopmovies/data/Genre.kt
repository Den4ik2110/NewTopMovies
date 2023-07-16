package ru.netology.newtopmovies.data

import java.io.Serializable


data class Genre(
    val genre: String? = null
): Serializable {
    override fun toString(): String {
        return genre as String
    }
}
