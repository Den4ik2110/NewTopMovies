package ru.netology.newtopmovies.view


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.PagerSnapHelper
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.FranchiseOnFromList
import ru.netology.newtopmovies.data.MovieNoFranchiseItem
import ru.netology.newtopmovies.data.OneGenre
import ru.netology.newtopmovies.databinding.FragmentViewMoviesBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.plural
import ru.netology.newtopmovies.viewModel.MovieViewModel

class FragmentViewMovies : Fragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private lateinit var arrayForAdapterSearch: MutableList<String>
    private val sectionOne = Section()
    private val sectionTwo = Section()
    private val sectionThree = Section()
    private val sectionFour = Section()
    private var keySort = Constants.NO


    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentViewMoviesBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapterOne = GroupieAdapter()
        val adapterTwo = GroupieAdapter()
        val adapterThree = GroupieAdapter()
        val adapterFour = GroupieAdapter()
        binding.recycleViewOne.adapter = adapterOne
        binding.recycleViewTwo.adapter = adapterTwo
        binding.recycleViewThree.adapter = adapterThree
        binding.recycleViewFour.adapter = adapterFour
        arrayForAdapterSearch = mutableListOf()
        adapterOne.add(sectionOne)
        adapterTwo.add(sectionTwo)
        adapterThree.add(sectionThree)
        adapterFour.add(sectionFour)

        val snapHelper1 = PagerSnapHelper()
        snapHelper1.attachToRecyclerView(binding.recycleViewFour)
        val snapHelper2 = PagerSnapHelper()
        snapHelper2.attachToRecyclerView(binding.recycleViewThree)

        requireActivity().findViewById<TextView>(R.id.toolbar_text).visibility = View.VISIBLE

        viewModel.searchDataSingle.observe(viewLifecycleOwner) { movieFromData ->
            val listItemGroupNull = mutableListOf<MovieNoFranchiseItem>()
            val listItemGroupAll = mutableListOf<MovieNoFranchiseItem>()
            val listFranchise = mutableListOf<String>()
            val listFranchiseNoDublicate = mutableListOf<FranchiseOnFromList>()
            val listGenre = mutableListOf<String>()
            val listGenreNoDublicate = mutableListOf<OneGenre>()
            movieFromData.forEach { movie ->
                if (movie.rating == 0) {
                    listItemGroupNull.add(
                        MovieNoFranchiseItem(
                            movie,
                            requireActivity().applicationContext,
                            view as View
                        )
                    )
                } else {
                    listItemGroupAll.add(
                        MovieNoFranchiseItem(
                            movie,
                            requireActivity().applicationContext,
                            view as View
                        )
                    )
                }
                if (movie.franchise != null) listFranchise.add(movie.franchise!!)
                if (movie.genres != null) {
                    movie.genres!!.forEach {
                        listGenre.add(it.genre.toString())
                    }
                }
            }
            val listFranchiseNonDouble = listFranchise.distinct()
            listFranchiseNonDouble.forEachIndexed { index, s ->
                listFranchiseNoDublicate.add(FranchiseOnFromList(index.toLong(), s))
            }

            listGenre.forEach { string ->
                string.lowercase()
            }
            val listGenreNonDouble = listGenre.distinct().toMutableList()
            listGenreNonDouble.forEach { string ->
                string.filterNot { it.isWhitespace() }
            }
            listGenreNonDouble.removeAll(listOf(""))
            listGenreNonDouble.forEachIndexed { index, s ->
                listGenreNoDublicate.add(OneGenre(index.toLong(),  s.plural()))
            }

            if (listItemGroupAll.count() < 20) {
                binding.textView14.visibility = View.GONE
            } else {
                binding.textView14.visibility = View.VISIBLE
            }

            if (listItemGroupNull.count() < 20) {
                binding.textView16.visibility = View.GONE
            } else {
                binding.textView16.visibility = View.VISIBLE

            }

            sectionOne.update(listItemGroupNull.take(20))
            sectionTwo.update(listItemGroupAll.take(20))
            sectionThree.update(listFranchiseNoDublicate)
            sectionFour.update(listGenreNoDublicate)
        }

        binding.textView14.setOnClickListener {
            val bundle = bundleOf("keyType" to Constants.KEY_MOVIES, "keyMovies" to Constants.KEY_NO_NULL_RATING)
            Navigation.findNavController(view as View).navigate(R.id.action_fragmentViewMovies_to_fragmentAllMovies, bundle)
        }

        binding.textView16.setOnClickListener {
            val bundle = bundleOf("keyType" to Constants.KEY_MOVIES, "keyMovies" to Constants.KEY_NULL_RATING)
            Navigation.findNavController(view as View).navigate(R.id.action_fragmentViewMovies_to_fragmentAllMovies, bundle)
        }

        viewModel.keySort.observe(viewLifecycleOwner) { key ->
            keySort = key
        }

        viewModel.hideToolbar(mapOf("type" to Constants.FRAGMENT_MOVIE, "text" to "Cinema Trip"))
        requireActivity().invalidateOptionsMenu()
    }.root
}