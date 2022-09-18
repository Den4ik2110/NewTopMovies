package ru.netology.newtopmovies.data

import java.io.Serializable

data class WishMovie(
    val title: String,
    val year: Int,
    val id: Long = 0
): Serializable