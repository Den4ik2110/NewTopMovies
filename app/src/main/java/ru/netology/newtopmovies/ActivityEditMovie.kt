package ru.netology.newtopmovies

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import ru.netology.newtopmovies.database.DataBaseManager
import ru.netology.newtopmovies.databinding.ActivityEditMovieBinding

class ActivityEditMovie : AppCompatActivity() {
    private lateinit var binding: ActivityEditMovieBinding
    private lateinit var myDataBaseManager: DataBaseManager
    private lateinit var movie: Movie

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBaseManager = DataBaseManager(this)

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
        myDataBaseManager.openDb()

        binding.buttonSaveAll.setOnClickListener {
            myDataBaseManager.updateMovie(
                binding.editTextPersonName.text.toString(),
                binding.valueHumor.text.toString().toInt(),
                binding.valueDinamic.text.toString().toInt(),
                binding.ValueStory.text.toString().toInt(),
                binding.ValueHeroes.text.toString().toInt(),
                binding.ValueAntiheroes.text.toString().toInt(),
                binding.valueDrama.text.toString().toInt(),
                binding.ValueMusik.text.toString().toInt(),
                binding.valueImage.text.toString().toInt(),
                binding.valueDialog.text.toString().toInt(),
                when (binding.repeatAddMovie2.progress) {
                    0 -> 0
                    2 -> 10
                    else -> 5
                },
                R.drawable.ic_no_image,
                movie.idMovie
            )
            finish()
        }

        binding.buttomNotSave.setOnClickListener {
            Toast.makeText(applicationContext, "Фильм не сохранен", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        myDataBaseManager.closeDataBase()
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
            editTextPersonName.setText(editMovie.title)
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