package ru.netology.newtopmovies.util

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors.getColor
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.SheetBottomSortBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel

class SheetBottomSort() : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomSortBinding.inflate(layoutInflater, container, false).also {
        binding ->
        binding.sortMaxMin.setOnClickListener {
            viewModel.sortMovie(Constants.MAX_MIN)
        }

        binding.sortMinMax.setOnClickListener {
            viewModel.sortMovie(Constants.MIN_MAX)
        }

        binding.sortRepeat.setOnClickListener {
            viewModel.sortMovie(Constants.REPEAT)
        }

        binding.sortAlphabet.setOnClickListener {
            viewModel.sortMovie(Constants.ALPHABET)
        }

        binding.sortNewOld.setOnClickListener {
            viewModel.sortMovie(Constants.NEW_OLD)
        }

        binding.sortOldNew.setOnClickListener {
            viewModel.sortMovie(Constants.OLD_NEW)
        }

        viewModel.keySort.observe(viewLifecycleOwner) { key ->
            setBackground(key,binding)
        }
    }.root

    companion object {
        const val TAG = "SheetBottomSort"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.setSearchQuery(null)
    }
    private fun setBackground(key: String, binding: SheetBottomSortBinding) {
        binding.apply {
            sortAlphabet.setBackgroundColor(resources.getColor(R.color.choco_milk))
            sortMaxMin.setBackgroundColor(resources.getColor(R.color.choco_milk))
            sortRepeat.setBackgroundColor(resources.getColor(R.color.choco_milk))
            sortNewOld.setBackgroundColor(resources.getColor(R.color.choco_milk))
            sortOldNew.setBackgroundColor(resources.getColor(R.color.choco_milk))
            sortMinMax.setBackgroundColor(resources.getColor(R.color.choco_milk))
            when (key) {
                Constants.MIN_MAX -> {sortMinMax.setBackgroundResource(R.drawable.rounded_text_view_sort)}
                Constants.MAX_MIN -> {sortMaxMin.setBackgroundResource(R.drawable.rounded_text_view_sort)}
                Constants.OLD_NEW -> {sortOldNew.setBackgroundResource(R.drawable.rounded_text_view_sort)}
                Constants.NEW_OLD -> {sortNewOld.setBackgroundResource(R.drawable.rounded_text_view_sort)}
                Constants.REPEAT -> {sortRepeat.setBackgroundResource(R.drawable.rounded_text_view_sort)}
                else -> {sortAlphabet.setBackgroundResource(R.drawable.rounded_text_view_sort)}
            }
        }
    }
}