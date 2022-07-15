package ru.netology.newtopmovies.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.MovieCardBinding

class MovieAdapter(
    private val context: Context,
    private val dialog: ShowDialog
) : ListAdapter<Movie, MovieAdapter.MovieHolder>(DiffCallback) {


    class MovieHolder(
        private val binding: MovieCardBinding,
        private val context: Context,
        private val dialog: ShowDialog
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var movie: Movie

        private val popupMenu by lazy {
            val contextThemeWrapper = ContextThemeWrapper(context, R.style.PopupMenu)
            PopupMenu(
                contextThemeWrapper,
                binding.repeat
            ).apply {
                inflate(R.menu.context_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.context_menu_edit -> {
                            context.apply {
                                val intent = Intent(this, ActivityEditMovie::class.java)
                                intent.putExtra("keyEdit", movie)
                                startActivity(intent)
                            }
                            true
                        }
                        R.id.context_menu_remove -> {
                            dialog.showDialog(movie)
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(movie: Movie) {
            this.movie = movie

            with(binding) {
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
                    0 -> binding.repeat.setImageResource(R.drawable.ic_card_repeat_0)
                    5 -> binding.repeat.setImageResource(R.drawable.ic_card_repeat_5)
                    10 -> binding.repeat.setImageResource(R.drawable.ic_card_repeat_10)
                }
                cardMovie.setOnLongClickListener {
                    popupMenu.show()
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieCardBinding.inflate(inflater, parent, false)
        return MovieHolder(binding, context, dialog)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.idMovie == newItem.idMovie

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            newItem == oldItem
    }

    interface ShowDialog {
        fun showDialog(movie: Movie)
    }
}




