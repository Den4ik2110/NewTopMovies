package ru.netology.newtopmovies.util

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.WishMovie
import ru.netology.newtopmovies.databinding.SheetBottomWishMovieAddBinding
import ru.netology.newtopmovies.databinding.SheetBottomWishMovieEditBinding
import ru.netology.newtopmovies.view.ActivityAddMovie
import ru.netology.newtopmovies.view.ActivityEditMovie
import ru.netology.newtopmovies.viewModel.MovieViewModel

class SheetBottomWishMovieEdit(
    private var wishMovie: WishMovie
) : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomWishMovieEditBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            binding.sheetBottomWishMovieTitleEdit.setText(wishMovie.title)
            binding.sheetBottomWishMovieYearEdit.setText("${wishMovie.year} г.")

            binding.sheetBottomWishMovieButtomEdit.setOnClickListener {
                binding.sheetBottomWishMovieYearEdit.setText("${wishMovie.year}")
                binding.sheetBottomWishMovieYearEdit.isEnabled = true
                binding.sheetBottomWishMovieTitleEdit.isEnabled = true
                binding.groupItemMenuEdit.visibility = View.GONE
                binding.groupItemMenuSave.visibility = View.VISIBLE
            }

            binding.sheetBottomWishMovieButtomSave.setOnClickListener {
                if (binding.sheetBottomWishMovieTitleEdit.text.isNotBlank() && binding.sheetBottomWishMovieYearEdit.text.isNotBlank()) {
                    wishMovie = WishMovie(
                        binding.sheetBottomWishMovieTitleEdit.text.toString(),
                        binding.sheetBottomWishMovieYearEdit.text.toString().toInt(),
                        id = wishMovie.id
                    )
                    viewModel.updateWishMovie(wishMovie)
                    binding.sheetBottomWishMovieYearEdit.isEnabled = false
                    binding.sheetBottomWishMovieTitleEdit.isEnabled = false
                    binding.groupItemMenuEdit.visibility = View.VISIBLE
                    binding.groupItemMenuSave.visibility = View.GONE
                    binding.sheetBottomWishMovieTitleEdit.setText(wishMovie.title)
                    binding.sheetBottomWishMovieYearEdit.setText("${wishMovie.year} г.")

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Заполните все поля",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.sheetBottomWishMovieButtomNotSave.setOnClickListener {
                binding.sheetBottomWishMovieYearEdit.isEnabled = false
                binding.sheetBottomWishMovieTitleEdit.isEnabled = false
                binding.groupItemMenuEdit.visibility = View.VISIBLE
                binding.groupItemMenuSave.visibility = View.GONE
                binding.sheetBottomWishMovieTitleEdit.setText(wishMovie.title)
                binding.sheetBottomWishMovieYearEdit.setText("${wishMovie.year} г.")
            }

            binding.sheetBottomWishMovieButtomDelete.setOnClickListener {
                closeSheetBottom()
                viewModel.deleteWishMovie(wishMovie)
            }

            binding.sheetBottomWishMovieButtomView.setOnClickListener {
                closeSheetBottom()
                requireContext().apply {
                    val intent = Intent(this, ActivityAddMovie::class.java)
                    intent.putExtra("keyWishMovieAdd", wishMovie)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

            }
        }.root

    companion object {
        const val TAG = "SheetBottomWishMovieEdit"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }
}