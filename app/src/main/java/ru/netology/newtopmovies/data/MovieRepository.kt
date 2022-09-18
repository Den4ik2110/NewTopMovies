package ru.netology.newtopmovies.data


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.newtopmovies.database.*
import ru.netology.newtopmovies.database.toEntity
import ru.netology.newtopmovies.util.Constants

class MovieRepository(
    private val daoMovie: MovieDao,
    private val daoFranchise: FranchiseDao,
    private val daoWishMovie: WishMovieDao
) {
    val data = daoMovie.getFromDataBase().map { entities ->
        if (entities.isNotEmpty()) {
            if (keySort.value != entities[0].keySort) keySort.value = entities[0].keySort
        } else keySort.value = "null"
        when (if (entities.isEmpty()) null else entities[0].keySort) {
            Constants.ALPHABET -> entities.map { it.toMovie() }.sortedBy { it.title }
            Constants.MIN_MAX -> {
                val comparatorRating = Comparator { p1: Movie, p2: Movie -> p1.rating - p2.rating }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRating.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) }
                )
            }
            Constants.MAX_MIN -> {
                val comparatorRating = Comparator { p1: Movie, p2: Movie -> p2.rating - p1.rating }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRating.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) }
                )
            }
            Constants.OLD_NEW -> entities.map { it.toMovie() }.sortedBy { it.idMovie }
            Constants.REPEAT -> {
                val comparatorRepeat = Comparator { p1: Movie, p2: Movie -> p2.repeat - p1.repeat }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRepeat.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) }
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

    fun sortMovie(key: String) = daoMovie.updateKey(key)

    val listFranchise
        get() = daoFranchise.getFranchise().map { entities ->
            entities.map {
                it.toFranchise()
            }
        }


    fun saveFranchise(franchise: Franchise) = daoFranchise.saveFranchise(franchise.toEntity())

    fun deleteFranchise(franchise: Franchise) {
        daoMovie.clearFranchise(franchise.title)
        daoFranchise.deleteFranchise(franchise.id)
    }

    fun addToFranchise(franchise: Franchise, movie: Movie) =
        daoMovie.addToFranchise(franchise.title, movie.idMovie)

    fun clearFranchiseOneMovie(movie: Movie) = daoMovie.clearFranchiseOneMovie(movie.idMovie)

    fun editNameFranchise(franchise: Franchise, newName: String) {
        daoMovie.editFranchiseOneMovie(newName, franchise.title)
        daoFranchise.updateNameFranchise(franchise.id, newName)
    }

    val listWishMovie
        get() = daoWishMovie.getWishMovies().map { entities ->
            entities.map { it.toWishMovie() }
        }

    fun saveWishMovie(wishMovie: WishMovie) = daoWishMovie.saveWishMovie(wishMovie.toEntity())

    fun deleteWishMovie(wishMovie: WishMovie) = daoWishMovie.deleteWishMovie(wishMovie.id)

    fun updateWishMovie(wishMovie: WishMovie) = daoWishMovie.updateWishMovie(wishMovie.id, wishMovie.title, wishMovie.year)

}
