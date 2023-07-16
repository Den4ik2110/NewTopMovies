package ru.netology.newtopmovies.data

import java.io.Serializable

class SequelsAndPrequels(
    val filmId: Int? = null
): Serializable {
    override fun toString(): String {
        return filmId.toString()
    }
}