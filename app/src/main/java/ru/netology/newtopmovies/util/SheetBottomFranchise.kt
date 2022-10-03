package ru.netology.newtopmovies.util

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.netology.newtopmovies.data.FranchiseOnFromList
import ru.netology.newtopmovies.data.HeaderFranchise
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.SheetBottomFranchiseBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel


class SheetBottomFranchise(
    private val movie: Movie
) : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private val section = Section()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomFranchiseBinding.inflate(layoutInflater, container, false).also {
        binding ->
        val adapter = GroupieAdapter()
        adapter.add(section)
        binding.recycleFranchise.adapter = adapter

        viewModel.listFranchise.observe(viewLifecycleOwner) {list ->
            val futureList = mutableListOf<FranchiseOnFromList>()
            list.forEach { franchise ->
                futureList.add(FranchiseOnFromList(franchise, viewModel, movie, parentFragmentManager, this))
            }
            section.update(futureList)
        }
    }.root

    companion object {
        const val TAG = "SheetBottomFranchise"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.setSearchQuery(null)
    }
}