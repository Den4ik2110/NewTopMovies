package ru.netology.newtopmovies.viewModel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.MovieRepository
import ru.netology.newtopmovies.data.SequelsAndPrequels
import ru.netology.newtopmovies.database.AppDb
import ru.netology.newtopmovies.util.SingleLiveEvent
import ru.netology.newtopmovies.util.fromSequelsAndPrequels
import ru.netology.newtopmovies.view.MovieInteractionListener

class MovieViewModel(application: Application) : AndroidViewModel(application),
    MovieInteractionListener {

    private val repository = MovieRepository(
        daoMovie = AppDb.getInstance(
            context = application
        ).movieDao,

        daoSearchQuery = AppDb.getInstance(
            context = application
        ).searchQueryDao
    )

    private val data by repository::data
    val keySort by repository::keySort
    val shareMovie = SingleLiveEvent<Movie>()
    val shareAllMovie = SingleLiveEvent<List<Movie>>()
    val writeAllMovieInTextFile = SingleLiveEvent<List<Movie>>()
    val writeAllMovieInDataBaseFile = SingleLiveEvent<List<Movie>>()
    val hideToolbar = SingleLiveEvent<Map<String, String>>()
    private val editImageMovie = SingleLiveEvent<String>()
    private val editUrlImage = SingleLiveEvent<String?>()
    val arrayForAdapterSearch = SingleLiveEvent<MutableList<Movie?>>()

    override fun removeMovie(movie: Movie) = repository.movieRemove(movie.idMovie)

    override fun editMovie(movie: Movie) = repository.movieEdit(movie)

    override fun addMovie(movie: Movie) = repository.movieAdd(movie)

    fun deleteAll() = repository.deleteAll()

    fun updateSequelsAndPrequels(sequelsAndPrequels: List<SequelsAndPrequels>?, kinopoiskId: Int?) {
        repository.updateSequelsAndPrequels(
            sequelsAndPrequels.fromSequelsAndPrequels(),
            kinopoiskId
        )
    }

    fun shareMovie(movie: Movie) {
        shareMovie.value = movie
    }

    fun hideToolbar(map: Map<String, String>) {
        hideToolbar.value = map
    }

    fun editUrlImage(url: String?) {
        this.editUrlImage.value = url
    }

    fun editImageMovie() = editImageMovie.call()

    fun shareAllMovie() {
        shareAllMovie.value = data.value
    }
    fun writeAllMovieInTextFile() {
        writeAllMovieInTextFile.value = data.value
    }

    fun writeAllMovieInDataBaseFile() {
        writeAllMovieInDataBaseFile.value = data.value
    }

    fun setAdapterSearch(array: MutableList<Movie?>) {
        arrayForAdapterSearch.value = array
    }

    private val searchQuery = MutableLiveData<String?>(null)

    val searchDataSingle: LiveData<List<Movie>>
        get() = searchQuery.switchMap { request ->
            val previousStepData by repository::data
            val searchData = when (request) {
                null -> previousStepData
                else -> {
                    previousStepData.switchMap { listData ->
                        val filterData = MutableLiveData<List<Movie>>()
                        val filterList = listData.filter { it.nameRu == request }
                        filterData.value = filterList
                        filterData
                    }
                }
            }
            searchData
        }

    val dataSearchQuery: LiveData<List<Movie>>
        get() {
            val previousStepData by repository::dataSearchQuery
            return previousStepData.map { it.sortedByDescending { movie -> movie.idMovie } }
        }

    override fun setSearchQuery(query: String?) {
        searchQuery.value = query
    }

    fun addToFranchise(title: String?, kinopoiskId: Int?) =
        repository.addToFranchise(title, kinopoiskId)

    fun saveSearchQuery(movie: Movie) = repository.saveSearchQuery(movie)

    fun deleteSearchQuery(movie: Movie) = repository.deleteSearchQuery(movie)

    fun deleteAllSearchQuery() = repository.deleteAllSearchQuery()

    fun updateReview(review: String?, idMovie: Long) = repository.updateReview(review, idMovie)

}