package ru.netology.newtopmovies

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DeleteMovieDialogFragment (private val answer: AnswerClick) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_start_delete)
                .setPositiveButton(R.string.dialog_yes
                ) { _, _ -> answer.answerPositive(true) }
                .setNegativeButton(R.string.dialog_no
                ) { _, _ ->  }
            setStyle(STYLE_NO_TITLE, 0)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface AnswerClick {
        fun answerPositive(answer: Boolean)
    }
}