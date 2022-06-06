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
    var rating: Int = 0
) : Serializable {

    init {
         rating = humor + music + dynamic + image + dialogs + heroes + antiheroes + story + drama
     }
}
