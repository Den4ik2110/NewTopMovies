package ru.netology.newtopmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.newtopmovies.databinding.MovieCardBinding

class MovieAdapter(val listener: Listener) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    private var movies = mutableListOf<Movie>()

    class MovieHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val bindingMovieCard = MovieCardBinding.bind(item)
        fun bind(movie: Movie, listener: Listener) = with(bindingMovieCard) {
            imageMovieCard.setImageResource(movie.iconRes)
            titleMovieCard.text = movie.title
            ratingMovieCard.rating = (movie.rating * 0.05).toFloat()
            ratingValue.text = (ratingMovieCard.rating * 20).toInt().toString()
            if (movie.repeat) repeatMovieCard.setText(R.string.watch_again)
            itemView.setOnClickListener {
                listener.onClick(movie)
            }
            listener.onLongClick(itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(movies[position], listener)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun addMovie(movie: Movie) {
        movies.add(movie)
        repeatMoviesList()
    }

    fun repeatMoviesList() {
        notifyDataSetChanged()
    }

    interface Listener {
        fun onClick(movie: Movie)

        fun onLongClick(view: View)
    }

}