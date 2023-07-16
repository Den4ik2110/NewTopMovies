package ru.netology.newtopmovies.data

import java.io.Serializable

class Country(
    val country: String? = null
): Serializable {
    override fun toString(): String {
        return country as String
    }
}
