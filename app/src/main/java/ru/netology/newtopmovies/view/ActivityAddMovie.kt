package ru.netology.newtopmovies.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.ActivityAddMovieBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel

class ActivityAddMovie : AppCompatActivity() {
    private lateinit var binding: ActivityAddMovieBinding
    private val viewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bKriterii.setOnClickListener {
            if (binding.editTextPersonName.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Сперва введите название фильма",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.groupVisInvis.visibility = View.VISIBLE
                binding.buttonSaveAll.visibility = View.VISIBLE
                binding.bKriterii.visibility = View.GONE
                binding.buttomNotSave.visibility = View.VISIBLE

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
        }
    }

    override fun onResume() {
        super.onResume()

        binding.buttonSaveAll.setOnClickListener {
            viewModel.addMovie(Movie(
                binding.editTextPersonName.text.toString(),
                binding.valueHumor.text.toString().toInt(),
                binding.ValueMusik.text.toString().toInt(),
                binding.valueDinamic.text.toString().toInt(),
                binding.valueImage.text.toString().toInt(),
                binding.valueDialog.text.toString().toInt(),
                binding.ValueHeroes.text.toString().toInt(),
                binding.ValueAntiheroes.text.toString().toInt(),
                binding.ValueStory.text.toString().toInt(),
                binding.valueDrama.text.toString().toInt(),
                when (binding.repeatAddMovie.progress) {
                    0 -> 0
                    2 -> 10
                    else -> 5
                }))
            finish()
        }

        binding.buttomNotSave.setOnClickListener {
            Toast.makeText(applicationContext, "Фильм не сохранен", Toast.LENGTH_SHORT)
                .show()
            finish()
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

}