package ru.netology.newtopmovies.view


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.ExpandableMovie
import ru.netology.newtopmovies.data.HeaderItemMovie
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.MovieNoFranchiseItem
import ru.netology.newtopmovies.databinding.FragmentViewMoviesBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.SheetBottomMenuAndInfo
import ru.netology.newtopmovies.viewModel.MovieViewModel
import java.util.*
import kotlin.concurrent.schedule

class FragmentViewMovies : Fragment(), SheetBottomMenuAndInfo.ShowDialogDelete,
    MovieNoFranchiseItem.SheetBottom {

    private val viewModel by activityViewModels<MovieViewModel>()
    private lateinit var arrayForAdapterSearch: MutableList<String>
    private val section = Section()
    private var keySort = Constants.NO
    private var listFranchise = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentViewMoviesBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = GroupieAdapter()
        binding.recycleMovies.adapter = adapter
        arrayForAdapterSearch = mutableListOf()
        adapter.add(section)

        viewModel.searchDataSingle.observe(viewLifecycleOwner) { movieFromData ->
            val listMovieNotFranchise = mutableListOf<Movie>()
            listMovieNotFranchise.addAll(movieFromData)
            val listItem = mutableListOf<MovieNoFranchiseItem>()

            movieFromData.forEach { movie ->
                listItem.add(
                    MovieNoFranchiseItem(
                        movie,
                        requireActivity().applicationContext,
                        this
                    )
                )
            }
            if (movieFromData.size < 2) section.removeHeader() else section.setHeader(
                HeaderItemMovie(
                    keySort,
                    parentFragmentManager
                )
            )

            if (movieFromData.size < 20) binding.floatingActionButton.visibility =
                View.GONE else binding.floatingActionButton.visibility = View.VISIBLE
            section.update(listItem)
            requireActivity().findViewById<TextView>(R.id.toolbar_text).text =
                if (movieFromData.isEmpty()) "Добавь новый фильм ->" else "Всего фильмов - ${movieFromData.size}"
            if (arrayForAdapterSearch.isNotEmpty()) arrayForAdapterSearch.clear()
            movieFromData.forEach { movie ->
                arrayForAdapterSearch.add(movie.title)
            }
            viewModel.setAdapterSearch(arrayForAdapterSearch)
        }

        viewModel.keySort.observe(viewLifecycleOwner) { key ->
            keySort = key
        }

        viewModel.shareMovie.observe(viewLifecycleOwner) { movie ->
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getStringShare(movie))
                type = "text/plain"
            }.also { intent ->
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.text_share_message))
                startActivity(shareIntent)
            }
        }

        viewModel.hideToolbar(Constants.FRAGMENT_MOVIE)
        requireActivity().invalidateOptionsMenu()
    }.root

    override fun showDialogDelete(movie: Movie) {
        val alertDialog = DeleteMovieDialogFragment(viewModel, movie)
        alertDialog.show(parentFragmentManager, "keyOne")
    }

    override fun sheetShow(movie: Movie) {
        val sheetBottomMenuAndInfo =
            SheetBottomMenuAndInfo(movie, requireActivity().applicationContext, this)
        sheetBottomMenuAndInfo.show(parentFragmentManager, SheetBottomMenuAndInfo.TAG)
    }

    private fun getStringShare(movie: Movie): String {
        return "Посмотрел недавно фильм \"${movie.title}\". \n" +
                "\n" +
                "Я оценил его в ${movie.rating} ${
                    rounding(
                        movie.rating,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 100 \n" +
                "\n" +
                "Вот все критерии, по которым я оценивал:\n" +
                "Юмор - ${movie.humor} ${
                    rounding(
                        movie.humor,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Музыка - ${movie.music} ${
                    rounding(
                        movie.music,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Динамика - ${movie.dynamic} ${
                    rounding(
                        movie.dynamic,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Картинка - ${movie.image} ${
                    rounding(
                        movie.image,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Диалоги - ${movie.dialogs} ${
                    rounding(
                        movie.dialogs,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Герои - ${movie.heroes} ${
                    rounding(
                        movie.heroes,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Злодеи - ${movie.antiheroes} ${
                    rounding(
                        movie.antiheroes,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "История - ${movie.story} ${
                    rounding(
                        movie.story,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Драма - ${movie.drama} ${
                    rounding(
                        movie.drama,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "\n" +
                "Если тебе интересно мое мнение, тогда знай: ${isRepeat(movie.repeat)}"
    }

    private fun rounding(amount: Int, key: String): String = if (amount in 11..14) {
        if (key == "movie") "баллов" else "фильмов"
    } else {
        when (amount % 10) {
            1 -> if (key == "movie") "балл" else "фильм"
            in 2..4 -> if (key == "movie") "балла" else "фильма"
            else -> if (key == "movie") "баллов" else "фильмов"
        }
    }

    private fun isRepeat(repeat: Int): String {
        return when (repeat) {
            0 -> "я пересматривать этот фильм не собираюсь!"
            10 -> "я с радостью посмотрю его еще ни один раз!"
            else -> "В рамках общей истории посмотрю, но сам по себе фильм - ни рыба, ни мясо!"
        }
    }
}