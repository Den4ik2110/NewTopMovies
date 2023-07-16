package ru.netology.newtopmovies.util

import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.SequelsAndPrequels

fun Movie.editUrlImage(url: String?) = Movie(
    nameRu = nameRu,
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
    posterUrlPreview = url,
    genres = genres,
    year = year,
    franchise = franchise,
    kinopoiskId = kinopoiskId,
    nameOriginal = nameOriginal,
    posterUrl = posterUrl,
    coverUrl = coverUrl,
    logoUrl = logoUrl,
    ratingKinopoisk = ratingKinopoisk,
    ratingKinopoiskVoteCount = ratingKinopoiskVoteCount,
    ratingImdb = ratingImdb,
    ratingImdbVoteCount = ratingImdbVoteCount,
    webUrl = webUrl,
    filmLength = filmLength,
    description = description,
    ratingAgeLimits = ratingAgeLimits,
    countries = countries,
    sequelsAndPrequels = sequelsAndPrequels
)

fun String.plural() : String {
    val stringNew = when (this) {
        "приключения" -> "Приключения"
        "фэнтези" -> "Фэнтези"
        "мультфильм" -> "Мультфильмы"
        "семейный" -> "Семейные"
        "аниме" -> "Аниме"
        "комедия" -> "Комедии"
        "триллер" -> "Триллеры"
        "криминал" -> "Криминал"
        "детектив" -> "Детективы"
        "фантастика" -> "Фантастика"
        "боевик" -> "Боевики"
        "мелодрама" -> "Мелодрамы"
        "драма" -> "Драмы"
        "военный" -> "Военные"
        "история" -> "Исторические"
        "ужасы" -> "Ужасы"
        "вестерн" -> "Вестерны"
        "биография" -> "Биографические"
        "спорт" -> "Спортивные"
        "мюзикл" -> "Мюзиклы"
        "музыка" -> "Музыкальные"
        else -> "Надо добавить жанр"
    }
    return stringNew
}

fun String.singular() : String {
    val stringNew = when (this) {
        "Приключения" -> "приключения"
        "Фэнтези" -> "фэнтези"
        "Мультфильмы" -> "мультфильм"
        "Семейные" -> "семейный"
        "Аниме" -> "аниме"
        "Комедии" -> "комедия"
        "Триллеры" -> "триллер"
        "Криминал" -> "криминал"
        "Детективы" -> "детектив"
        "Фантастика" -> "фантастика"
        "Боевики" -> "боевик"
        "Мелодрамы" -> "мелодрама"
        "Драмы" -> "драма"
        "Военные" -> "военный"
        "Исторические" -> "история"
        "Ужасы" -> "ужасы"
        "Вестерны" -> "вестерн"
        "Биографические" -> "биография"
        "Спортивные" -> "спорт"
        "Мюзиклы" -> "мюзикл"
        "Музыкальные" -> "музыка"
        else -> "Надо добавить жанр"
    }
    return stringNew
}

fun List<SequelsAndPrequels>?.fromSequelsAndPrequels() : String? {
    var stringSequelsAndPrequels = ""
    this?.forEachIndexed { index, sequelsAndPrequelsOne ->
        stringSequelsAndPrequels += if (index < this.size - 1) sequelsAndPrequelsOne.filmId.toString() + ", " else sequelsAndPrequelsOne.filmId.toString()
    }
    return stringSequelsAndPrequels.ifBlank { null }
}

fun List<SequelsAndPrequels>?.toListString(): List<String>? {
    val listString = mutableListOf<String>()
    return if (this == null) {
        null
    } else {
        this.fromSequelsAndPrequels()?.trim()?.split(", ")?.forEach {
            listString.add(it)
        }
        listString.toList()
    }
}

fun String?.toSequelsAndPrequels() : List<SequelsAndPrequels>? {
    val listSequelsAndPrequels = mutableListOf<SequelsAndPrequels>()
    return if (this == null) {
        null
    } else {
        this.trim().split(", ").forEach {
            listSequelsAndPrequels.add(SequelsAndPrequels(it.toInt()))
        }
        listSequelsAndPrequels.toList()
    }
}