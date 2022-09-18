package ru.netology.newtopmovies.util

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.SheetBottomMenuAndInfoMovieBinding
import ru.netology.newtopmovies.view.ActivityEditMovie
import ru.netology.newtopmovies.viewModel.MovieViewModel

class SheetBottomMenuAndInfo(
    private val movie: Movie,
    private val contextActivity: Context,
    private val showDialogDelete: ShowDialogDelete

) : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomMenuAndInfoMovieBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            binding.sheetBottomTitle.text = movie.title
            binding.sheetBottomAntiheroes.text =
                "Злодеи - ${movie.antiheroes / 10}" + "," + "${movie.antiheroes % 10}"
            binding.sheetBottomHeroes.text =
                "Герои - ${movie.heroes / 10}" + "," + "${movie.heroes % 10}"
            binding.sheetBottomHumor.text =
                "Юмор - ${movie.humor / 10}" + "," + "${movie.humor % 10}"
            binding.sheetBottomStory.text =
                "Сюжет - ${movie.story / 10}" + "," + "${movie.story % 10}"
            binding.sheetBottomDialogs.text =
                "Диалоги - ${movie.dialogs / 10}" + "," + "${movie.dialogs % 10}"
            binding.sheetBottomDynamic.text =
                "Динамика - ${movie.dynamic / 10}" + "," + "${movie.dynamic % 10}"
            binding.sheetBottomDrama.text =
                "Драма - ${movie.drama / 10}" + "," + "${movie.drama % 10}"
            binding.sheetBottomMusic.text =
                "Музыка - ${movie.music / 10}" + "," + "${movie.music % 10}"
            binding.sheetBottomImage.text =
                "Картинка - ${movie.image / 10}" + "," + "${movie.image % 10}"

            if (movie.franchise != null) binding.deleteToFranchise.visibility = View.VISIBLE else binding.deleteToFranchise.visibility = View.GONE
            if (movie.franchise != null) binding.divider17.visibility = View.VISIBLE else binding.divider17.visibility = View.GONE
            if (movie.franchise != null) binding.addInFranchise.text = "Изменить франшизу" else binding.addInFranchise.text = "Добавить во франшизу"

            binding.edit.setOnClickListener {
                closeSheetBottom()
                contextActivity.apply {
                    val intent = Intent(this, ActivityEditMovie::class.java)
                    intent.putExtra("keyEdit", movie)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                viewModel.setSearchQuery(null)
                requireActivity().findViewById<RecyclerView>(R.id.recycle_movies).scrollToPosition(0)
            }

            binding.buttomShowHideParameter.setOnClickListener {
                if (binding.groupParameter.visibility == View.GONE) {
                    binding.groupParameter.visibility = View.VISIBLE
                    binding.buttomShowHideParameter.setImageResource(R.drawable.angle_up_white)
                } else {
                    binding.groupParameter.visibility = View.GONE
                    binding.buttomShowHideParameter.setImageResource(R.drawable.angle_down_white)
                }
            }

            binding.delete.setOnClickListener {
                showDialogDelete.showDialogDelete(movie)
                closeSheetBottom()
            }

            binding.share.setOnClickListener {
                viewModel.shareMovie(movie)
                closeSheetBottom()
            }

            binding.addInFranchise.setOnClickListener {
                closeSheetBottom()
                val sheetBottomFranchise = SheetBottomFranchise(movie)
                sheetBottomFranchise.show(parentFragmentManager, SheetBottomFranchise.TAG)
            }

            binding.deleteToFranchise.setOnClickListener {
                viewModel.clearFranchiseOneMovie(movie)
                closeSheetBottom()
            }

        }.root

    companion object {
        const val TAG = "SheetBottomMenuAndInfo"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.setSearchQuery(null)
    }

    interface ShowDialogDelete {
        fun showDialogDelete(movie: Movie)
    }
}