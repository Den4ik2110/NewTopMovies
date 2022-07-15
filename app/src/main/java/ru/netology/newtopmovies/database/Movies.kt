package ru.netology.newtopmovies.database

import ru.netology.newtopmovies.data.Movie

internal fun MovieEntity.toMovie() = Movie(
    title = title,
    humor = humor,
    music = music,
    dynamic = dynamic,
    image = image,
    dialogs = dialogs,
    heroes = heroes,
    antiheroes = antiheroes,
    story = story,
    drama = drama,
    repeat = repeat,
    idMovie = id,
    rating = rating
)

internal fun Movie.toEntity() = MovieEntity(
    title = title,
    humor = humor,
    music = music,
    dynamic = dynamic,
    image = image,
    dialogs = dialogs,
    heroes = heroes,
    antiheroes = antiheroes,
    story = story,
    drama = drama,
    repeat = repeat,
    id = idMovie,
    rating = rating
)



