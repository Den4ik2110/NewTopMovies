package ru.netology.newtopmovies.data


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.newtopmovies.database.*
import ru.netology.newtopmovies.database.toEntity

class MovieRepository(
    private val dao: MovieDao
) {
    val data = dao.getFromDataBase().map { entities ->
        if (entities.isNotEmpty()) {
            if (keySort.value != entities[0].keySort) keySort.value = entities[0].keySort
        } else keySort.value = "null"
            when (if (entities.isEmpty()) null else entities[0].keySort) {
            "A_Z" -> entities.map { it.toMovie() }.sortedBy { it.title }
            "Min_Max" -> {
                val comparatorRating = Comparator { p1: Movie, p2: Movie -> p1.rating - p2.rating }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRating.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) }
                )
            }
            "Max_Min" -> {
                val comparatorRating = Comparator { p1: Movie, p2: Movie -> p2.rating - p1.rating }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRating.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) }
                )
            }
            "Old_New" -> entities.map { it.toMovie() }.sortedBy { it.idMovie }
            "Repeat" -> {
                val comparatorRepeat = Comparator { p1: Movie, p2: Movie -> p2.repeat - p1.repeat }
                entities.map { it.toMovie() }.sortedWith(
                    comparatorRepeat.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) }
                )
            }
            else -> entities.map { it.toMovie() }.sortedByDescending { it.idMovie }
        }
    }

    val keySort = MutableLiveData<String>()

    fun movieRemove(movieId: Long) {
        dao.delete(movieId)
    }

    fun movieAdd(movie: Movie) {
        dao.saveMovie(movie.toEntity())
    }

    fun movieEdit(movie: Movie) {
        dao.saveMovie(movie.toEntity())
    }

    fun deleteAll() {
        dao.deleteAll()
    }

    fun sortMovie(key: String) {
        dao.updateKey(key)
    }
}
