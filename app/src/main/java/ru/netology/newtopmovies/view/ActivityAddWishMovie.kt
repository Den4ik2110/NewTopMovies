package ru.netology.newtopmovies.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.newtopmovies.data.WishMovie
import ru.netology.newtopmovies.databinding.ActivityAddWishMovieBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel

class ActivityAddWishMovie : AppCompatActivity() {

    private lateinit var binding: ActivityAddWishMovieBinding
    private val viewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWishMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSaveWishMovie.setOnClickListener {
            if (binding.titleWishMovie.text.isNotBlank() && binding.yearWishMovie.text.isNotBlank()) {
                viewModel.saveWishMovie(
                    WishMovie(
                        binding.titleWishMovie.text.toString(),
                        binding.yearWishMovie.text.toString().toInt()
                    )
                )
                finish()
            }
            else Toast.makeText(
                applicationContext,
                "Заполните все поля",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.buttonNotSaveWishMovie.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "Фильм не сохранен",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }
}