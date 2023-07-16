package ru.netology.newtopmovies.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.newtopmovies.apiSetting.App
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.util.toListString
import ru.netology.newtopmovies.viewModel.MovieViewModel


class DeleteMovieDialogFragment(
    private val listener: MovieInteractionListener,
    private val movie: Movie
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it, R.style.AlertDialogTheme)
            builder.setMessage(R.string.dialog_start_delete)

                .setPositiveButton(
                    R.string.dialog_yes
                ) { _, _ ->
                    listener.removeMovie(movie)
                    parentFragmentManager.popBackStack()
                    listener.setSearchQuery(null)
                    Toast.makeText(activity,"Фильм удален",Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(
                    R.string.dialog_no
                ) { _, _ ->
                    listener.setSearchQuery(null)
                }
            setStyle(STYLE_NO_TITLE, 0)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class DeleteAllMovieDialogFragment(
    private val viewModel: MovieViewModel,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setMessage(R.string.dialog_start_delete_all)
                .setPositiveButton(
                    R.string.dialog_yes
                ) { _, _ -> viewModel.deleteAll() }
                .setNegativeButton(
                    R.string.dialog_no
                ) { _, _ -> }
            setStyle(STYLE_NO_TITLE, 0)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}


class InputIdKinopoisk(
    private val viewModel: MovieViewModel,
    private val movie: Movie,
    private val context: Context,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = MaterialAlertDialogBuilder(fragmentActivity, R.style.AlertDialogTheme)
            val inflater = fragmentActivity.layoutInflater
            val dialogView = inflater.inflate(R.layout.edit_text_from_dialog, null)
            val editText = dialogView.findViewById<EditText>(R.id.edit_text_title_franchise)
            editText.setText( if (movie.kinopoiskId == null) "" else movie.kinopoiskId.toString())
            builder.setMessage("Введите ID фильма на Кинопоиске")
                .setView(dialogView)
                .setPositiveButton(
                    "Сохранить"
                ) { _, _ ->
                    val idKinopoisk = editText.text.toString().toInt()
                    viewModel.editMovie(Movie(movie.nameRu,movie.humor,movie.music,movie.dynamic,movie.image,movie.dialogs,movie.heroes, movie.antiheroes,
                        movie.story, movie.drama, movie.repeat, movie.idMovie, movie.rating, movie.isClicked, movie.posterUrlPreview, movie.genres, movie.year,
                        movie.franchise, idKinopoisk, movie.nameOriginal, movie.posterUrl, movie.coverUrl, movie.logoUrl, movie.ratingKinopoisk,
                        movie.ratingKinopoiskVoteCount, movie.ratingImdb, movie.ratingImdbVoteCount, movie.webUrl, movie.filmLength, movie.description,
                        movie.ratingAgeLimits, movie.countries))
                    Toast.makeText(
                        context,
                        "Новый ID - $idKinopoisk",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton(
                    "Отмена"
                ) { _, _ -> }
            setStyle(STYLE_NO_TITLE, 0)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class InputNameFranchise(
    private val viewModel: MovieViewModel,
    private val movie: Movie,
    private val context: Context,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = MaterialAlertDialogBuilder(fragmentActivity, R.style.AlertDialogTheme)
            val inflater = fragmentActivity.layoutInflater;
            val dialogView = inflater.inflate(R.layout.edit_text_from_dialog, null)
            val editText = dialogView.findViewById<EditText>(R.id.edit_text_title_franchise)
            editText.setText( if (movie.franchise == null) "" else movie.franchise)
            builder.setMessage("Введите название франшизы")
                .setView(dialogView)
                .setPositiveButton(
                    "Сохранить"
                ) { _, _ ->
                    viewModel.addToFranchise(editText.text.toString(), movie.kinopoiskId)
                    if (movie.sequelsAndPrequels != null) {
                        movie.sequelsAndPrequels.toListString()?.forEach { stringId ->
                            viewModel.addToFranchise(editText.text.toString(), stringId.toInt())
                        }
                    }
                    Toast.makeText(
                        context,
                        "Настройки франшизы изменены",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton(
                    "Отмена"
                ) { _, _ -> }
            setStyle(STYLE_NO_TITLE, 0)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class AddIdKinopoisk(
    private val viewModel: MovieViewModel,
    private val context: Context,
    private val view: View
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = MaterialAlertDialogBuilder(fragmentActivity, R.style.AlertDialogTheme)
            val inflater = fragmentActivity.layoutInflater;
            val dialogView = inflater.inflate(R.layout.edit_text_from_dialog, null);
            val editText = dialogView.findViewById<EditText>(R.id.edit_text_title_franchise)
            builder.setMessage("Введите ID фильма на Кинопоиске")
                .setView(dialogView)
                .setPositiveButton(
                    "Добавить"
                ) { _, _ ->
                    val apiToken = "587ee233-7fbc-4e45-b1f2-8af58dda6335"
                    val idKinopoisk = editText.text.toString().toInt()
                    var isDublicate = false
                    viewModel.searchDataSingle.observe(this) { listMovies ->
                        listMovies.forEach { movie ->
                            if (movie.kinopoiskId == idKinopoisk)
                                isDublicate = true
                        }
                    }

                    if (!isDublicate) {
                        App.getApi().getDataId(idKinopoisk, apiToken)
                            .enqueue(object : Callback<Movie?> {
                                override fun onResponse(
                                    call: Call<Movie?>?,
                                    response: Response<Movie?>
                                ) {
                                    val newDataMovie = response.body() as Movie
                                    viewModel.addMovie(
                                        Movie(
                                            newDataMovie.nameRu,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            newDataMovie.isClicked,
                                            newDataMovie.posterUrlPreview,
                                            newDataMovie.genres,
                                            newDataMovie.year,
                                            newDataMovie.franchise,
                                            newDataMovie.kinopoiskId,
                                            newDataMovie.nameOriginal,
                                            newDataMovie.posterUrl,
                                            newDataMovie.coverUrl,
                                            newDataMovie.logoUrl,
                                            newDataMovie.ratingKinopoisk,
                                            newDataMovie.ratingKinopoiskVoteCount,
                                            newDataMovie.ratingImdb,
                                            newDataMovie.ratingImdbVoteCount,
                                            newDataMovie.webUrl,
                                            newDataMovie.filmLength,
                                            newDataMovie.description,
                                            newDataMovie.ratingAgeLimits,
                                            newDataMovie.countries
                                        )
                                    )
                                    Toast.makeText(
                                        context,
                                        "Фильм \"${newDataMovie.nameRu}\" добавлен",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onFailure(call: Call<Movie?>, t: Throwable?) {
                                    Log.d("MyLog", t.toString())
                                    Toast.makeText(
                                        context,
                                        "Ошибка загрузки данных о фильме!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                    } else {
                        Toast.makeText(
                            context,
                            "Фильм с таким ID уже добавлен!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .setNegativeButton(
                    "Отмена"
                ) { _, _ -> }
            setStyle(STYLE_NO_TITLE, 0)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class DownloadImageFragment(
    private val viewModel: MovieViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val items = arrayOf(
                "Загрузить картинку из галереи",
                "Загрузить картинку по URL"
            )

            val builder = MaterialAlertDialogBuilder(fragmentActivity)
            builder.setItems(items) { _, case ->
                when (case) {
                    0 -> viewModel.editImageMovie()
                    1 -> {
                        val urlImageFragment = UrlImageFragment(viewModel)
                        urlImageFragment.show(parentFragmentManager, "UrlImageFranchise")
                    }
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class UrlImageFragment(
    private val viewModel: MovieViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = MaterialAlertDialogBuilder(fragmentActivity)
            val inflater = fragmentActivity.layoutInflater;
            val dialogView = inflater.inflate(R.layout.edit_text_from_dialog_url, null);
            val editText = dialogView.findViewById<EditText>(R.id.edit_text_url_image)
            builder.setMessage("Введите URL картинки")
                .setView(dialogView)
                .setPositiveButton(
                    "Сохранить"
                ) { _, _ ->
                    viewModel.editUrlImage(editText.text.toString())
                }
                .setNegativeButton(
                    "Отмена"
                ) { _, _ -> }
            setStyle(STYLE_NO_TITLE, 0)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class ExportListMovies(
    private val viewModel: MovieViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val items = arrayOf(
                "Экспорт списка фильмов в формате .txt",
                "Экспорт списка фильмов в формате .db"
            )

            val builder = MaterialAlertDialogBuilder(fragmentActivity, R.style.AlertDialogTheme)
            builder.setItems(items) { _, case ->
                when (case) {
                    0 -> viewModel.writeAllMovieInTextFile()
                    1 -> viewModel.writeAllMovieInDataBaseFile()
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

