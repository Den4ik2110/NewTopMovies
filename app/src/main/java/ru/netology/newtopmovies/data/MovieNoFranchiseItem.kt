package ru.netology.newtopmovies.data


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.FragmentViewMoviesBinding
import ru.netology.newtopmovies.databinding.OneMovieCardNoFranchiseBinding

class MovieNoFranchiseItem(
    private val movie: Movie,
    private val context: Context,
    private val sheetShowInterface: SheetBottom
) : BindableItem<OneMovieCardNoFranchiseBinding>() {

    @SuppressLint("SetTextI18n")
    override fun bind(viewBinding: OneMovieCardNoFranchiseBinding, position: Int) {
        viewBinding.apply {
            oneMovieTextTitleFranchise.text = movie.title
            oneMovieTextRatingFranchise.text =
                (movie.rating / 10).toString() + "," + (movie.rating % 10).toString()
            if (movie.year.isNullOrEmpty()) oneMovieYear.text = "Год не задан" else oneMovieYear.text =
                "${movie.year} г."
            if (movie.genre.isNullOrEmpty()) oneMovieGenre.text = "Жанр не задан" else oneMovieGenre.text =
                movie.genre
            if (movie.urlImage.isNullOrEmpty()) {
                oneMoviePoster.setImageResource(R.drawable.ic_no_image)
            } else {
                Glide.with(context)
                    .load(movie.urlImage)
                    .override(500, 700)
                    .centerCrop()
                    .placeholder(R.drawable.ic_no_image)
                    .into(oneMoviePoster)
            }
        }

        viewBinding.root.setOnClickListener {
            sheetShowInterface.sheetShow(movie)
        }
    }

    override fun getLayout() = R.layout.one_movie_card_no_franchise

    override fun initializeViewBinding(view: View): OneMovieCardNoFranchiseBinding {
        return OneMovieCardNoFranchiseBinding.bind(view)
    }

    override fun getId(): Long = movie.idMovie

    override fun isSameAs(other: Item<*>): Boolean = viewType == other.viewType

    override fun hasSameContentAs(other: Item<*>): Boolean = this == other

    interface SheetBottom {
        fun sheetShow(movie: Movie)
    }
}