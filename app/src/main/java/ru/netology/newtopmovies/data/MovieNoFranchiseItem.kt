package ru.netology.newtopmovies.data


import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.OneMovieCardNoFranchiseBinding


class MovieNoFranchiseItem(
    private val movie: Movie,
    private val context: Context,
    private val view: View
) : BindableItem<OneMovieCardNoFranchiseBinding>() {

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun bind(viewBinding: OneMovieCardNoFranchiseBinding, position: Int) {
        viewBinding.apply {
            oneMovieTextTitleFranchise.text = movie.nameRu
            oneMovieFextRatingFranchise.text =
                (movie.rating / 10).toString() + "." + (movie.rating % 10).toString()
            if (movie.rating == 0) oneMovieFextRatingFranchise.visibility = View.GONE else oneMovieFextRatingFranchise.visibility = View.VISIBLE
            when (movie.rating) {
                in 70..100 -> oneMovieFextRatingFranchise.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                in 50 until 70 -> oneMovieFextRatingFranchise.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                else -> oneMovieFextRatingFranchise.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            }

            if (movie.genres.isNullOrEmpty()) {
                oneMovieGenre.text = "Жанр не задан"
            } else {
                oneMovieGenre.text = movie.genres!![0].genre
            }

            if (movie.posterUrlPreview.isNullOrEmpty()) {
                oneMoviePoster.setImageResource(R.drawable.ic_no_image)
            } else {
                Glide.with(context)
                    .load(movie.posterUrlPreview)
                    .override(500, 700)
                    .centerCrop()
                    .placeholder(R.drawable.ic_no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(oneMoviePoster)
            }

            root.setOnClickListener {
                val bundle = bundleOf("idMovie" to movie.idMovie, "idKino" to (movie.kinopoiskId ?: 0))
                try {
                    Navigation.findNavController(view).navigate(R.id.action_global_fragmentMovieInfo2, bundle)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Произошла ошибка!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun getLayout() = R.layout.one_movie_card_no_franchise

    override fun initializeViewBinding(view: View): OneMovieCardNoFranchiseBinding {
        return OneMovieCardNoFranchiseBinding.bind(view)
    }

    override fun getId(): Long = movie.idMovie

    override fun isSameAs(other: Item<*>): Boolean = viewType == other.viewType

    override fun hasSameContentAs(other: Item<*>): Boolean = this == other
}