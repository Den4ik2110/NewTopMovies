package ru.netology.newtopmovies.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.netology.newtopmovies.data.WishMovie
import ru.netology.newtopmovies.databinding.SheetBottomWishMovieAddBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel

class SheetBottomWishMovieAdd : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomWishMovieAddBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            binding.sheetBottomWishMovieSave.setOnClickListener {
                if (binding.sheetBottomWishMovieTitle.text.isNotBlank() && binding.sheetBottomWishMovieYear.text.isNotBlank()) {
                    viewModel.saveWishMovie(
                        WishMovie(
                            binding.sheetBottomWishMovieTitle.text.toString(),
                            binding.sheetBottomWishMovieYear.text.toString().toInt()
                        )
                    )
                    closeSheetBottom()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Заполните все поля",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            binding.sheetBottomWishMovieNotSave.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Фильм не сохранен",
                    Toast.LENGTH_SHORT
                ).show()
                closeSheetBottom()
            }
        }.root

    companion object {
        const val TAG = "SheetBottomWishMovieAdd"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }
}