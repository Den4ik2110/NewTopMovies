package ru.netology.newtopmovies.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.viewModel.MovieViewModel


class ArrayAdapterCustom(context: Context, resource: Int, objects: MutableList<Movie?>,
                         private val view: View, private val viewModel: MovieViewModel
) :
    ArrayAdapter<Movie?>(context, resource, objects) {

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewNew = convertView
        val movie: Movie? = getItem(position)

        if (convertViewNew == null) {
            convertViewNew = LayoutInflater.from(context)
                .inflate(R.layout.array_adapter, null)
        }
        (convertViewNew!!.findViewById(R.id.textView3) as TextView).text = movie?.nameRu

        if (movie?.nameOriginal == null) {
            (convertViewNew.findViewById(R.id.textView7) as TextView).visibility = View.GONE
        } else {
            (convertViewNew.findViewById(R.id.textView7) as TextView).visibility = View.VISIBLE
            (convertViewNew.findViewById(R.id.textView7) as TextView).text = "${movie.nameOriginal}, "
        }

        (convertViewNew.findViewById(R.id.textView15) as TextView).text = movie?.year
        val imageView = (convertViewNew.findViewById(R.id.imageView8) as ImageView)
        Glide.with(context)
            .load(movie?.posterUrlPreview)
            .placeholder(R.drawable.ic_no_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)


        convertViewNew.setOnClickListener {
            if (movie != null) {
                viewModel.saveSearchQuery(Movie(movie.nameRu,movie.humor,movie.music,movie.dynamic,movie.image,movie.dialogs,movie.heroes, movie.antiheroes,
                    movie.story, movie.drama, movie.repeat, 0, movie.rating, movie.isClicked, movie.posterUrlPreview, movie.genres, movie.year,
                    movie.franchise, movie.kinopoiskId, movie.nameOriginal, movie.posterUrl, movie.coverUrl, movie.logoUrl, movie.ratingKinopoisk,
                    movie.ratingKinopoiskVoteCount, movie.ratingImdb, movie.ratingImdbVoteCount, movie.webUrl, movie.filmLength, movie.description,
                    movie.ratingAgeLimits, movie.countries))
            }
            val bundle = bundleOf("idMovie" to movie?.idMovie, "idKino" to movie?.kinopoiskId)
            Navigation.findNavController(view)
                .navigate(R.id.action_global_fragmentMovieInfo2, bundle)
        }

        return convertViewNew
    }
}