package ru.netology.newtopmovies.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.WishMovieItem
import ru.netology.newtopmovies.databinding.FragmentViewMoviesBinding
import ru.netology.newtopmovies.databinding.FragmentWishListBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.viewModel.MovieViewModel

class FragmentWishList : Fragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private val section = Section()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentWishListBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = GroupieAdapter()
        binding.recycleWishList.adapter = adapter
        adapter.add(section)

        viewModel.listWishMovie.observe(viewLifecycleOwner) { wishMovieFromData ->
            val listItem = mutableListOf<WishMovieItem>()
            wishMovieFromData.forEach { wishMovie ->
                listItem.add(WishMovieItem(wishMovie, parentFragmentManager))
            }
            section.update(listItem)

            if (wishMovieFromData.isEmpty()) requireActivity().findViewById<TextView>(R.id.toolbar_text).text =
                "Желаемого нет" else requireActivity().findViewById<TextView>(R.id.toolbar_text).text =
                "Фильмов к просмотру - ${wishMovieFromData.size}"
        }

        /*viewModel.searchDataSingle.observe(viewLifecycleOwner) { movieFromData ->
            requireActivity().findViewById<TextView>(R.id.toolbar_text).text = "Желаемого нет"
            if (adapter.itemCount == 0) binding.progressWork.visibility = View.VISIBLE else binding.progressWork.visibility = View.GONE
        }*/

        viewModel.hideToolbar(Constants.FRAGMENT_WISHLIST)
        requireActivity().invalidateOptionsMenu()
    }.root
}