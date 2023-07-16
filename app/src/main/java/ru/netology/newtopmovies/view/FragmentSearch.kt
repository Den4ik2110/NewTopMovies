package ru.netology.newtopmovies.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.MovieNoFranchiseItem
import ru.netology.newtopmovies.databinding.FragmentSearchBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.viewModel.MovieViewModel

class FragmentSearch : Fragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private val section = Section()
    private lateinit var arrayForAdapterSearch: MutableList<Movie?>
    private lateinit var listMoviesForDelete: MutableList<Movie?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSearchBinding.inflate(layoutInflater, container, false).also { binding ->
        arrayForAdapterSearch = mutableListOf()
        val adapter = GroupieAdapter()
        binding.recycleViewSearchFragment.adapter = adapter
        adapter.add(section)


        viewModel.searchDataSingle.observe(viewLifecycleOwner) {movie ->

            if (arrayForAdapterSearch.isNotEmpty()) arrayForAdapterSearch.clear()
            arrayForAdapterSearch.addAll(movie)

            viewModel.setAdapterSearch(arrayForAdapterSearch)
        }

        viewModel.dataSearchQuery.observe(viewLifecycleOwner) { movie ->
            listMoviesForDelete = movie.toMutableList()
            val listStorySearch = mutableListOf<MovieNoFranchiseItem>()
            movie.forEach { movieLocal ->
                listStorySearch.add(
                    MovieNoFranchiseItem(
                        movieLocal,
                        requireActivity().applicationContext,
                        view as View
                    )
                )
            }
            section.update(listStorySearch.take(12))

            if (listStorySearch.isNotEmpty()) binding.textView27.visibility = View.VISIBLE else binding.textView27.visibility = View.GONE
        }

        binding.textView27.setOnClickListener {
            viewModel.deleteAllSearchQuery()
        }

        viewModel.hideToolbar(mapOf("type" to Constants.FRAGMENT_SEARCH, "text" to "Search"))
        requireActivity().invalidateOptionsMenu()
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        if (listMoviesForDelete.size > 12) {
            repeat (listMoviesForDelete.size - 12) {
                viewModel.deleteSearchQuery(listMoviesForDelete[listMoviesForDelete.size - 1] as Movie)
                listMoviesForDelete.removeAt(listMoviesForDelete.size - 1)
            }
        }
    }
}