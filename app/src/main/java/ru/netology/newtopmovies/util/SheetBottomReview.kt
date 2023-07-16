package ru.netology.newtopmovies.util



import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.SheetBottomReviewBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel

class SheetBottomReview(
    private val movie: Movie
) : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomReviewBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            if (movie.review != null) binding.editTextText.setText(movie.review)
            binding.buttonSaveAll2.setOnClickListener {
                if (binding.editTextText.text.toString().isEmpty()) {
                    viewModel.updateReview(null, movie.idMovie)
                    closeSheetBottom()
                } else {
                    viewModel.updateReview(binding.editTextText.text.toString(), movie.idMovie)
                    closeSheetBottom()
                }
            }
        }.root

    companion object {
        const val TAG = "SheetBottomReview"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.setSearchQuery(null)
    }
}