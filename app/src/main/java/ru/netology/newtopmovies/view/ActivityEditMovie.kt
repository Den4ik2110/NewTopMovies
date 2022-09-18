package ru.netology.newtopmovies.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.ActivityEditMovieBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.viewModel.MovieViewModel
import java.io.IOException


class ActivityEditMovie : AppCompatActivity() {
    private lateinit var binding: ActivityEditMovieBinding
    private lateinit var movie: Movie
    private lateinit var uriImage: String
    private var uriEdit : String? = null
    private val viewModel by viewModels<MovieViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movie = intent.getSerializableExtra("keyEdit") as Movie

        chain(movie)

        movementSeekBar(binding.Humor, binding.valueHumor)
        movementSeekBar(binding.Dinamic, binding.valueDinamic)
        movementSeekBar(binding.story, binding.ValueStory)
        movementSeekBar(binding.heroes, binding.ValueHeroes)
        movementSeekBar(binding.antiheroes, binding.ValueAntiheroes)
        movementSeekBar(binding.drama, binding.valueDrama)
        movementSeekBar(binding.Musik, binding.ValueMusik)
        movementSeekBar(binding.Image, binding.valueImage)
        movementSeekBar(binding.dialog, binding.valueDialog)


    }

    override fun onResume() {
        super.onResume()

        viewModel.editUrlImage.observe(this) {url ->
            uriEdit = url
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_no_image)
                .into(binding.posterMovie)
        }

        binding.buttonSaveAll.setOnClickListener {
            viewModel.editMovie(
                Movie(
                    title = binding.editTextPersonName.text.toString(),
                    humor = binding.valueHumor.text.toString().toInt(),
                    dynamic = binding.valueDinamic.text.toString().toInt(),
                    story = binding.ValueStory.text.toString().toInt(),
                    heroes = binding.ValueHeroes.text.toString().toInt(),
                    antiheroes = binding.ValueAntiheroes.text.toString().toInt(),
                    drama = binding.valueDrama.text.toString().toInt(),
                    music = binding.ValueMusik.text.toString().toInt(),
                    image = binding.valueImage.text.toString().toInt(),
                    dialogs = binding.valueDialog.text.toString().toInt(),
                    repeat = when (binding.repeatAddMovie2.progress) {
                        0 -> 0
                        2 -> 10
                        else -> 5
                    },
                    idMovie = movie.idMovie,
                    urlImage = if (uriEdit == null) uriImage else uriEdit,
                    genre = binding.editGenre.text.toString(),
                    year = binding.editYear.text.toString(),
                    franchise = movie.franchise
                )
            )
            finish()
        }

        binding.buttomNotSave.setOnClickListener {
            Toast.makeText(applicationContext, "Фильм не сохранен", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

        binding.posterMovie.setOnClickListener {
            val downloadImageFragment = DownloadImageFragment(viewModel)
            downloadImageFragment.show(supportFragmentManager, "DownloadImageFranchise")
        }

        viewModel.editImageMovie.observe(this) {
            val intent = Intent().apply {
                action = Intent.ACTION_PICK
                type = "image/*"
            }
            val shareIntent = Intent.createChooser(intent, getString(android.R.string.search_go))
            startActivityForResult(shareIntent, Constants.INTENT_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == Constants.INTENT_PHOTO) {
            val selectedImage: Uri? = data?.data
            if (selectedImage != null) {
                uriImage = selectedImage.toString()
            }
            Glide.with(this)
                .load(uriImage)
                .override(500, 700)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_no_image)
                .into(binding.posterMovie)
        }
    }

    private fun movementSeekBar(seekBar: SeekBar, valueSeekBar: TextView) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                valueSeekBar.text = p1.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun chain(editMovie: Movie) {
        with(binding) {
            Glide.with(this@ActivityEditMovie)
                .load(movie.urlImage.toString())
                .placeholder(R.drawable.ic_no_image)
                .into(binding.posterMovie)
            editTextPersonName.setText(editMovie.title)
            editGenre.setText(movie.genre)
            if (movie.year == null) editYear.setText("") else editYear.setText(movie.year)
            Humor.progress = editMovie.humor
            Dinamic.progress = editMovie.dynamic
            story.progress = editMovie.story
            heroes.progress = editMovie.heroes
            antiheroes.progress = editMovie.antiheroes
            drama.progress = editMovie.drama
            Musik.progress = editMovie.music
            Image.progress = editMovie.image
            dialog.progress = editMovie.dialogs
            repeatAddMovie2.progress = when (editMovie.repeat) {
                0 -> 0
                10 -> 2
                else -> 1
            }

            valueHumor.text = editMovie.humor.toString()
            valueDinamic.text = editMovie.dynamic.toString()
            ValueStory.text = editMovie.story.toString()
            ValueHeroes.text = editMovie.heroes.toString()
            ValueAntiheroes.text = editMovie.antiheroes.toString()
            valueDrama.text = editMovie.drama.toString()
            ValueMusik.text = editMovie.music.toString()
            valueImage.text = editMovie.image.toString()
            valueDialog.text = editMovie.dialogs.toString()
        }
    }
}