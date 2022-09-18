package ru.netology.newtopmovies.viewModel

import android.app.Application
import android.widget.ArrayAdapter
import androidx.lifecycle.*
import ru.netology.newtopmovies.data.Franchise
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.MovieRepository
import ru.netology.newtopmovies.data.WishMovie
import ru.netology.newtopmovies.database.AppDb
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.SingleLiveEvent
import ru.netology.newtopmovies.view.MovieInteractionListener

class MovieViewModel(application: Application) : AndroidViewModel(application),
    MovieInteractionListener {

    private val repository = MovieRepository(
        daoMovie = AppDb.getInstance(
            context = application
        ).movieDao,

        daoFranchise = AppDb.getInstance(
            context = application
        ).franchiseDao,

        daoWishMovie = AppDb.getInstance(
            context = application
        ).wishMovieDao
    )

    private val data by repository::data
    val keySort by repository::keySort
    val shareMovie = SingleLiveEvent<Movie>()
    val shareAllMovie = SingleLiveEvent<List<Movie>>()
    val hideToolbar = SingleLiveEvent<String>()
    val editImageMovie = SingleLiveEvent<String>()
    val editUrlImage = SingleLiveEvent<String?>()
    val arrayForAdapterSearch = SingleLiveEvent<MutableList<String>>()

    override fun removeMovie(movie: Movie) = repository.movieRemove(movie.idMovie)

    override fun editMovie(movie: Movie) = repository.movieEdit(movie)

    override fun addMovie(movie: Movie) = repository.movieAdd(movie)

    fun deleteAll() = repository.deleteAll()

    fun sortMovie(key: String) {
        if (keySort.value == key) return
        repository.sortMovie(key)
    }

    fun shareMovie(movie: Movie) {
        shareMovie.value = movie
    }

    fun hideToolbar(key: String) {
        hideToolbar.value = key
    }

    fun editUrlImage(url: String?) {
        editUrlImage.value = url
    }

    fun editImageMovie() = editImageMovie.call()

    fun shareAllMovie() {
        shareAllMovie.value = data.value
    }

    fun scrollToMovie(query: String): Int? {
        val listMovie: List<Movie>? = data.value
        if (listMovie != null) {
            if (listMovie.isNotEmpty()) listMovie.forEachIndexed { index, movie ->
                if (movie.title.equals(query, ignoreCase = true)) return index
            }
        }
        return null
    }

    fun setAdapterSearch(array: MutableList<String>) {
        arrayForAdapterSearch.value = array
    }

    private val searchQuery = MutableLiveData<String?>(null)

    val searchDataFranchise: LiveData<List<Movie>>
        get() {
            val previousStepData by repository::data
            return previousStepData.map { list -> list.filter { movie -> movie.franchise != null } }
        }

    val searchDataSingle: LiveData<List<Movie>>
        get() = Transformations.switchMap(searchQuery) { request ->
            val previousStepData by repository::data
            val searchData = when (request) {
                null -> previousStepData
                else -> {
                    Transformations.switchMap(previousStepData) { listData ->
                        val filterData = MutableLiveData<List<Movie>>()
                        val filterList = listData.filter { it.title == request }
                        filterData.value = filterList
                        filterData
                    }
                }
            }
            searchData
        }


    override fun setSearchQuery(query: String?) {
        searchQuery.value = query
    }

    val listFranchise by repository::listFranchise

    fun saveFranchise(franchise: Franchise) = repository.saveFranchise(franchise)

    fun deleteFranchise(franchise: Franchise) = repository.deleteFranchise(franchise)

    fun addToFranchise(franchise: Franchise, movie: Movie) =
        repository.addToFranchise(franchise, movie)

    fun clearFranchiseOneMovie(movie: Movie) = repository.clearFranchiseOneMovie(movie)

    fun editNameFranchise(franchise: Franchise, newName: String) =
        repository.editNameFranchise(franchise, newName)

    val listWishMovie by repository::listWishMovie

    fun saveWishMovie(wishMovie: WishMovie) = repository.saveWishMovie(wishMovie)

    fun deleteWishMovie(wishMovie: WishMovie) = repository.deleteWishMovie(wishMovie)

    fun updateWishMovie(wishMovie: WishMovie) = repository.updateWishMovie(wishMovie)
}