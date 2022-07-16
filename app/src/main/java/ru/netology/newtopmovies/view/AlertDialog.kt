package ru.netology.newtopmovies.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AndroidViewModel
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.viewModel.MovieViewModel

class DeleteMovieDialogFragment(
    private val listener: MovieInteractionListener,
    private val movie: Movie
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_start_delete)
                .setPositiveButton(
                    R.string.dialog_yes
                ) { _, _ -> listener.removeMovie(movie) }
                .setNegativeButton(
                    R.string.dialog_no
                ) { _, _ -> }
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
            val builder = AlertDialog.Builder(it)
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