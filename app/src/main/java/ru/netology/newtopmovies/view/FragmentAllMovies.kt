package ru.netology.newtopmovies.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.netology.newtopmovies.data.MovieNoFranchiseItem
import ru.netology.newtopmovies.databinding.FragmentAllMoviesBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.plural
import ru.netology.newtopmovies.viewModel.MovieViewModel

class FragmentAllMovies : Fragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private val section = Section()
    private var keySort = Constants.NO
    private lateinit var keyType: String
    private lateinit var keyMovies: String
    private lateinit var valueMap: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAllMoviesBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = GroupieAdapter()
        binding.recyclerViewAllMovies.adapter = adapter
        adapter.add(section)

        arguments?.let {
            keyType = it.getString("keyType") as String
            keyMovies = it.getString("keyMovies") as String
        }

        viewModel.searchDataSingle.observe(viewLifecycleOwner) { movieFromData ->
            val listItemGroup = mutableListOf<MovieNoFranchiseItem>()

            if (keyType == Constants.KEY_MOVIES && keyMovies == Constants.KEY_NO_NULL_RATING) {
                movieFromData.filter { it.rating != 0 }.forEach { movie ->
                    listItemGroup.add(
                        MovieNoFranchiseItem(
                            movie,
                            requireActivity().applicationContext,
                            view as View
                        )
                    )
                }
            }

            if (keyType == Constants.KEY_MOVIES && keyMovies == Constants.KEY_NULL_RATING) {
                movieFromData.filter { it.rating == 0 }.forEach { movie ->
                    listItemGroup.add(
                        MovieNoFranchiseItem(
                            movie,
                            requireActivity().applicationContext,
                            view as View
                        )
                    )
                }
            }

            if (keyType == Constants.KEY_FRANCHISE) {
                movieFromData.filter { it.franchise == keyMovies }.forEach { movie ->
                    listItemGroup.add(
                        MovieNoFranchiseItem(
                            movie,
                            requireActivity().applicationContext,
                            view as View
                        )
                    )
                }
            }

            if (keyType == Constants.KEY_GENRE) {
                movieFromData.forEach { movie ->
                    if (movie.genres != null) {
                        movie.genres!!.forEach {
                            if (it.genre == keyMovies) {
                                listItemGroup.add(
                                    MovieNoFranchiseItem(
                                        movie,
                                        requireActivity().applicationContext,
                                        view as View
                                    )
                                )
                            }
                        }
                    }
                }
            }

            section.update(listItemGroup)
        }

        viewModel.keySort.observe(viewLifecycleOwner) { key ->
            keySort = key
        }

        valueMap = when {
            (keyType == Constants.KEY_MOVIES && keyMovies == Constants.KEY_NO_NULL_RATING) -> "Все просмотренные фильмы"
            (keyType == Constants.KEY_MOVIES && keyMovies == Constants.KEY_NULL_RATING) -> "Ожидают вашей оценки"
            (keyType == Constants.KEY_FRANCHISE) -> keyMovies
            (keyType == Constants.KEY_GENRE) -> keyMovies.plural()
            else -> ""
        }
        viewModel.hideToolbar(mapOf("type" to Constants.FRAGMENT_ALL_MOVIES, "text" to valueMap))
        requireActivity().invalidateOptionsMenu()
    }.root
}