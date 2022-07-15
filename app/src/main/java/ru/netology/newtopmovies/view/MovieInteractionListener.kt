package ru.netology.newtopmovies.view

import ru.netology.newtopmovies.data.Movie

interface MovieInteractionListener {

    fun removeMovie(movie: Movie)

    fun editMovie(movie: Movie)

    fun addMovie(movie: Movie)

}
