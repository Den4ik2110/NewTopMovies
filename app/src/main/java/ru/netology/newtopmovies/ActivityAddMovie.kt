package ru.netology.newtopmovies

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import ru.netology.newtopmovies.database.DataBaseManager
import ru.netology.newtopmovies.databinding.ActivityAddMovieBinding

class ActivityAddMovie : AppCompatActivity() {
    private lateinit var binding: ActivityAddMovieBinding
    private lateinit var myDataBaseManager: DataBaseManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBaseManager = DataBaseManager(this)

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
        myDataBaseManager.openDb()
        binding.buttonSaveAll.setOnClickListener {
            myDataBaseManager.insertToDataBase(
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
                when (binding.repeatAddMovie.progress) {
                    0 -> 0
                    2 -> 10
                    else -> 5
                },
                R.drawable.ic_no_image
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

}