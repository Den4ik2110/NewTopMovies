package ru.netology.newtopmovies.database

import ru.netology.newtopmovies.data.Franchise
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.WishMovie

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
    rating = rating,
    urlImage = urlImage,
    genre = genre,
    year = year,
    franchise = franchise
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
    rating = rating,
    urlImage = urlImage,
    genre = genre,
    year = year,
    franchise = franchise
)

internal fun Franchise.toEntity() = FranchiseEntity(
    id = id,
    title = title
)

internal fun FranchiseEntity.toFranchise() = Franchise(
    id = id,
    title = title
)

internal fun WishMovieEntity.toWishMovie() = WishMovie(
    id = id,
    title = title,
    year = year
)

internal fun WishMovie.toEntity() = WishMovieEntity(
    id = id,
    title = title,
    year = year
)



