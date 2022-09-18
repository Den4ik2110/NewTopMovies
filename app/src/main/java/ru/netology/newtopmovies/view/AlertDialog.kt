package ru.netology.newtopmovies.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Franchise
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.util.editUrlImage
import ru.netology.newtopmovies.viewModel.MovieViewModel


class DeleteMovieDialogFragment(
    private val listener: MovieInteractionListener,
    private val movie: Movie
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setMessage(R.string.dialog_start_delete)
                .setPositiveButton(
                    R.string.dialog_yes
                ) { _, _ ->
                    listener.removeMovie(movie)
                    listener.setSearchQuery(null)
                    requireActivity().findViewById<RecyclerView>(R.id.recycle_movies)
                        .scrollToPosition(0)
                }
                .setNegativeButton(
                    R.string.dialog_no
                ) { _, _ ->
                    listener.setSearchQuery(null)
                    requireActivity().findViewById<RecyclerView>(R.id.recycle_movies)
                        .scrollToPosition(0)
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

class AddFranchiseFragment(
    private val viewModel: MovieViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = MaterialAlertDialogBuilder(fragmentActivity)
            val inflater = fragmentActivity.layoutInflater;
            val dialogView = inflater.inflate(R.layout.edit_text_from_dialog, null);
            val editText = dialogView.findViewById<EditText>(R.id.edit_text_title_franchise)
            builder.setMessage("Введите название франшизы")
                .setView(dialogView)
                .setPositiveButton(
                    "Сохранить"
                ) { _, _ ->
                    viewModel.saveFranchise(Franchise(title = editText.text.toString()))
                }
                .setNegativeButton(
                    "Отмена"
                ) { _, _ -> }
            setStyle(STYLE_NO_TITLE, 0)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class ActionFranchiseFragment(
    private val viewModel: MovieViewModel,
    private val franchise: Franchise,
    private val movie: Movie,
    private val fragment: Fragment
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val items = arrayOf(
                "Добавить фильм в франшизу",
                "Изменить название франшизы",
                "Удалить франшизу"
            )
            val builder = MaterialAlertDialogBuilder(fragmentActivity)
            builder.setItems(items) { _, case ->
                when (case) {
                    0 -> {
                        viewModel.addToFranchise(franchise, movie)
                        parentFragmentManager.beginTransaction().remove(fragment).commit()
                    }
                    1 -> {
                        val editFranchiseFragment = EditFranchiseFragment(viewModel, franchise)
                        editFranchiseFragment.show(parentFragmentManager, "DialogEditFranchise")
                    }
                    2 -> {
                        viewModel.deleteFranchise(franchise)
                    }
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class EditFranchiseFragment(
    private val viewModel: MovieViewModel,
    private val franchise: Franchise
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = MaterialAlertDialogBuilder(fragmentActivity)
            val inflater = fragmentActivity.layoutInflater;
            val dialogView = inflater.inflate(R.layout.edit_text_from_dialog, null);
            val editText = dialogView.findViewById<EditText>(R.id.edit_text_title_franchise)
            builder.setMessage("Введите новое название франшизы")
                .setView(dialogView)
                .setPositiveButton(
                    "Сохранить"
                ) { _, _ ->
                    viewModel.editNameFranchise(franchise, editText.text.toString())
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

