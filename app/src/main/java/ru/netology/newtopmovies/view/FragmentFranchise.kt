package ru.netology.newtopmovies.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.ExpandableMovie
import ru.netology.newtopmovies.data.HeaderItemMovie
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.MovieNoFranchiseItem
import ru.netology.newtopmovies.databinding.FragmentFranchiseBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.SheetBottomMenuAndInfo
import ru.netology.newtopmovies.viewModel.MovieViewModel

class FragmentFranchise : Fragment(), MovieNoFranchiseItem.SheetBottom,
    SheetBottomMenuAndInfo.ShowDialogDelete{

    private val viewModel by activityViewModels<MovieViewModel>()
    private val listFranchise = mutableListOf<String>()
    private val listExpandableGroup = mutableListOf<ExpandableGroup>()
    private val section = Section()
    private var keySort = Constants.NO


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFranchiseBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = GroupieAdapter()
        binding.recycleViewFranchise.adapter = adapter
        adapter.add(section)

        viewModel.searchDataFranchise.observe(viewLifecycleOwner) { franchises ->
            listExpandableGroup.clear()
            franchises.forEach { movie ->
                if (movie.franchise != null) {
                    listFranchise.add(movie.franchise!!)
                }
            }

            val listNoDouble = listFranchise.distinct()

            listNoDouble.forEach { name ->
                val listMovieInFranchise = franchises.filter { it.franchise == name }
                val group = ExpandableGroup(ExpandableMovie(name, listMovieInFranchise.size, listMovieInFranchise))
                listExpandableGroup.add(group)
            }

            listNoDouble.forEachIndexed { index, name ->
                franchises.forEach { movie ->
                    if (movie.franchise == name) {
                        listExpandableGroup[index].add(
                            MovieNoFranchiseItem(
                                movie,
                                requireActivity().applicationContext,
                                this
                            )
                        )
                    }
                }
            }
            val listFullGroup = listExpandableGroup.filter { it.childCount != 0 }
            section.update(listFullGroup)

            requireActivity().findViewById<TextView>(R.id.toolbar_text).text =
                if (franchises.isEmpty()) "Франшиз нет" else "Всего франшиз - ${section.groupCount}"
        }

        viewModel.hideToolbar(Constants.FRAGMENT_FRANCHISE)
        requireActivity().invalidateOptionsMenu()
    }.root

    override fun sheetShow(movie: Movie) {
        val sheetBottomMenuAndInfo =
            SheetBottomMenuAndInfo(movie, requireActivity().applicationContext, this)
        sheetBottomMenuAndInfo.show(parentFragmentManager, SheetBottomMenuAndInfo.TAG)
    }

    override fun showDialogDelete(movie: Movie) {
        val alertDialog = DeleteMovieDialogFragment(viewModel, movie)
        alertDialog.show(parentFragmentManager, "keyOne")
    }
}