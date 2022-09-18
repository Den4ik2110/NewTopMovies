package ru.netology.newtopmovies.util

import ru.netology.newtopmovies.data.Movie

fun Movie.editUrlImage(url: String?) = Movie(
    title = title,
    humor = humor,
    music = music,
    dynamic = dynamic,
    image = image,
    dialogs = dialogs,
    heroes = dialogs,
    antiheroes = antiheroes,
    story = story,
    drama = drama,
    repeat = repeat,
    idMovie = idMovie,
    rating = rating,
    isClicked = isClicked,
    urlImage = url,
    genre = genre,
    year = year,
    franchise = franchise,
)