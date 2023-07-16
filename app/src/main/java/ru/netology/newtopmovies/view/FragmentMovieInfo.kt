package ru.netology.newtopmovies.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.MovieNoFranchiseItem
import ru.netology.newtopmovies.databinding.FragmentMovieInfoBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.SheetBottomMenuAndInfo
import ru.netology.newtopmovies.viewModel.MovieViewModel
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.util.SheetBottomRating
import ru.netology.newtopmovies.util.SheetBottomReview
import ru.netology.newtopmovies.util.toListString

class FragmentMovieInfo : Fragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private var keySort = Constants.NO
    private var movieId: Long = 0
    private var kinoId: Int? = null
    private val section = Section()
    private val listSequelsAndPrequels = mutableListOf<Movie>()
    private val listSequelsAndPrequelsItem = mutableListOf<MovieNoFranchiseItem>()
    private  lateinit var currentMovie: Movie


    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieInfoBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = GroupieAdapter()
        binding.recycleViewMovieInfo.adapter = adapter
        adapter.add(section)

        arguments?.let {
            movieId = it.getLong("idMovie")
            kinoId = it.getInt("idKino")
        }



        viewModel.searchDataSingle.observe(viewLifecycleOwner) { listMovie ->

            listMovie.forEach { replaceMovie ->

                if (replaceMovie.idMovie == movieId || replaceMovie.kinopoiskId == kinoId) {
                    currentMovie = replaceMovie
                    binding.apply {

                        Glide.with(context as Context)
                            .load(replaceMovie.coverUrl ?: replaceMovie.posterUrlPreview)
                            .placeholder(R.drawable.ic_no_image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView10)

                        imageView10.setOnClickListener {
                            Log.d("MyLog", replaceMovie.kinopoiskId.toString())
                        }

                        textView23.text = replaceMovie.nameRu

                        if (replaceMovie.nameOriginal == null) {
                            textView24.visibility = View.GONE
                        } else {
                            textView24.visibility = View.VISIBLE
                            textView24.text = replaceMovie.nameOriginal
                        }

                        textView26.text = "Ваша оценка: ${(replaceMovie.rating / 10)}. ${(replaceMovie.rating % 10)}"
                        when (replaceMovie.rating) {
                            in 70..100 -> textView26.setTextColor(ContextCompat.getColor(context as Context, R.color.green))
                            in 50 until 70 -> textView26.setTextColor(ContextCompat.getColor(context as Context, R.color.grey))
                            else -> textView26.setTextColor(ContextCompat.getColor(context as Context, R.color.red))
                        }

                        var stringGenres = ""
                        stringGenres += "${replaceMovie.year}, "
                        replaceMovie.genres?.forEachIndexed { index, genre ->
                            stringGenres += if (index < replaceMovie.genres!!.size - 1) genre.genre + ", " else genre.genre
                        }
                        textView8.text = stringGenres

                        var stringCountry = ""
                        if (replaceMovie.countries.isNullOrEmpty()) {
                            stringCountry += "Cтрана, "
                        } else {
                            replaceMovie.countries.forEach { country ->
                                stringCountry += "${country.country}, "
                            }
                        }
                        stringCountry += if (replaceMovie.filmLength == null) "Продолжительность, " else "${(replaceMovie.filmLength / 60)} ч ${replaceMovie.filmLength % 60} мин, "
                        stringCountry += if (replaceMovie.ratingAgeLimits == null) "Возраст" else "${replaceMovie.ratingAgeLimits.drop(3)}+"
                        textView17.text = stringCountry

                        imageButton4.setOnClickListener {
                            sheetShowRating(replaceMovie)
                        }

                        imageButton.setOnClickListener {
                            showDialogDelete(replaceMovie)
                        }

                        imageButton3.setOnClickListener {
                            viewModel.shareMovie(replaceMovie)
                        }

                        imageButton2.setOnClickListener {
                            sheetShowMenu(replaceMovie)
                        }

                        textView21.setOnClickListener {
                            sheetShowReview(replaceMovie)
                        }

                        textView19.text = replaceMovie.description
                    }
                }
            }

            listSequelsAndPrequels.clear()
            listSequelsAndPrequelsItem.clear()

            listMovie.forEach { movie ->
                if (currentMovie.sequelsAndPrequels != null)
                    if (currentMovie.sequelsAndPrequels.toListString()
                            ?.contains(movie.kinopoiskId.toString()) == true) {
                        listSequelsAndPrequels.add(movie)
                    }
            }

            if (listSequelsAndPrequels.isEmpty()) {
                binding.divider5.visibility = View.GONE
                binding.textView22.visibility = View.GONE
                binding.recycleViewMovieInfo.visibility = View.GONE
            } else {
                binding.divider5.visibility = View.VISIBLE
                binding.textView22.visibility = View.VISIBLE
                binding.recycleViewMovieInfo.visibility = View.VISIBLE
            }

            listSequelsAndPrequels.sortedBy { it.year }.forEach { movie ->
                listSequelsAndPrequelsItem.add(MovieNoFranchiseItem(movie, requireActivity().applicationContext, view as View))
            }
            section.update(listSequelsAndPrequelsItem)
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

        viewModel.keySort.observe(viewLifecycleOwner) { key ->
            keySort = key
        }

        viewModel.hideToolbar(mapOf("type" to Constants.FRAGMENT_MOVIE_INFO, "text" to "Movie"))
        requireActivity().invalidateOptionsMenu()
    }.root

    private fun getStringShare(movie: Movie): String {
        return "Посмотрел недавно фильм \"${movie.nameRu}\". \n" +
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

    private fun sheetShowMenu(movie: Movie) {
        val sheetBottomMenuAndInfo =
            SheetBottomMenuAndInfo(movie, requireActivity().applicationContext)
        sheetBottomMenuAndInfo.show(parentFragmentManager, SheetBottomMenuAndInfo.TAG)
    }

    private fun sheetShowReview(movie: Movie) {
        val sheetBottomReview =
            SheetBottomReview(movie)
        sheetBottomReview.show(parentFragmentManager, SheetBottomReview.TAG)
    }

    private fun sheetShowRating(movie: Movie) {
        val sheetBottomRating =
            SheetBottomRating(movie)
        sheetBottomRating.show(parentFragmentManager, SheetBottomRating.TAG)
    }

    private fun showDialogDelete(movie: Movie) {
        val alertDialog = DeleteMovieDialogFragment(viewModel, movie)
        alertDialog.show(parentFragmentManager, "keyOne")
    }
}

