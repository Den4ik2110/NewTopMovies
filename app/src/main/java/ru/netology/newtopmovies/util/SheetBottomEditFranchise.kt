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
import ru.netology.newtopmovies.data.FranchiseEditFragmentSheetBottom
import ru.netology.newtopmovies.data.FranchiseOnFromList
import ru.netology.newtopmovies.data.HeaderFranchise
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.SheetBottomEditFranchiseBinding
import ru.netology.newtopmovies.databinding.SheetBottomFranchiseBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel


class SheetBottomEditFranchise(
) : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private val section = Section()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomEditFranchiseBinding.inflate(layoutInflater, container, false).also {
        binding ->
        val adapter = GroupieAdapter()
        adapter.add(section)
        section.setHeader(HeaderFranchise(viewModel, parentFragmentManager))
        binding.recycleEditFranchise.adapter = adapter

        viewModel.listFranchise.observe(viewLifecycleOwner) {list ->
            val futureList = mutableListOf<FranchiseEditFragmentSheetBottom>()
            list.forEach { franchise ->
                futureList.add(FranchiseEditFragmentSheetBottom(franchise, viewModel, parentFragmentManager))
            }
            section.update(futureList)
        }
    }.root

    companion object {
        const val TAG = "SheetBottomEditFranchise"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.setSearchQuery(null)
    }
}