package ru.netology.newtopmovies

import java.io.Serializable

data class Movie(
    val title: String,
    val humor: Int,
    val music: Int,
    val dynamic: Int,
    val image: Int,
    val dialogs: Int,
    val heroes: Int,
    val antiheroes: Int,
    val story: Int,
    val drama: Int,
    val repeat: Int,
    val iconId: Int,
    val idMovie: Int = 0,
    var rating: Int = 0,
    var isClicked: Boolean = false
) : Serializable, Comparable<Movie> {

    init {
         rating = humor + music + dynamic + image + dialogs + heroes + antiheroes + story + drama + repeat
     }

    override fun compareTo(other: Movie): Int = title.compareTo(other.title)
}
