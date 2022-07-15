package ru.netology.newtopmovies.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.MovieRepository
import ru.netology.newtopmovies.database.AppDb
import ru.netology.newtopmovies.view.MovieInteractionListener

class MovieViewModel(application: Application) : AndroidViewModel(application),
    MovieInteractionListener {

    private val repository = MovieRepository(
        dao = AppDb.getInstance(
            context = application
        ).movieDao
    )

    val data by repository::data

    override fun removeMovie(movie: Movie) = repository.movieRemove(movie.idMovie)

    override fun editMovie(movie: Movie) = repository.movieEdit(movie)

    override fun addMovie(movie: Movie) = repository.movieAdd(movie)

    fun deleteAll() = repository.deleteAll()

    fun sortMovie(key: String) = repository.sortMovie(key)
}