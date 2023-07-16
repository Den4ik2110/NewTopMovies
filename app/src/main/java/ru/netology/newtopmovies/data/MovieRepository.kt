package ru.netology.newtopmovies.data


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.newtopmovies.database.*
import ru.netology.newtopmovies.database.toEntity
import ru.netology.newtopmovies.util.Constants

class MovieRepository(
    private val daoMovie: MovieDao,
    private val daoSearchQuery: SearchQueryDao
) {
    val data = daoMovie.getFromDataBase().map { entities ->
        if (entities.isNotEmpty()) {
            if (keySort.value != entities[0].keySort) keySort.value = entities[0].keySort
        } else keySort.value = "null"
        when (if (entities.isEmpty()) null else entities[0].keySort) {
            Constants.ALPHABET -> entities.map { it.toMovie() }.sortedBy { it.nameRu }
            Constants.MIN_MAX -> {
                val comparatorRating = Comparator { p1: Movie, p2: Movie -> p1.rating - p2.rating }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRating.thenComparator { p1, p2 -> p1.nameRu.compareTo(p2.nameRu) }
                )
            }
            Constants.MAX_MIN -> {
                val comparatorRating = Comparator { p1: Movie, p2: Movie -> p2.rating - p1.rating }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRating.thenComparator { p1, p2 -> p1.nameRu.compareTo(p2.nameRu) }
                )
            }
            Constants.OLD_NEW -> entities.map { it.toMovie() }.sortedBy { it.idMovie }
            Constants.REPEAT -> {
                val comparatorRepeat = Comparator { p1: Movie, p2: Movie -> p2.repeat - p1.repeat }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRepeat.thenComparator { p1, p2 -> p1.nameRu.compareTo(p2.nameRu) }
                )
            }
            else -> entities.map { it.toMovie() }.sortedByDescending { it.idMovie }
        }
    }


    val keySort = MutableLiveData<String>()

    fun movieRemove(movieId: Long) = daoMovie.delete(movieId)

    fun movieAdd(movie: Movie) = daoMovie.saveMovie(movie.toEntity())

    fun movieEdit(movie: Movie) = daoMovie.saveMovie(movie.toEntity())

    fun deleteAll() = daoMovie.deleteAll()

    fun updateSequelsAndPrequels(sequelsAndPrequels: String?, kinopoiskId: Int?) = daoMovie.updateSequelsAndPrequels(sequelsAndPrequels, kinopoiskId)

    fun addToFranchise(title: String?, kinopoiskId: Int?) =
        daoMovie.addToFranchise(title, kinopoiskId)

    fun saveSearchQuery(movie: Movie) = daoSearchQuery.saveSearchQuery(movie.toSearchEntity())

    fun deleteSearchQuery(movie: Movie) = daoSearchQuery.deleteSearchQuery(movie.idMovie)

    fun deleteAllSearchQuery() = daoSearchQuery.deleteAllSearchQuery()

    fun updateReview(review: String?, idMovie: Long) = daoMovie.updateReview(review, idMovie)

    val dataSearchQuery = daoSearchQuery.getSearchQuery().map { entities ->
        entities.map { it.toMovie() }
    }

}
