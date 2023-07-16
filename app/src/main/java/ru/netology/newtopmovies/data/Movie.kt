package ru.netology.newtopmovies.data

import java.io.Serializable

data class Movie(
    val nameRu: String,
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
    val idMovie: Long = 0,
    var rating: Int = 0,
    var isClicked: Boolean = false,
    var posterUrlPreview: String? = null,
    var genres: List<Genre>? = null,
    var year: String? = null,
    var franchise: String? = null,
    val kinopoiskId: Int? = null,
    val nameOriginal: String? = null,
    val posterUrl: String? = null,
    val coverUrl: String? = null,
    val logoUrl: String? = null,
    val ratingKinopoisk: String? = null,
    val ratingKinopoiskVoteCount: String? = null,
    val ratingImdb: String? = null,
    val ratingImdbVoteCount: String? = null,
    val webUrl: String? = null,
    val filmLength: Int? = null,
    val description: String? = null,
    val ratingAgeLimits: String? = null,
    val countries: List<Country>? = null,
    val sequelsAndPrequels: List<SequelsAndPrequels>? = null,
    val review: String? = null

) : Serializable, Comparable<Movie> {

    init {
         rating = humor + music + dynamic + image + dialogs + heroes + antiheroes + story + drama + repeat
     }

    override fun compareTo(other: Movie): Int = nameRu.compareTo(other.nameRu)

    override fun toString(): String {
        return nameRu
    }

}


