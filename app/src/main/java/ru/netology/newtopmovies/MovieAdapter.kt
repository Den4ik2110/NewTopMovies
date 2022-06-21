package ru.netology.newtopmovies

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.newtopmovies.databinding.MovieCardBinding

class MovieAdapter(private val listener: Listener) :
    ListAdapter<Movie, MovieAdapter.MovieHolder>(DiffCallback) {

    private var movies = mutableListOf<Movie>()


    class MovieHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val bindingMovieCard = MovieCardBinding.bind(item)

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(movie: Movie, listener: Listener, movies: MutableList<Movie>) =
            with(bindingMovieCard) {
                titleMovieCard.text = movie.title
                ratingMovieCard.rating = (movie.rating * 0.1).toFloat()
                humorValueCard.text = "Юмор - ${movie.humor}"
                musicValueCard.text = "Музыка - ${movie.music}"
                dynamicValueCard.text = "Динамика - ${movie.dynamic}"
                imageValueCard.text = "Картинка - ${movie.image}"
                dialogsValueCard.text = "Диалоги - ${movie.dialogs}"
                heroesValueCard.text = "Герои - ${movie.heroes}"
                antiheroesValueCard.text = "Злодеи - ${movie.antiheroes}"
                storyValueCard.text = "Сюжет - ${movie.story}"
                dramaValueCard.text = "Драма - ${movie.drama}"
                ratingValue.text = "Рейтинг ${(ratingMovieCard.rating * 10).toInt()}"
                when (movie.repeat) {
                    0 -> bindingMovieCard.repeat.setImageResource(R.drawable.ic_card_repeat_0)
                    5 -> bindingMovieCard.repeat.setImageResource(R.drawable.ic_card_repeat_5)
                    10 -> bindingMovieCard.repeat.setImageResource(R.drawable.ic_card_repeat_10)
                }
                itemView.setOnClickListener {
                    listener.onClick(movie, itemView, movies)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(movies[position], listener, movies)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setMoviesList(movieList: MutableList<Movie>) {
        movies = movieList.asReversed()
        repeatMoviesList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun repeatMoviesList() {
        notifyDataSetChanged()
    }

    interface Listener {
        fun onClick(movie: Movie, item: View, movies: MutableList<Movie>)

    }

    fun delete(movie: Movie) {
        val listForDelete = mutableListOf<Movie>()

        for (movieFromList in movies) {
            if (movieFromList.idMovie == movie.idMovie) listForDelete.add(movieFromList)
        }
        for (movieDelete in listForDelete) movies.remove(movieDelete)

        listForDelete.clear()
        repeatMoviesList()
    }

    fun sortTitleAtoZ() {
        movies.sortBy { it.title }
        repeatMoviesList()
    }

    fun sortRatingMinToMax() {
        val comparatorRating = Comparator { p1: Movie, p2: Movie -> p1.rating - p2.rating }
        movies.sortWith(comparatorRating.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) })
        repeatMoviesList()
    }

    fun sortRatingMaxToMin() {
        val comparatorRating = Comparator { p1: Movie, p2: Movie -> p2.rating - p1.rating }
        movies.sortWith(comparatorRating.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) })
        repeatMoviesList()
    }

    fun sortOldNew() {
        movies.sortBy { it.idMovie }
        repeatMoviesList()
    }

    fun sortNewOld() {
        movies.sortByDescending { it.idMovie }
        repeatMoviesList()
    }

    fun sortRepeat() {
        val comparatorRepeat = Comparator { p1: Movie, p2: Movie -> p2.repeat - p1.repeat }
        movies.sortWith(comparatorRepeat.thenComparator { p1, p2 -> p1.title.compareTo(p2.title) })
        repeatMoviesList()
    }

    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.idMovie == newItem.idMovie


        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            newItem == oldItem

    }
}




